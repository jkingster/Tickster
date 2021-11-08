package io.jking.tickster.objects.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;



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


}
