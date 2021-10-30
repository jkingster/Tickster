package io.jking.untitled;

import io.jking.untitled.core.Config;
import io.jking.untitled.core.Untitled;
import io.jking.untitled.database.Database;


public class Starter {

    public static void main(String[] args) {
        try {
            Database.getInstance().buildTables(
                    "sql/guild_data.sql",
                    "sql/guild_infractions.sql",
                    "sql/guild_settings.sql"
            );

            final Config config = new Config("config.json");

            final String mode = args[0];
            if (mode.equalsIgnoreCase("dev")) {
                Untitled.build(config, true);
            } else {
                Untitled.build(config);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
