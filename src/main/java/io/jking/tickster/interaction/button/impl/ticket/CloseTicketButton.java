package io.jking.tickster.interaction.button.impl.ticket;

import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.core.Error;
import io.jking.tickster.interaction.core.impl.ButtonContext;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

public class CloseTicketButton extends AbstractButton {

    public CloseTicketButton() {
        super("button:close_ticket:id:%s");
    }

    @Override
    public void onButtonPress(ButtonContext context) {
        final Member member = context.getMember();
        context.deferEdit().flatMap(hook -> context.getTextChannel().upsertPermissionOverride(member).setAllow(Permission.MESSAGE_SEND))
                .queue(success -> context.getHook().retrieveOriginal().
                        flatMap(m -> m.editMessageComponents(
                                ActionRow.of(Button.success("button:open_ticket:id:%s", "Open Ticket").withEmoji(EmbedUtil.UNLOCK_EMOJI))
                        )).queue(), error -> context.replyErrorEphemeral(Error.CUSTOM, "Could not lock ticket!"));
    }
}
