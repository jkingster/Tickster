package io.jking.tickster.utility;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

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

}
