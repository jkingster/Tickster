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
                    "sql/guild_settings.sql",
                    "sql/guild_tickets.sql"
            );

            final Config config = new Config("config.json");

            if (args.length == 0) {
                Untitled.build(config);
            } else if (args[0].equalsIgnoreCase("dev")) {
                Untitled.build(config, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
