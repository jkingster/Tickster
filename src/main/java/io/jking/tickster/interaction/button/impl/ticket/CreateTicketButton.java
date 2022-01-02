package io.jking.tickster.interaction.button.impl.ticket;

import io.jking.tickster.cache.impl.TicketCache;
import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.core.impl.ButtonSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import io.jking.tickster.utility.EmbedUtil;
import io.jking.tickster.utility.EmojiUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.time.LocalDateTime;
import java.util.List;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class CreateTicketButton extends AbstractButton {

    private final List<Permission> permissionList = List.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);

    public CreateTicketButton() {
        super("button:create_ticket");
    }

    @Override
    public void onButtonPress(ButtonSender sender) {
        final Member self = sender.getSelfMember();
        if (!self.hasPermission(Permission.MANAGE_CHANNEL)) {
            sender.replyErrorEphemeral(
                    Error.PERMISSION,
                    self.getUser().getAsTag(),
                    Permission.MANAGE_CHANNEL
            );
            return;
        }

        final Member member = sender.getMember();

        if (hasOpenTicket(sender, member)) {
            sender.replyErrorEphemeral(Error.CUSTOM, "You already have a ticket open!").queue();
            return;
        }

        final GuildDataRecord record = sender.getGuildRecord();
        final long categoryId = record.getCategoryId();
        final Category category = sender.getGuild().getCategoryById(categoryId);

        // Callback Hell (!!!!)
        createTicketChannel(member, category).queue(created -> {
            insertTicket(sender.getCache().getTicketCache(), member, created);
            setupSupportRole(created, record);
            setupMemberPermissions(created, member).queue(success -> {
                final String buttonId = String.format("button:close_ticket:id:%s", member.getIdLong());
                created.sendMessageEmbeds(EmbedUtil.getNewTicket(member).build())
                        .content(member.getAsMention())
                        .setActionRow(Button.danger(buttonId, "Close Ticket").withEmoji(EmojiUtil.LOCK_EMOJI))
                        .queue();

                sender.replySuccessEphemeral(Success.CREATED, created.getAsMention()).queue();
            }, error -> created.delete().flatMap(deleted -> sender.replyErrorEphemeral(Error.UNKNOWN)).queue());
        }, error -> sender.replyErrorEphemeral(Error.CUSTOM, "Could not create your ticket, sorry!"));
    }

    private boolean hasOpenTicket(ButtonSender sender, Member member) {
        final long memberId = member.getIdLong();
        return sender.getDatabase().
                getContext().
                fetchExists(
                        GUILD_TICKETS,
                        GUILD_TICKETS.CREATOR_ID.eq(memberId)
                        .and(GUILD_TICKETS.STATUS.eq(true))
                );
    }

    private ChannelAction<TextChannel> createTicketChannel(Member member, Category category) {
        final Guild guild = member.getGuild();
        final String channelName = String.format("ticket-%s", member.getUser().getName());
        return category == null
                ? guild.createTextChannel(channelName)
                : guild.createTextChannel(channelName).setParent(category);
    }

    private void setupSupportRole(TextChannel channel, GuildDataRecord record) {
        final Guild guild = channel.getGuild();
        final long supportId = record.getSupportId();
        final Role supportRole = guild.getRoleById(supportId);

        if (supportRole == null)
            return;

        channel.putPermissionOverride(supportRole).setAllow(permissionList)
                .flatMap(__ -> channel.sendMessage(supportRole.getAsMention()))
                .queue();
    }

    private RestAction<Void> setupMemberPermissions(TextChannel channel, Member member) {
        final Role publicRole = channel.getGuild().getPublicRole();
        return channel.putPermissionOverride(publicRole).setDeny(Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL)
                .and(channel.putPermissionOverride(member).setAllow(permissionList));
    }

    private void insertTicket(TicketCache ticketCache, Member member, TextChannel created) {
        final long guildId = member.getGuild().getIdLong();
        final long channelId = created.getIdLong();
        final long creatorId = member.getIdLong();

        final GuildTicketsRecord record = GUILD_TICKETS.newRecord()
                .setGuildId(guildId)
                .setCreatorId(creatorId)
                .setChannelId(channelId)
                .setTicketTimestamp(LocalDateTime.now())
                .setStatus(true);

        ticketCache.insert(record);
    }
}

