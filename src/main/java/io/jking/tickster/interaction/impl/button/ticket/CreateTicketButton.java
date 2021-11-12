package io.jking.tickster.interaction.impl.button.ticket;


import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.Result;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class CreateTicketButton implements IButton {

    @Override
    public void onInteraction(ButtonContext context) {

        context.reply("Please wait as I attempt to create a ticket...").queue(ignored -> {
            final long guildId = context.getGuild().getIdLong();
            final long memberId = context.getMember().getIdLong();

            context.getDatabase().getDSL().selectFrom(GUILD_TICKETS)
                    .where(GUILD_TICKETS.GUILD_ID.eq(guildId))
                    .and(GUILD_TICKETS.CREATOR_ID.eq(memberId))
                    .fetchAsync(context.getDatabase().getExecutor())
                    .thenAcceptAsync(record -> {
                        final GuildTicketsRecord fetchedRecord = record.get(0);
                        if (!fetchedRecord.getOpen()) {
                            createTicketProcess(context);
                            return;
                        }

                        final long ticketChannelId = fetchedRecord.getChannelId();
                        final TextChannel channel = context.getGuild().getTextChannelById(ticketChannelId);
                        if (channel != null) {
                            context.getHook().editOriginalFormat("It seems you already have a ticket opened here... %s", channel.getAsMention())
                                    .delay(3, TimeUnit.SECONDS)
                                    .flatMap(Message::delete)
                                    .queueAfter(3, TimeUnit.SECONDS);
                        }

                    })
                    .exceptionallyAsync(throwable -> {
                        createTicketProcess(context);
                        return null;
                    });
        });
    }

    private void createTicketProcess(ButtonContext ctx) {
        ctx.getHook().editOriginal("Creating your ticket now!")
                .flatMap(message -> createTicketChannel(ctx))
                .queueAfter(3, TimeUnit.SECONDS, result -> {
                    result.onSuccess(textChannel -> {
                        insertTicket(ctx.getDatabase(), textChannel, ctx.getMember());

                        ctx.getHook().editOriginalFormat("Your ticket was created here: %s", textChannel.getAsMention())
                                .delay(3, TimeUnit.SECONDS)
                                .flatMap(Message::delete)
                                .queueAfter(3, TimeUnit.SECONDS);

                        // TODO: Ping staff and add their own buttons to delete/close a ticket?

                        textChannel.sendMessage(ctx.getMember().getAsMention())
                                .setActionRow(Button.of(ButtonStyle.DANGER, "close_ticket", "Close Ticket", Emoji.fromUnicode("\uD83D\uDD12")))
                                .flatMap(message -> message.editMessageEmbeds(EmbedFactory.getNewTicket(ctx.getUser()).build()))
                                .queue();
                    });

                    result.onFailure(failure -> ctx.getHook().editOriginal("An error occurred creating your ticket.. try again later.")
                            .delay(8, TimeUnit.SECONDS)
                            .flatMap(Message::delete)
                            .queue());
                });
    }

    private void insertTicket(Database database, TextChannel channel, Member member) {
        final long guildId = channel.getGuild().getIdLong();
        final long channelId = channel.getIdLong();
        final long categoryId = 0L;
        final long creatorId = member.getIdLong();
        final String timestamp = OffsetDateTime.now().toString();

        database.getDSL().insertInto(GUILD_TICKETS)
                .values(guildId, channelId, categoryId, creatorId, timestamp, true, null)
                .executeAsync(database.getExecutor());
    }

    private RestAction<Result<TextChannel>> createTicketChannel(ButtonContext context) {
        final Member member = context.getMember();
        final String channelName = String.format("ticket-%s", member.getUser().getName());
        final List<Permission> permissionList = List.of(Permission.MESSAGE_WRITE, Permission.VIEW_CHANNEL);
        return context.getGuild().createTextChannel(channelName).
                addMemberPermissionOverride(member.getIdLong(), permissionList, null)
                .mapToResult();
    }

    @Override
    public String componentId() {
        return "create_ticket";
    }
}
