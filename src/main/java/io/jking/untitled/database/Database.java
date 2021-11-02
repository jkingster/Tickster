package io.jking.untitled.database;

import okio.ByteString;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

public class Database {

    private static final Database database = new Database();

    private Database(){}

    public void buildTables(String... tablePath) {
        try {
            final ClassLoader classLoader = Database.class.getClassLoader();
            for (String path : tablePath) {
                final InputStream inputStream = classLoader.getResourceAsStream(path);
                if (inputStream == null)
                    continue;

                final String SQL = ByteString.read(inputStream, inputStream.available()).string(StandardCharsets.UTF_8);
                final Connection connection = Hikari.getInstance().getConnection();

                connection.createStatement().execute(SQL);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        return database;
    }
}
