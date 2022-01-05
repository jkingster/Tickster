package io.jking.tickster.interaction.select.impl;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.impl.SelectSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.select.AbstractSelect;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.List;

public class CategoriesSelect extends AbstractSelect {
    private final CommandRegistry registry;

    public CategoriesSelect(CommandRegistry registry) {
        super("menu:help_categories");
        this.registry = registry;
    }

    @Override
    public void onSelectPress(SelectSender sender) {
        final SelectOption selectOption = sender.getSelectedOption();
        if (selectOption == null) {
            sender.replyErrorEphemeral(
                    Error.UNKNOWN
            ).queue();
            return;
        }

        final String value = selectOption.getValue();
        final String categoryName = value.split(":")[2];
        if (categoryName == null || categoryName.isEmpty()) {
            sender.replyErrorEphemeral(
                    Error.CUSTOM, "Unknown parsing error."
            ).queue();
            return;
        }

        final CommandCategory category = CommandCategory.getCategoryByName(categoryName);
        if (category == null) {
            sender.replyErrorEphemeral(
                    Error.CUSTOM,
                    "Could not find that category!"
            ).queue();
            return;
        }

        sendCategoryMenu(sender, sender.getMember(), category);
    }

    private void sendCategoryMenu(SelectSender sender, Member member, CommandCategory category) {
        final List<AbstractCommand> commandList = registry.getCommandsByCategory(category, member);
        if (commandList.isEmpty()) {
            sender.replyErrorEphemeral(
                    Error.CUSTOM,
                    "The returning command list was empty, are you missing permissions?"
            ).queue();
            return;
        }

        final SelectMenu.Builder menu = getCategoryMenu(commandList);
        sender.deferEdit().flatMap(
                hook -> hook.editOriginalFormat("%s %s - Click any command to view its information.",
                        category.getEmoji(), category.getPrettifiedName()).setActionRow(menu.build())
        ).queue();
    }

    private SelectMenu.Builder getCategoryMenu(List<AbstractCommand> commandList) {
        final SelectMenu.Builder menu = SelectMenu.create("menu:help_category");
        for (AbstractCommand command : commandList) {
            menu.addOption(
                    command.getName(),
                    String.format("command:%s", command.getName()),
                    command.getDescription()
            );
        }
        return menu;
    }
}
