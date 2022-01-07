package io.jking.tickster.interaction.command;

import io.jking.tickster.interaction.core.InteractionSender;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.jooq.tables.GuildTickets;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.utility.EmojiUtil;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static final CommandCategory[] VALUES = values();
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

    public static String getCategoriesPrinted(List<CommandCategory> categories) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (CommandCategory category : categories) {
            stringBuilder
                    .append(category.getEmoji().getAsMention())
                    .append(" `")
                    .append(String.format("%s`", StringUtils.rightPad(category.getPrettifiedName(), getLongestCategory(), " ")))
                    .append("- `")
                    .append(category.getDescription())
                    .append("`")
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    private static int getLongestCategory() {
        int size = 0;
        for (CommandCategory category : VALUES) {
            final int length = category.getPrettifiedName().length();
            if (length > size)
                size = length;
        }
        return size;
    }

    public boolean isPermitted(Member member, GuildDataRecord record) {
        return switch (this) {
            case ADMINISTRATOR -> member.hasPermission(Permission.ADMINISTRATOR);
            case MODERATOR -> member.hasPermission(Permission.MODERATE_MEMBERS);
            case TICKET_SUPPORT -> MiscUtil.isSupport(record, member) || member.hasPermission(Permission.ADMINISTRATOR);
            case UNKNOWN, DEVELOPER, DISABLED -> false;
            default -> true;
        };
    }

    public String getDescription() {
        return description;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public String getPrettifiedName() {
        if (this == TICKET_SUPPORT)
            return "Ticket Support";

        return name().charAt(0) + name().substring(1).toLowerCase();
    }

}
