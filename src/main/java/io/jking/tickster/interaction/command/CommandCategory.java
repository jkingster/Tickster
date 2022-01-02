package io.jking.tickster.interaction.command;

import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.utility.EmojiUtil;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.Arrays;

public enum CommandCategory {

    BOT_OWNER(769456676016226314L, "Commands available to the bot owner.", EmojiUtil.BOT_OWNER),
    ADMIN(Permission.ADMINISTRATOR, "Administrative purposed commands.", EmojiUtil.ADMIN),
    TICKET_MANAGEMENT(Permission.MESSAGE_SEND, "Ticket management commands.", EmojiUtil.TICKET_MANAGEMENT),
    TICKET(Permission.MESSAGE_SEND, "Ticket processing related commands.", EmojiUtil.TICKET),
    UTILITY(Permission.MESSAGE_SEND, "Server utility based commands.", EmojiUtil.UTILITY),
    INFO(Permission.MESSAGE_SEND, "Informational commands.", EmojiUtil.INFO);

    public static CommandCategory[] fixedValues = values();

    private final String description;
    private final Emoji emoji;
    private long userId;
    private Permission permission;

    CommandCategory(long userId, String description, Emoji emoji) {
        this.userId = userId;
        this.description = description;
        this.emoji = emoji;
    }

    CommandCategory(Permission permission, String description, Emoji emoji) {
        this.permission = permission;
        this.description = description;
        this.emoji = emoji;
    }

    public static CommandCategory getCategoryByName(String name) {
        for (CommandCategory commandCategory : fixedValues) {
            if (commandCategory.name().equalsIgnoreCase(name))
                return commandCategory;
        }
        return null;
    }

    public static CommandCategory[] getCategories(GuildDataRecord record, Member member) {
        return Arrays.stream(fixedValues)
                .filter(category -> category.isPermitted(member))
                .filter(category -> {
                    if (category == TICKET_MANAGEMENT)
                        return category.isSupport(record, member)
                                || member.hasPermission(Permission.ADMINISTRATOR);
                    return true;
                })
                .toArray(CommandCategory[]::new);
    }

    public String getDescription() {
        return description;
    }

    public long getUserId() {
        return userId;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getPrettifiedName() {
        return name().charAt(0) + name().substring(1).toLowerCase().replaceAll("_", " ");
    }

    public boolean isSupport(GuildDataRecord record, Member member) {
        if (this != TICKET_MANAGEMENT)
            return false;

        if (record == null)
            return false;


        final long supportId = record.getSupportId();
        final Role role = member.getGuild().getRoleById(supportId);
        if (role == null)
            return false;

        return MiscUtil.hasRole(member, supportId);
    }

    public boolean isPermitted(Member member) {
        if (this == BOT_OWNER)
            return false;
        return member.hasPermission(this.getPermission());
    }
}
