package io.jking.tickster.interaction.impl.selection;

import io.jking.tickster.command.Category;
import io.jking.tickster.command.Command;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.interaction.context.SelectionContext;
import io.jking.tickster.interaction.type.ISelection;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.util.List;

public class MenuCategoriesSelection implements ISelection {

    @Override
    public void onInteraction(SelectionContext context) {
        context.deferEdit().queue(deferred -> {
            final SelectOption selectedOption = context.getSelectedOption();
            if (selectedOption == null) {
                context.replyError(ErrorType.SELECTION);
                return;
            }

            final Category category = Category.fromName(selectedOption.getValue());
            if (category == null) {
                context.replyError(ErrorType.RETRIEVING, "category.");
                return;
            }

            sendCategoryMenu(context, category);
        });
    }

    private void sendCategoryMenu(SelectionContext context, Category category) {
        final SelectionMenu.Builder categoryMenu = getCategoryMenu(context, category);
        if (categoryMenu.isDisabled())
            return;

        context.getHook().editOriginalEmbeds(EmbedFactory.getSelectionEmbed(context.getMember(), "Select a command!").build())
                .setActionRow(categoryMenu.build())
                .queue();

    }

    private SelectionMenu.Builder getCategoryMenu(SelectionContext context, Category category) {
        final SelectionMenu.Builder menu = SelectionMenu.create("menu_category");
        final List<Command> permittedCommands = context.getRegistry().getCommandsByCategory(category, context.getMember());

        if (permittedCommands.isEmpty()) {
            menu.setDisabled(true);
            return menu;
        }

        for (Command command : permittedCommands) {
            menu.addOption(
                    command.getPrettyName(),
                    command.getName().toLowerCase(),
                    command.getDescription()
            );
        }

        return menu;
    }

    @Override
    public String componentId() {
        return "menu_categories";
    }
}
