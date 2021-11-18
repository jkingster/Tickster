package io.jking.tickster.utility;

public final class MiscUtil {
    private MiscUtil() {
    }

    public static boolean containsAnyOption(String option, String... options) {
        for (String possibleOption : options) {
            if (possibleOption.equalsIgnoreCase(option))
                return true;
        }
        return false;
    }

    public static boolean containsAnyNull(Object... objects) {
        for (Object object : objects) {
            if (object == null)
                return true;
        }
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
}
