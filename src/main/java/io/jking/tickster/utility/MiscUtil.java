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
}
