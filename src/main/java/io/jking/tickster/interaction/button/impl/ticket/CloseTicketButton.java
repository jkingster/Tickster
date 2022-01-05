package io.jking.tickster.interaction.button.impl.ticket;

import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.core.Error;
import io.jking.tickster.interaction.core.impl.ButtonContext;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;

public class CloseTicketButton extends AbstractButton {

    public CloseTicketButton() {
        super("button:close_ticket:id:%s");
    }

    @Override
    public void onButtonPress(ButtonContext context) {
        final Member member = context.getMember();
        final TextChannel channel = context.getTextChannel();
        final String buttonId = String.format("button:open_ticket:id:%s", member.getIdLong());
        final ActionRow actionRow = ActionRow.of(
                Button.success(buttonId, "Open Ticket").withEmoji(EmbedUtil.UNLOCK_EMOJI)
        );

        context.deferEdit().flatMap(hook -> setPermissions(channel, member))
                .flatMap(ignored -> context.getHook().retrieveOriginal())
                .flatMap(message -> editComponents(message, actionRow))
                .queue(null, error -> context.replyErrorEphemeral(Error.CUSTOM, "Could not unlock ticket!"));
    }

    private PermissionOverrideAction setPermissions(TextChannel channel, Member member) {
        return channel.upsertPermissionOverride(member).setDeny(Permission.MESSAGE_SEND);
    }

    private MessageAction editComponents(Message message, ActionRow actionRow) {
        return message.editMessageComponents(actionRow);
    }
}
