package io.jking.tickster.interaction.command.impl.utility;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandFlag;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.utility.EmbedUtil;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand extends AbstractCommand {

    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        super(
                "help",
                "Displays a list of categories/commands.",
                Permission.MESSAGE_SEND,
                CommandCategory.UTILITY,
                CommandFlag.ofEphemeral()
        );
        this.registry = registry;

        addOption(OptionType.STRING, "input", "Command name/category.", false);
    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final Member member = sender.getMember();
        final String input = sender.getStringOption("input");
        if (input == null) {
            sendCategoriesMenu(sender, getCategories(sender.getGuildRecord(), member));
            return;
        }

        final CommandCategory category = CommandCategory.getCategoryByName(input);
        if (category != null) {
            sendCategoryMenu(sender, member, category);
            return;
        }

        final AbstractCommand command = registry.get(input);
        if (command == null) {
            sender.reply(
                    Error.CUSTOM,
                    "You provided an invalid command name."
            ).queue();
            return;
        }

        sendCommandInfo(sender, command);
    }

    private void sendCategoriesMenu(SlashSender sender, List<CommandCategory> categories) {
        if (categories.isEmpty()) {
            sender.reply(
                    Error.CUSTOM,
                    "You are not permitted to view any command categories."
            ).queue();
            return;
        }

        final SelectMenu.Builder menu = getCategoriesMenu(sender.getMember().getIdLong(), categories);
        sender.reply(EmbedUtil.getCategories(categories))
                .addActionRow(menu.build())
                .queue();
    }

    private SelectMenu.Builder getCategoriesMenu(long memberId, List<CommandCategory> categories) {
        final SelectMenu.Builder builder = SelectMenu.create("menu:help_categories");
        builder.setPlaceholder("Select a command.");
        for (CommandCategory category : categories) {
            if (category == null)
                continue;

            if (category == CommandCategory.DEVELOPER || category == CommandCategory.DISABLED) {
                if (!MiscUtil.isDeveloper(memberId))
                    continue;
            }

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
        final List<AbstractCommand> commandList = registry.getCommandsByCategory(category, member);
        if (commandList.isEmpty()) {
            sender.reply(
                    Error.CUSTOM,
                    "The returning command list was empty, are you missing permissions?"
            ).queue();
            return;
        }

        final SelectMenu.Builder menu = getCategoryMenu(commandList);

        sender.reply(EmbedUtil.getCommands(category, commandList))
                .addActionRow(menu.build()).queue();
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
        if (command.isSupportCommand()) {
            final GuildDataRecord record = sender.getGuildRecord();
            final long supportId = record.getSupportId();
            final Role role = sender.getGuild().getRoleById(supportId);
            if (role == null) {
                sender.reply(Error.CUSTOM, "Support role is not configured.").queue();
                return;
            }

            if (!MiscUtil.hasRole(sender.getMember(), supportId)) {
                sender.reply(Error.PERMISSION, member.getUser().getAsTag(), "Support Role").queue();
                return;
            }
        }

        if (!member.hasPermission(command.getPermission())) {
            sender.reply(Error.PERMISSION, member.getUser().getAsTag(), command.getPermission()).queue();
            return;
        }


        final EmbedBuilder commandEmbed = command.getAsEmbed();
        sender.reply(commandEmbed).queue();
    }

    private List<CommandCategory> getCategories(GuildDataRecord record, Member member) {
        return Arrays.stream(CommandCategory.VALUES)
                .filter(category -> category.isPermitted(member, record))
                .sorted(Comparator.comparing(CommandCategory::getPrettifiedName))
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

}
