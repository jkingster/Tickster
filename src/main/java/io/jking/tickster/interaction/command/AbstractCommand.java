package io.jking.tickster.interaction.command;

import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommand extends CommandDataImpl {

    private Permission permission = Permission.MESSAGE_SEND;
    private CommandCategory category = CommandCategory.UNKNOWN;
    private CommandFlag[] flags = new CommandFlag[]{};
    private List<String> usages = Collections.singletonList("No provided usage.");
    private boolean requiresSupportRole = false;

    public AbstractCommand(@NotNull String name, @NotNull String description) {
        super(name, description);
    }

    public AbstractCommand(@NotNull String name, @NotNull String description, Permission permission, CommandCategory category) {
        super(name, description);
        this.permission = permission;
        this.category = category;
    }

    public AbstractCommand(@NotNull String name, @NotNull String description, Permission permission, CommandCategory category, CommandFlag... flags) {
        super(name, description);
        this.permission = permission;
        this.category = category;
        this.flags = flags;
    }

    public AbstractCommand(@NotNull String name, @NotNull String description, Permission permission, CommandCategory category, List<String> usages, CommandFlag... flags) {
        super(name, description);
        this.permission = permission;
        this.category = category;
        this.flags = flags;
        this.usages = usages;
    }

    public AbstractCommand(String name, String description, CommandCategory category, CommandFlag... flags) {
        super(name, description);
        this.category = category;
        this.flags = flags;
    }


    public abstract void onSlashCommand(SlashSender sender);

    public Permission getPermission() {
        return permission;
    }

    public CommandCategory getCategory() {
        return category;
    }

    public CommandFlag[] getFlags() {
        return flags;
    }

    public List<String> getUsages() {
        return usages;
    }

    public boolean isSupportCommand() {
        return requiresSupportRole;
    }

    public void requiresSupportRole() {
        this.requiresSupportRole = true;
    }

    public EmbedBuilder getAsEmbed() {
        return EmbedUtil.getDefault()
                .setTitle("Command Information")
                .setAuthor(this.getName())
                .setFooter("Required Permission(s): " + getPermission())
                .setDescription(String.format(
                        """
                        **Description:** `%s`
                        **Category:**  %s `%s`
                        **Usage:** ```%s```
                     
                        """,
                        getDescription(),
                        getCategory().getEmoji(),
                        getCategory().getPrettifiedName(),
                        "No provided usage.")
                );
    }

    @Override
    public String toString() {
        return "AbstractCommand{" +
                "permission=" + permission +
                ", category=" + category +
                ", flags=" + Arrays.toString(flags) +
                ", usages=" + usages +
                ", requiresSupportRole=" + requiresSupportRole +
                ", options=" + options +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
