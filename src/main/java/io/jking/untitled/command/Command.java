package io.jking.untitled.command;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public abstract class Command extends CommandData {

    private Category category = Category.UNKNOWN;

    public Command(@NotNull String name, @NotNull String description) {
        super(name, description);
    }

    public Command(String name, String description, Category category) {
        super(name, description);
        this.category = category;
    }

    public abstract void onCommand(CommandContext ctx);

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
