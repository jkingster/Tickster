package io.jking.tickster.command.impl.utility;


import io.jking.tickster.command.Category;
import io.jking.tickster.command.Command;
import io.jking.tickster.command.CommandContext;
import io.jking.tickster.command.CommandRegistry;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.utility.EmbedFactory;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.util.List;


public class HelpCommand extends Command {
    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        super("help", "Get information about a command.", Category.INFO);
        this.registry = registry;
        addOption(OptionType.STRING, "input", "Provide a category/command name.", false);
    }

    @Override
    public void onCommand(CommandContext ctx) {
        final String input = ctx.getOptionString("input");
        if (input == null) {
            sendCategoriesMenu(ctx, ctx.getMember());
            return;
        }

        if (MiscUtil.containsAnyOption(input, Category.stringValues())) {
            sendCategoryMenu(ctx, Category.fromName(input));
            return;
        }

        final Command command = registry.getCommand(input);
        if (command == null) {
            ctx.replyError(ErrorType.CUSTOM, "Could not process your input.");
            return;
        }

        sendCommandInfo(ctx, command);
    }

    private void sendCategoriesMenu(CommandContext context, Member member) {
        final SelectionMenu.Builder categoriesMenu = getCategoriesMenu(member);
        context.reply(EmbedFactory.getSelectionEmbed(context.getMember(), "Select a category!"))
                .addActionRow(categoriesMenu.build())
                .setEphemeral(true)
                .queue();
    }

    private void sendCategoryMenu(CommandContext context, Category category) {
        final SelectionMenu.Builder categoryMenu = getCategoryMenu(category, context.getMember());
        if (categoryMenu.isDisabled())
            return;

        context.reply(EmbedFactory.getSelectionEmbed(context.getMember(), "Select a command!"))
                .addActionRow(categoryMenu.build())
                .setEphemeral(true)
                .queue();
    }

    private void sendCommandInfo(CommandContext context, Command command) {
        context.reply(command.asEmbed())
                .setEphemeral(true)
                .queue();
    }

    private SelectionMenu.Builder getCategoriesMenu(Member member) {
        final SelectionMenu.Builder menu = SelectionMenu.create("menu_categories");
        final List<Category> permittedCategories = registry.getCategories(member);

        if (permittedCategories.isEmpty()) {
            menu.setDisabled(true);
            return menu;
        }

        permittedCategories.forEach(category -> menu.addOption(
                category.getName(),
                category.getName(),
                category.getDescription(),
                category.getEmoji()
        ));

        return menu;
    }

    private SelectionMenu.Builder getCategoryMenu(Category category, Member member) {
        final SelectionMenu.Builder menu = SelectionMenu.create("menu_category");
        final List<Command> permittedCommands = registry.getCommandsByCategory(category, member);

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


}
