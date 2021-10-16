package io.jking.untitled.utility;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class MiscUtil {
    private MiscUtil() {
    }

    public static boolean isAnyOption(String input, String... options) {
        for (String option : options)
            if (input.equalsIgnoreCase(option))
                return true;
        return false;
    }

    public static boolean isSnowflake(String id) {
        try {
            net.dv8tion.jda.api.utils.MiscUtil.parseSnowflake(id);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static int getAge(OffsetDateTime dateTime) {
        return (int) ChronoUnit.DAYS.between(dateTime, OffsetDateTime.now());
    }

    public static String getFormattedDate(OffsetDateTime dateTime) {
        return DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm").format(dateTime);
    }

}
