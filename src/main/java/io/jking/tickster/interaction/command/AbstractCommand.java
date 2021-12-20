package io.jking.tickster.interaction.command;

import io.jking.tickster.interaction.core.impl.SlashContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommand extends CommandData {

    private final CommandCategory category;
    private Permission permission = Permission.MESSAGE_SEND;

    public AbstractCommand(@NotNull String name, @NotNull String description, CommandCategory category) {
        super(name, description);
        this.category = category;
    }

    public AbstractCommand(@NotNull String name, @NotNull String description, CommandCategory category, Permission permission) {
        super(name, description);
        this.category = category;
        this.permission = permission;
    }

    public abstract void onSlashCommand(SlashContext context);

    public CommandCategory getCategory() {
        return category;
    }

    public Permission getPermission() {
        return permission;
    }
}
