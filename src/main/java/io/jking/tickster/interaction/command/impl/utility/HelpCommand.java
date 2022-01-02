package io.jking.tickster.interaction.command.impl.utility;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.interaction.core.responses.Error;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.util.List;

public class HelpCommand extends AbstractCommand {

    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        super("help", "Displays permitted commands.", CommandCategory.UTILITY);
        addOption(OptionType.STRING, "input", "Command name/category.", false);
        this.registry = registry;
    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final String input = sender.getStringOption("input");
        if (input == null) {
            sendCategoriesMenu(sender, CommandCategory.getCategories(sender.getGuildRecord(), sender.getMember()));
            return;
        }

        final CommandCategory category = CommandCategory.getCategoryByName(input);
        if (category != null) {
            sendCategoryMenu(sender, sender.getMember(), category);
            return;
        }

        final AbstractCommand command = registry.get(input);
        if (command == null) {
            sender.replyErrorEphemeral(
                    Error.CUSTOM,
                    "You provided an invalid command name."
            ).queue();
            return;
        }

        sendCommandInfo(sender, command);
    }

    private void sendCategoriesMenu(SlashSender sender, CommandCategory[] categories) {
        if (categories.length == 0) {
            sender.replyErrorEphemeral(
                    Error.CUSTOM,
                    "You are not permitted to view any command categories."
            ).queue();
            return;
        }

        final SelectMenu.Builder menu = getCategoriesMenu(categories);
        sender.replyEphemeral("Select any category to get started.")
                .addActionRow(menu.build())
                .queue();
    }

    private SelectMenu.Builder getCategoriesMenu(CommandCategory[] categories) {
        final SelectMenu.Builder builder = SelectMenu.create("menu:help_categories");
        builder.setPlaceholder("Pick a command category to get started!");
        for (CommandCategory category : categories) {
            if (category == null)
                continue;

            builder.addOption(
                    category.getPrettifiedName(),
                    String.format("menu:value:%s", category.name().toLowerCase()),
                    category.getDescription(),
                    category.getEmoji()
            );
        }
        return builder;
    }

    private void sendCategoryMenu(SlashSender sender, Member member, CommandCategory category) {
        if (!category.isPermitted(member)) {
            sender.replyErrorEphemeral(
                    Error.CUSTOM,
                    "You are not permitted to view that category."
            ).queue();
            return;
        }

        final List<AbstractCommand> commandList = registry.getCommands(member, category);
        if (commandList.isEmpty()) {
            sender.replyErrorEphemeral(
                    Error.CUSTOM,
                    "The returning command list was empty, are you missing permissions?"
            ).queue();
            return;
        }

        final SelectMenu.Builder menu = getCategoryMenu(commandList);
        sender.replyEphemeralFormat(
                "%s %s - Click any command to view its information.",
                category.getEmoji(),
                category.getPrettifiedName()
        ).addActionRow(menu.build()).queue();
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

    private void sendCommandInfo(SlashSender sender, AbstractCommand command) {
        final Member member = sender.getMember();
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            if (!command.getCategory().isSupport(sender.getGuildRecord(), sender.getMember())) {
                sender.replyError(
                        Error.CUSTOM,
                        "You do not have the support role to manage tickets."
                ).queue();
                return;
            }

            if (!member.hasPermission(command.getPermission())) {
                sender.replyErrorEphemeral(
                        Error.PERMISSION,
                        member.getUser().getAsTag(),
                        command.getPermission()
                ).queue();
                return;
            }
        }

        final EmbedBuilder commandEmbed = command.getAsEmbed();
        sender.replyEphemeral(commandEmbed).queue();
    }

}
