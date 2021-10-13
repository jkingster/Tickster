package io.jking.untitled.command.impl.utility;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import io.jking.untitled.command.CommandRegistry;
import io.jking.untitled.utility.EmbedUtil;
import io.jking.untitled.utility.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenuInteraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HelpCommand extends Command {


    final EmbedBuilder DEFAULT = EmbedUtil.getDefault()
            .setDescription("Please select **one** of the categories below to see more info.")
            .setFooter("Please note, if you do not see any commands -- you have access to none of them.");

    private final Emoji TRASH_EMOJI = Emoji.fromUnicode("\uD83D\uDDD1");

    private final String CATEGORY_ID = "menu:categories";
    private final String BUTTON_ID = "button:trash";
    private final String COMMANDS_ID = "menu:commands";

    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        super("help", "A command that gives information about commands. Leave blank for category selection.", Category.UTILITY);
        this.registry = registry;

        addOption(OptionType.STRING, "input", "The category or specific command name.", false);

        setButtonKeys(List.of("button:trash"));
        setSelectionKeys(List.of(CATEGORY_ID, COMMANDS_ID));
    }

    @Override
    public void onCommand(CommandContext ctx) {
        final Member member = ctx.getMember();
        final String input = ctx.getStringOption("input");
        if (input == null) {
            sendCategories(ctx, member);
            return;
        }

        final String[] categories = getCategories();
        if (MiscUtil.isAnyChoice(input, categories)) {
            final Category category = Category.getCategoryByName(input);
            sendCategory(ctx, category);
            return;
        }

        final Command command = registry.getCommand(input.toLowerCase());
        if (command == null) {
            ctx.reply("An error occurred.. please try again.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        sendCommand(ctx, command);
    }

    @Override
    public void onButtonClick(ButtonInteraction interaction) {
        final String componentId = interaction.getComponentId();
        if (componentId.equalsIgnoreCase(BUTTON_ID))
            interaction.deferEdit()
                    .flatMap(InteractionHook::deleteOriginal)
                    .queue();
    }

    @Override
    public void onSelectionMenu(SelectionMenuInteraction interaction) {
        final String componentId = interaction.getComponentId();

        final List<SelectOption> selectOptions = interaction.getSelectedOptions();
        if (selectOptions == null || selectOptions.isEmpty()) {
            interaction.reply("An error occurred with retrieving the option you selected.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        final SelectOption option = selectOptions.get(0);
        if (option == null) {
            interaction.reply("Something went wrong with the selection you picked.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        if (componentId.equalsIgnoreCase(CATEGORY_ID)) {
            final Category category = Category.getCategoryByKey(option.getValue());
            if (category == null) {
                interaction.reply("An error occurred retrieving the category.")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            final SelectionMenu.Builder menu = getCommandsMenu(interaction.getMember(), category);
            if (menu == null) {
                interaction.reply("An error occurred building the selection menu, or there are no commands in that category!")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            interaction.editMessageEmbeds(DEFAULT.build())
                    .setActionRows(
                            ActionRow.of(menu.build()),
                            ActionRow.of(Button.secondary("button:trash", TRASH_EMOJI))
                    ).queue();

            final EmbedBuilder embed = DEFAULT.setDescription("Please select **one** of the commands below to see more info.");

            interaction.editSelectionMenu(menu.build())
                    .flatMap(__ -> interaction.getHook().editOriginalEmbeds(embed.build()))
                    .queue();

        } else if (componentId.equalsIgnoreCase(COMMANDS_ID)) {
            final Command command = registry.getCommand(option.getValue());
            if (command == null)
                return;

            final EmbedBuilder commandEmbed = command.getAsEmbed();
            interaction.editSelectionMenu(null)
                    .flatMap(__ -> interaction.getHook().editOriginalEmbeds(commandEmbed.build()))
                    .queue();
        }
    }

    private void sendCategories(CommandContext ctx, Member member) {
        final SelectionMenu.Builder menu = getCategoriesMenu(member);
        if (menu == null) {
            ctx.reply("It seems you have access to no categories to view.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        final EmbedBuilder embedBuilder = EmbedUtil.getDefault()
                .setDescription("Please select **one** of the categories below to see a list of commands.")
                .setFooter("Please note, if you do not see a category -- you do not have access to it.");

        ctx.reply(embedBuilder.build()).addActionRows(
                ActionRow.of(menu.build()),
                ActionRow.of(Button.secondary(BUTTON_ID, TRASH_EMOJI))
        ).queue();
    }

    private void sendCategory(CommandContext ctx, Category category) {
        final SelectionMenu.Builder menu = getCommandsMenu(ctx.getMember(), category);
        if (menu == null) {
            ctx.reply("It seems you have access to no commands in that category.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        final EmbedBuilder embedBuilder = EmbedUtil.getDefault()
                .setDescription("Please select **one** of the commands below to see more info.")
                .setFooter("Please note, if you do not see any commands -- you have access to none of them.");

        ctx.reply(embedBuilder.build()).addActionRows(
                ActionRow.of(menu.build()),
                ActionRow.of(Button.secondary(BUTTON_ID, TRASH_EMOJI))
        ).queue();
    }

    private void sendCommand(CommandContext ctx, Command command) {
        final EmbedBuilder commandEmbed = command.getAsEmbed();

        ctx.reply(commandEmbed.build())
                .addActionRow(Button.primary("button:trash", TRASH_EMOJI))
                .delay(30, TimeUnit.SECONDS)
                .flatMap(InteractionHook::deleteOriginal)
                .queue();
    }

    private SelectionMenu.Builder getCategoriesMenu(Member member) {
        final List<Category> permittedCategories = getCategories(member);
        if (permittedCategories.isEmpty())
            return null;

        final SelectionMenu.Builder menu = SelectionMenu.create(CATEGORY_ID);
        permittedCategories.forEach(category -> menu.addOption(category.getName(), category.getCategoryKey()));

        return menu;
    }

    public SelectionMenu.Builder getCommandsMenu(Member member, Category category) {
        final List<Command> permittedCommands = registry.getCommandsByCategory(member, category);
        if (permittedCommands.isEmpty())
            return null;

        final SelectionMenu.Builder menu = SelectionMenu.create(COMMANDS_ID);
        permittedCommands.forEach(command -> menu.addOption(command.getPrettyName(), command.getName()));

        return menu;
    }

    private List<Category> getCategories(Member member) {
        if (member == null)
            return Collections.emptyList();

        final List<Category> categories = new ArrayList<>();
        for (Category category : Category.values())
            if (category.isPermitted(member) && category != Category.UNKNOWN)
                categories.add(category);

        return categories;
    }

    private String[] getCategories() {
        final Category[] categories = Category.values();
        final String[] array = new String[categories.length];

        for (int i = 0; i < array.length; i++) {
            array[i] = categories[i].getName();
        }

        return array;
    }
}
