package io.jking.untitled.utility;

public final class MiscUtil {
    private MiscUtil() {
    }

    public static boolean isAnyChoice(String input, String... options) {
        for (String option : options)
            if (input.equalsIgnoreCase(option))
                return true;
        return false;
    }

}
