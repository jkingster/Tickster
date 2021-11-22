package io.jking.tickster.interaction.impl.selection;

import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.interaction.context.SelectionContext;
import io.jking.tickster.interaction.type.ISelection;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public class ViewTicketsSelection implements ISelection {


    // TODO: FINISH
    @Override
    public void onInteraction(SelectionContext context) {
        context.deferEdit().queue(deferred -> {
            final SelectOption option = context.getSelectedOption();
            if (option == null) {

                context.replyError(ErrorType.SELECTION);
                return;
            }

            final String[] split = option.getValue().split("_");
            final int ticketId = Integer.parseInt(split[0]);
            final long authorId = Long.parseLong(split[1]);

            context.getHook().editOriginalFormat("%s %s", ticketId, authorId).queue();
        });
    }

    @Override
    public String componentId() {
        return "view_tickets";
    }
}
