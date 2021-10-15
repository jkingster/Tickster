package io.jking.untitled.utility;

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


}
