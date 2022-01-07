package io.jking.tickster.utility;

import io.jking.tickster.core.TicksterInfo;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public final class MiscUtil {

    private MiscUtil() {
    }

    public static void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage(content))
                .queue(null, new ErrorHandler().ignore(ErrorResponse.CANNOT_SEND_TO_USER));
    }

    public static int getDays(OffsetDateTime offsetDateTime) {
        return (int) ChronoUnit.DAYS.between(offsetDateTime, OffsetDateTime.now());
    }

    public static boolean hasRole(Member member, long roleId) {
        for (Role role : member.getRoles()) {
            if (role.getIdLong() == roleId)
                return true;
        }
        return false;
    }

    public static boolean isSnowflake(String snowflake) {
        try {
            net.dv8tion.jda.api.utils.MiscUtil.parseSnowflake(snowflake);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean isSupport(GuildDataRecord guildRecord, Member member) {
        final long supportId = guildRecord.getSupportId();
        final Role role = member.getGuild().getRoleById(supportId);
        if (role == null)
            return false;

        return hasRole(member, supportId);
    }

    public static boolean isDeveloper(long memberId) {
        for (long id : TicksterInfo.DEVELOPER_IDS) {
            if (memberId == id)
                return true;
        }
        return false;
    }

}
