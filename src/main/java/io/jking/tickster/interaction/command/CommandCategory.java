package io.jking.tickster.interaction.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public enum CommandCategory {

    BOT_OWNER           (769456676016226314L, "Commands available to the bot owner."),
    ADMIN               (Permission.ADMINISTRATOR, "Administrative purposed commands."),
    TICKET_MANAGEMENT   (Permission.MESSAGE_SEND, "Ticket management commands."),
    TICKET              (Permission.MESSAGE_SEND, "Ticket processing related commands."),
    UTILITY             (Permission.MESSAGE_SEND, "Server utility based commands."),
    INFO                (Permission.MESSAGE_SEND, "Informational commands.");

    public static CommandCategory[] categories = CommandCategory.values();
    private final String description;
    private long userId;
    private Permission permission;

    CommandCategory(long userId, String description) {
        this.userId = userId;
        this.description = description;
    }

    CommandCategory(Permission permission, String description) {
        this.permission = permission;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public long getUserId() {
        return userId;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getPrettifiedName() {
        if (name().contains("_")) {
            final String name = name().replaceAll("_", " ");
            return name.charAt(0) + name().substring(1).toLowerCase();
        }

        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public CommandCategory getCategoryByName(String name) {
        for (CommandCategory commandCategory : categories) {
            if (commandCategory.name().equalsIgnoreCase(name))
                return commandCategory;
        }
        return null;
    }

    public boolean isPermitted(Member member) {
        return member.hasPermission(this.getPermission());
    }

}
