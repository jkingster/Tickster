package io.jking.tickster.interaction.impl.slash.object;

import io.jking.tickster.interaction.context.CommandContext;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Command extends CommandData {

    private final Category category;
    private final Permission permission;

    public Command(@NotNull String name, @NotNull String description, Category category) {
        super(name, description);
        this.category = category;
        this.permission = Permission.MESSAGE_WRITE;
    }

    public Command(@NotNull String name, @NotNull String description, Category category, Permission permission) {
        super(name, description);
        this.category = category;
        this.permission = permission;
    }

    public abstract void onCommand(CommandContext ctx, CommandError err);

    public Category getCategory() {
        return category;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getPrettyName() {
        return Character.toUpperCase(getName().charAt(0)) + getName().substring(1).toLowerCase();
    }

    public EmbedBuilder asEmbed() {
        final EmbedBuilder embed = EmbedFactory.getDefault()
                .setTitle(getPrettyName() + " command.")
                .setAuthor("Command Information")
                .setFooter("Permission(s): " + getPermission().getName());

        embed.appendDescription(String.format("**Category:** %s\n", getCategory().getName()));
        embed.appendDescription(String.format("**Description:** ```%s```\n", getDescription()));

        final List<OptionData> options = getOptions();
        if (!options.isEmpty()) {
            embed.appendDescription("**Possible Option(s):**\n```");
            for (OptionData optionData : options) {
                embed.appendDescription(String.format("Name: %s\nDesc.: %s\nChoice(s): %s",
                        optionData.getName(),
                        optionData.getDescription(),
                        optionData.getChoices().isEmpty() ? "No choices." : optionData.getChoices())
                );
            }
            embed.appendDescription("```");
        }

        return embed;
    }
}
