package io.jking.tickster.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;

public enum Category {

    UTILITY(Permission.MESSAGE_WRITE, "Useful utility commands.", Emoji.fromUnicode("\uD83D\uDEE0")),
    TICKETS(Permission.MESSAGE_WRITE, "Commands related to tickets.", Emoji.fromUnicode("\uD83C\uDF9F")),
    SETUP(Permission.ADMINISTRATOR, "Configuration/Settings to help me function.", Emoji.fromUnicode("⚙")),
    INFO(Permission.MESSAGE_WRITE, "Informational commands.", Emoji.fromUnicode("\uD83D\uDCDD")),
    REPORTS(Permission.MESSAGE_WRITE, "Reporting commands.", Emoji.fromUnicode("\uD83D\uDCDB"));

    private final Permission permission;
    private final String description;
    private final Emoji emoji;

    Category(Permission permission, String description, Emoji emoji) {
        this.permission = permission;
        this.description = description;
        this.emoji = emoji;
    }

    public static String[] stringValues() {
        final String[] array = new String[values().length];
        for (int i = 0; i < array.length; i++) {
            array[i] = values()[i].getName();
        }
        return array;
    }

    public static Category fromName(String name) {
        for (Category category : values()) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public Permission getPermission() {
        return permission;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public boolean isPermitted(Member member) {
        return member.hasPermission(this.getPermission());
    }

}
