package io.jking.tickster.interaction.impl.slash.impl.utility;


import io.jking.tickster.interaction.context.CommandContext;
import io.jking.tickster.interaction.impl.slash.object.Category;
import io.jking.tickster.interaction.impl.slash.object.Command;
import io.jking.tickster.interaction.impl.slash.object.CommandError;
import io.jking.tickster.interaction.impl.slash.object.CommandRegistry;
import io.jking.tickster.interaction.impl.slash.object.type.ErrorType;
import io.jking.tickster.utility.EmbedFactory;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HelpCommand extends Command {
    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        super("help", "Get information about a command.", Category.INFO);
        this.registry = registry;
        addOption(OptionType.STRING, "input", "Provide a category/command name.", false);
    }

    @Override
    public void onCommand(CommandContext ctx, CommandError err) {
        final String input = ctx.getOptionString("input");
        if (input == null) {
            sendCategoriesMenu(ctx, ctx.getMember());
            return;
        }

        if (MiscUtil.containsAnyOption(input, Category.stringValues())) {
            sendCategoryMenu(ctx, Category.getCategory(input));
            return;
        }

        final Command command = registry.getCommand(input);
        if (command == null) {
            err.reply(ErrorType.CUSTOM, "Could not process your input.");
            return;
        }

        sendCommandInfo(ctx, command);

    }

    private void sendCategoriesMenu(CommandContext context, Member member) {
        final SelectionMenu categoriesMenu = getCategoriesMenu(member);
        if (categoriesMenu.isDisabled()) {
            context.reply("Your categories selection menu is disabled because you have no access to any of the commands.")
                    .delay(8, TimeUnit.SECONDS)
                    .flatMap(InteractionHook::deleteOriginal)
                    .queue();
            return;
        }

        context.reply(getSelectionEmbed(context.getMember()))
                .addActionRows(
                        ActionRow.of(categoriesMenu),
                        ActionRow.of(Button.primary("button_trash", Emoji.fromUnicode("\uD83D\uDEAE"))
                        ))
                .queue();
    }

    private void sendCategoryMenu(CommandContext context, Category category) {
        final SelectionMenu categoryMenu = getCategoryMenu(category, context.getMember());
        if (categoryMenu.isDisabled()) {
            context.reply("Your command selection menu is disabled because you have no access to any of the commands.")
                    .delay(8, TimeUnit.SECONDS)
                    .flatMap(InteractionHook::deleteOriginal)
                    .queue();
            return;
        }

        context.reply(getSelectionEmbed(context.getMember()))
                .addActionRows(
                        ActionRow.of(categoryMenu),
                        ActionRow.of(Button.primary("button_trash", Emoji.fromUnicode("\uD83D\uDEAE")))
                ).queue();
    }

    private void sendCommandInfo(CommandContext context, Command command) {
        context.reply(command.asEmbed())
                .setEphemeral(true)
                .queue();
    }

    private SelectionMenu getCategoriesMenu(Member member) {
        final SelectionMenu.Builder menu = SelectionMenu.create("menu_categories");
        final List<Category> permittedCategories = registry.getCategories(member);

        if (permittedCategories.isEmpty()) {
            menu.setDisabled(true);
            return menu.build();
        }

        permittedCategories.forEach(category -> menu.addOption(
                category.getName(),
                "category_" + category.name().toLowerCase(),
                category.getDescription(),
                category.getEmoji()
        ));

        return menu.build();
    }

    private SelectionMenu getCategoryMenu(Category category, Member member) {
        final SelectionMenu.Builder menu = SelectionMenu.create("menu_category");
        final List<Command> permittedCommands = registry.getCommandsByCategory(category, member);

        if (permittedCommands.isEmpty()) {
            menu.setDisabled(true);
            return menu.build();
        }

        permittedCommands.forEach(command -> menu.addOption(
                command.getPrettyName(),
                "command_" + command.getName().toLowerCase(),
                command.getDescription())
        );

        return menu.build();
    }

    private EmbedBuilder getSelectionEmbed(Member member) {
        return EmbedFactory.getDefault()
                .setAuthor("Make a selection!", null, member.getUser().getEffectiveAvatarUrl())
                .setDescription("Please select an option to continue, or run the command again with an input token!");
    }
}
