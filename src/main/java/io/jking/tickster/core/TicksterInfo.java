package io.jking.tickster.core;

import io.jking.tickster.Starter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class TicksterInfo {

    public static final long[] DEVELOPER_IDS = {
            769456676016226314L
    };

    public static String TICKSTER_VERSION = getVersion();

    // https://stackoverflow.com/questions/14189162/get-name-of-running-jar-or-exe/14189195
    private static String getVersion() {
        try {
            return new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .getName();
        } catch (Exception ignored) {
            return "Unknown.";
        }
    }


}
