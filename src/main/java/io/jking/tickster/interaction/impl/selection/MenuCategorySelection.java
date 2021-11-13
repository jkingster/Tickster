package io.jking.tickster.interaction.impl.selection;

import io.jking.tickster.command.Command;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.interaction.context.SelectionContext;
import io.jking.tickster.interaction.type.ISelection;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public class MenuCategorySelection implements ISelection {
    @Override
    public void onInteraction(SelectionContext context) {
        context.deferEdit().queue(deferred -> {
            final SelectOption selectedOption = context.getSelectedOption();
            if (selectedOption == null) {
                context.getHook().editOriginalEmbeds(EmbedFactory.getError(ErrorType.CUSTOM, "An error occurred with your selection.").build())
                        .queue();
                return;
            }


            final Command command = context.getRegistry().getCommand(selectedOption.getValue());
            if (command == null) {
                context.getHook().editOriginalEmbeds(EmbedFactory.getError(ErrorType.CUSTOM, "An error occurred retrieving that command.").build())
                        .queue();
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
