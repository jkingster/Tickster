package io.jking.tickster.interaction.command;

import io.jking.tickster.utility.EmojiUtil;
import net.dv8tion.jda.api.entities.Emoji;

public enum CommandCategory {

    DISABLED("Disabled commands that are non-functional.", EmojiUtil.LOCK_EMOJI),
    DEVELOPER("Developer commands that aren't usable.", EmojiUtil.LOCK_EMOJI),
    ADMINISTRATOR("Administrative purposed commands.", EmojiUtil.ADMINISTRATOR),
    MODERATOR("Moderation based commands.", EmojiUtil.MODERATOR),
    TICKET_SUPPORT("Ticket management commands.", EmojiUtil.TICKET_SUPPORT),
    TICKET("Ticket processing related commands.", EmojiUtil.TICKET),
    UTILITY("Server utility based commands.", EmojiUtil.UTILITY),
    INFO("Informational commands.", EmojiUtil.INFO),
    UNKNOWN("Unknown category.", EmojiUtil.UNKNOWN);

    private static final CommandCategory[] VALUES = values();

    private final String description;
    private final Emoji emoji;

    CommandCategory(String description, Emoji emoji) {
        this.description = description;
        this.emoji = emoji;
    }

    public static CommandCategory getCategoryByName(String categoryName) {
        for (CommandCategory category : VALUES) {
            if (category.getPrettifiedName().equalsIgnoreCase(categoryName))
                return category;
        }
        return UNKNOWN;
    }

    public String getDescription() {
        return description;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public static CommandCategory[] getCategories() {
        return VALUES;
    }

    public String getPrettifiedName() {
        if (this == TICKET_SUPPORT)
            return "Ticket Support";

        return name().charAt(0) + name().substring(1).toLowerCase();
    }




}
