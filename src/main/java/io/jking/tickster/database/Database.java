package io.jking.tickster.database;

import okio.ByteString;
import org.jooq.DSLContext;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

public class Database {

    private final Hikari hikari;

    public Database(Hikari hikari) {
        this.hikari = hikari;
    }

    public Database createTables(String... tablePaths) {
        try {
            final ClassLoader classLoader = Database.class.getClassLoader();
            for (String path : tablePaths) {
                final InputStream inputStream = classLoader.getResourceAsStream(path);
                if (inputStream == null)
                    continue;

                final String SQL = ByteString.read(inputStream, inputStream.available()).string(StandardCharsets.UTF_8);
                final Connection connection = hikari.getConnection();

                connection.createStatement().execute(SQL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public DSLContext getDSL() {
        return hikari.getDSL();
    }
}
