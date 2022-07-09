/*
 * This file is generated by jOOQ.
 */
package io.jking.jooq.tables.pojos;


import java.io.Serializable;
import java.util.Arrays;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GuildTicketBlacklist implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long   guildId;
    private final Long[] blacklistedUserIds;

    public GuildTicketBlacklist(GuildTicketBlacklist value) {
        this.guildId = value.guildId;
        this.blacklistedUserIds = value.blacklistedUserIds;
    }

    public GuildTicketBlacklist(
        Long   guildId,
        Long[] blacklistedUserIds
    ) {
        this.guildId = guildId;
        this.blacklistedUserIds = blacklistedUserIds;
    }

    /**
     * Getter for <code>public.guild_ticket_blacklist.guild_id</code>.
     */
    public Long getGuildId() {
        return this.guildId;
    }

    /**
     * Getter for
     * <code>public.guild_ticket_blacklist.blacklisted_user_ids</code>.
     */
    public Long[] getBlacklistedUserIds() {
        return this.blacklistedUserIds;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("GuildTicketBlacklist (");

        sb.append(guildId);
        sb.append(", ").append(Arrays.toString(blacklistedUserIds));

        sb.append(")");
        return sb.toString();
    }
}
