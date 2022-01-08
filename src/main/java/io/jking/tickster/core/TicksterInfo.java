package io.jking.tickster.core;

import io.jking.tickster.Starter;

import java.io.File;

public class TicksterInfo {

    public static final long[] DEVELOPER_IDS = {
            769456676016226314L
    };

    public static String TICKSTER_VERSION = getVersion();

    // https://stackoverflow.com/questions/14189162/get-name-of-running-jar-or-exe/14189195
    // This is hacky but w.e. smh
    private static String getVersion() {
        try {
            final String name = new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getName();
            final String[] split = name.split("-");
            return String.format("%s-%s", split[1], split[2]).replace(".jar", "");
        } catch (Exception ignored) {
            return "Unknown.";
        }
    }


}
