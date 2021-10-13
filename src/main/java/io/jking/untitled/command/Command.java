package io.jking.untitled.command;

import io.jking.untitled.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenuInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Command extends CommandData {

    private Category category = Category.UNKNOWN;
    private Permission permission = Permission.MESSAGE_WRITE;

    private boolean isGlobal = false;

    private List<String> buttonKeys = new ArrayList<>();
    private List<String> selectionKeys = new ArrayList<>();

    public Command(@NotNull String name, @NotNull String description) {
        super(name, description);
    }

    public Command(String name, String description, Category category) {
        super(name, description);
        this.category = category;
    }

    public Command(@NotNull String name, @NotNull String description, Category category, Permission permission) {
        super(name, description);
        this.category = category;
        this.permission = permission;
    }

    public abstract void onCommand(CommandContext ctx);

    public void onButtonClick(ButtonInteraction interaction) {
    }

    public void onSelectionMenu(SelectionMenuInteraction interaction) {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public List<String> getButtonKeys() {
        return buttonKeys;
    }

    public void setButtonKeys(List<String> buttonKeys) {
        this.buttonKeys = buttonKeys;
    }

    public List<String> getSelectionKeys() {
        return selectionKeys;
    }

    public void setSelectionKeys(List<String> selectionKeys) {
        this.selectionKeys = selectionKeys;
    }

    public boolean hasButtons() {
        return !getButtonKeys().isEmpty();
    }

    public boolean hasSelections() {
        return !getSelectionKeys().isEmpty();
    }

    public String getPrettyName() {
        return Character.toUpperCase(this.getName().charAt(0)) + this.getName().substring(1);
    }

    public EmbedBuilder getAsEmbed() {
        final EmbedBuilder embed = EmbedUtil.getDefault()
                .setTitle(this.getPrettyName())
                .setAuthor("Command Information")
                .setFooter("Required Permission(s): " + this.getPermission());

        final StringBuilder description = embed.getDescriptionBuilder();
        description.append("**Category:** ").append(this.getCategory().getName()).append("\n")
                .append("**Description:**\n```").append(this.getDescription()).append("```\n");

        if (!this.getOptions().isEmpty()) {
            description.append("**Option(s):**\n```");
            for (OptionData option : this.getOptions()) {
                description.append("Name: ").append(option.getName()).append("\n")
                        .append("Desc.: ").append(option.getDescription()).append("\n")
                        .append("Choice(s): ").append(option.getChoices()).append("\n\n");
            }
            description.append("```");
        }

        return embed;
    }

    @Override
    public String toString() {
        return "Command{" +
                "options=" + options +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}
