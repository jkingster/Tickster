package io.jking.tickster.interaction.impl.selection;

import io.jking.tickster.command.Command;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.interaction.context.SelectionContext;
import io.jking.tickster.interaction.type.ISelection;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public class MenuCategorySelection implements ISelection {
    @Override
    public void onInteraction(SelectionContext context) {
        context.deferEdit().queue(deferred -> {
            final SelectOption selectedOption = context.getSelectedOption();
            if (selectedOption == null) {
                context.replyError(ErrorType.SELECTION);
                return;
            }


            final Command command = context.getRegistry().getCommand(selectedOption.getValue());
            if (command == null) {
                context.replyError(ErrorType.RETRIEVING, "command.");
                return;
            }

            sendCommandInfo(context, command);
        });
    }

    private void sendCommandInfo(SelectionContext context, Command command) {
        context.getHook().editOriginalEmbeds(command.asEmbed().build())
                .queue();
    }

    @Override
    public String componentId() {
        return "menu_category";
    }
}
