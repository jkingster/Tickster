package io.jking.tickster.database;

import io.jking.tickster.core.Config;
import okio.ByteString;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private final List<String> TABLE_PATHS = List.of(
            "sql/guild_data.sql",
            "sql/guild_tickets.sql",
            "sql/blacklist_data.sql"
    );

    private final Hikari hikari;
    private final DSLContext context;

    public Database(Config config) {
        logger.info("Configured Database (Hikari/jOOQ)");
        this.hikari = new Hikari(config);
        this.context = DSL.using(hikari.getDefaultConfiguration());
        createDatabaseTables();
    }

    private void createDatabaseTables() {
        try {
            final ClassLoader classLoader = Database.class.getClassLoader();
            for (String path : TABLE_PATHS) {
                final InputStream inputStream = classLoader.getResourceAsStream(path);
                if (inputStream == null)
                    continue;

                final String SQL = ByteString.read(inputStream, inputStream.available())
                        .string(StandardCharsets.UTF_8);

                getConnection().createStatement().execute(SQL);
                logger.info("Creating Table [{}]", path);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Hikari getHikari() {
        return hikari;
    }

    public DSLContext getContext() {
        return context;
    }

    public Connection getConnection() throws SQLException {
        return hikari.getDataSource().getConnection();
    }

}