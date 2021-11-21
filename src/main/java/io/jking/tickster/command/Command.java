package io.jking.tickster.command;

import io.jking.tickster.utility.EmbedFactory;
import io.jking.tickster.utility.MiscUtil;
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

        if (!getSubcommands().isEmpty()) {
            return EmbedFactory.getDefault()
                    .setDescription(String.format("The information on this command is too long.\nPlease click %s to read about this command.",
                            MiscUtil.urlMarkdown("https://www.google.com", "here")));
        }

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
                embed.appendDescription(String.format("Name: %s\nDescription: %s\n\n",
                        optionData.getName(),
                        optionData.getDescription()
                ));
            }
            embed.appendDescription("```");
        }

        return embed;
    }

    @Override
    public String toString() {
        return "Command{" +
                "category=" + category +
                ", permission=" + permission +
                ", options=" + options +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
