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
            Untitled.build(new Config("config.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
