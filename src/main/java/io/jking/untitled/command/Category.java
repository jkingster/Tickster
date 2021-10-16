package io.jking.untitled.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public enum Category {

    BOT_OWNER(),
    ADMIN(Permission.ADMINISTRATOR, "category:admin"),
    MODERATION(Permission.KICK_MEMBERS, "category:moderation"),
    MISC(Permission.MESSAGE_WRITE, "category:misc"),
    UNKNOWN(Permission.MESSAGE_WRITE, "category:unknown"),
    UTILITY(Permission.MESSAGE_WRITE, "category:utility"),
    INFO(Permission.MESSAGE_WRITE, "category:info");

    private Permission permission;
    private String categoryKey;

    Category() {
    }

    Category(Permission permission, String categoryKey) {
        this.permission = permission;
        this.categoryKey = categoryKey;
    }

    public static Category getCategoryByName(String name) {
        for (Category category : values())
            if (name.equalsIgnoreCase(category.getName()))
                return category;
        return null;
    }

    public static Category getCategoryByKey(String key) {
        for (Category category : values())
            if (key.equalsIgnoreCase(category.getCategoryKey()))
                return category;
        return null;
    }

    public boolean isPermitted(Member member) {
        if (member == null)
            return false;

        if (this == Category.BOT_OWNER)
            return false;

        return member.hasPermission(this.getPermission());
    }

    public Permission getPermission() {
        return permission;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
