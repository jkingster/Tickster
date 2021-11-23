package io.jking.tickster.interaction.impl.button.ticket;

import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class CreateTicketButton implements IButton {

    private static final List<Permission> ALLOWED_PERMS = List.of(Permission.MESSAGE_WRITE, Permission.VIEW_CHANNEL);

    @Override
    public void onInteraction(ButtonContext context) {
        final long guildId = context.getGuild().getIdLong();
        final long memberId = context.getMember().getIdLong();

        context.deferEdit().queue(deferred -> {
            context.getDatabase().getDSL().selectFrom(GUILD_TICKETS)
                    .where(GUILD_TICKETS.GUILD_ID.eq(guildId))
                    .and(GUILD_TICKETS.CREATOR_ID.eq(memberId))
                    .and(GUILD_TICKETS.OPEN.eq(true))
                    .fetchAsync()
                    .thenAcceptAsync(result -> {
                        if (result.isEmpty()) {
                            startTicketChannelProcess(context);
                            return;
                        }

                        context.getHook().sendMessageEmbeds(EmbedFactory.getError(ErrorType.CUSTOM, "You have an open ticket already!").build())
                                .setEphemeral(true)
                                .queue();

                    })
                    .exceptionallyAsync(throwable -> {
                        startTicketChannelProcess(context);
                        return null;
                    });
        });
    }

    private void startTicketChannelProcess(ButtonContext context) {
        final long guildId = context.getGuild().getIdLong();
        context.getGuildCache().retrieve(guildId, record -> {

            final long managerId = record.getTicketManager();
            final Role ticketManager = context.getGuild().getRoleById(managerId);

            if (ticketManager == null) {
                context.replyError(ErrorType.CUSTOM, "Ticket Manager role is not set!");
                return;
            }

            final long categoryId = record.getTicketCategory();
            final Category category = context.getGuild().getCategoryById(categoryId);

            createTicketChannel(context, category, ticketManager).queue(success -> {
                sendTicketInfo(success, ticketManager, context);
                context.getHook().sendMessageFormat("**%s:** Your ticket was created here. %s.", context.getMember().getUser().getName(),
                        success.getAsMention())
                        .setEphemeral(true)
                        .queue();

                handleTicketInsertion(context, success, category);
            }, error -> context.replyError(ErrorType.CUSTOM, "An error occurred creating the ticket channel!"));

        }, error -> context.replyError(ErrorType.CUSTOM, "Could not create ticket channel!"));
    }

    private ChannelAction<TextChannel> createTicketChannel(ButtonContext context, Category category, Role ticketManager) {
        final long memberId = context.getMember().getIdLong();
        final String channelName = String.format("ticket-%s", context.getMember().getEffectiveName());
        final ChannelAction<TextChannel> action = context.getGuild().createTextChannel(channelName)
                .addMemberPermissionOverride(memberId, ALLOWED_PERMS, null)
                .addRolePermissionOverride(ticketManager.getIdLong(), ALLOWED_PERMS, null);

        if (category != null) {
            return action.setParent(category);
        }

        return action;
    }

    private void sendTicketInfo(TextChannel channel, Role ticketManager, ButtonContext context) {
        channel.sendMessageFormat("%s | %s", context.getMember().getAsMention(), ticketManager.getAsMention())
                .setEmbeds(EmbedFactory.getNewTicket(context.getMember().getUser()).build())
                .setActionRow(Button.of(ButtonStyle.DANGER, "close_ticket", "Close Ticket", Emoji.fromUnicode("\uD83D\uDD12")))
                .queue();
    }

    private void handleTicketInsertion(ButtonContext context, TextChannel channel, Category category) {
        insertTicket(context, channel, category).exceptionallyAsync(throwable -> {
            channel.delete().queue(success -> context.replyError(ErrorType.UNKNOWN));
            return null;
        });
    }

    private CompletionStage<Integer> insertTicket(ButtonContext context, TextChannel channel, Category category) {
        final long guildId = channel.getGuild().getIdLong();
        final long channelId = channel.getIdLong();
        final long categoryId = category == null ? 0L : category.getIdLong();
        final long creatorId = context.getMember().getIdLong();

        return context.getDatabase().getDSL().insertInto(GUILD_TICKETS)
                .values(guildId, channelId, categoryId, creatorId, LocalDateTime.now(), true, null)
                .executeAsync(context.getDatabase().getExecutor());
    }

    @Override
    public String componentId() {
        return "create_ticket";
    }
}
