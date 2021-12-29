package io.jking.tickster.interaction.command;

import io.jking.tickster.interaction.core.impl.SlashSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand  {

    private final String name;
    private final String description;
    private final CommandCategory category;
    private final SlashCommandData data;
    private Permission permission = Permission.MESSAGE_SEND;

    public AbstractCommand(@NotNull String name, @NotNull String description, CommandCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.data = Commands.slash(name, description);
    }

    public AbstractCommand(@NotNull String name, @NotNull String description, CommandCategory category, Permission permission) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.permission = permission;
        this.data = Commands.slash(name, description);
    }

    public abstract void onSlashCommand(SlashSender context);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CommandCategory getCategory() {
        return category;
    }

    public Permission getPermission() {
        return permission;
    }

    public SlashCommandData getData() {
        return data;
    }

    public void addOptions(OptionData... options) {
        this.data.addOptions(options);
    }

    public void addSubCommands(SubcommandData... subCommands) {
        this.data.addSubcommands(subCommands);
    }

    public void addOption(OptionType optionType, String name, String desc, boolean required) {
        this.data.addOption(optionType, name, desc, required);
    }

    public void addOption(OptionType optionType, String nane, String desc) {
        addOption(optionType, name, desc, false);
    }
}
