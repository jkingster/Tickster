package io.jking.tickster;

import io.jking.tickster.core.Tickster;
public class Starter {

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                Tickster.buildProduction("config.json");
            } else if (args[0].equalsIgnoreCase("dev")) {
                Tickster.buildDev("config.json");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
