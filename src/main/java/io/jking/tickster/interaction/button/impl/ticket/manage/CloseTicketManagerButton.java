package io.jking.tickster.interaction.button.impl.ticket.manage;

import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.core.impl.ButtonSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;

import java.time.Instant;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class CloseTicketManagerButton extends AbstractButton {

    public CloseTicketManagerButton() {
        super("button:manage:close_ticket");
    }

    @Override
    public void onButtonPress(ButtonSender sender) {
        final GuildTicketsRecord record = sender.getTicketRecord();
        if (record == null) {
            sender.replyErrorEphemeral(Error.CUSTOM, "This is not a valid ticket or an error occurred.").queue();
            return;
        }

        if (!record.getStatus()) {
            sender.replyErrorEphemeral(Error.CUSTOM, "This ticket is already closed...").queue();
            return;
        }

        final TextChannel channel = sender.getTextChannel();
        final long memberId = sender.getTicketRecord().getCreatorId();

        sender.deferReply().flatMap(__ -> sender.retrieveMember(memberId))
                .flatMap(member -> denyPermissions(channel, member))
                .queue(success -> {
                    final long channelId = channel.getIdLong();
                    sender.getTicketCache().update(channelId, GUILD_TICKETS.STATUS, false);

                    sender.getHook().editOriginalEmbeds(EmbedUtil.getDefault()
                            .setColor(EmbedUtil.SECONDARY)
                            .setDescription(String.format("**%s** closed this ticket. It cannot be reopened.", sender.getUser().getAsTag()))
                            .setTimestamp(Instant.now())
                            .build()
                    ).queue();
                });
    }

    private PermissionOverrideAction denyPermissions(TextChannel channel, Member member) {
        return channel.putPermissionOverride(member)
                .setAllow(Permission.VIEW_CHANNEL)
                .deny(Permission.MESSAGE_SEND);
    }
}
