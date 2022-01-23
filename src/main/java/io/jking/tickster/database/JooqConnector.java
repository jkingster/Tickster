package io.jking.tickster.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.jking.tickster.core.Config;
import net.dv8tion.jda.api.utils.data.DataObject;
import okio.ByteString;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JooqConnector {

    private static final Logger logger = LoggerFactory.getLogger(JooqConnector.class);
    private static final JooqConnector instance = new JooqConnector();

    private final Config config = Config.getInstance();

    private HikariConfig hikariConfig;
    private DSLContext dsl;

    private JooqConnector() {
        logger.info("Initializing Hikari/jOOQ.");
        configureHikari();
        configureDSL();
    }

    public static JooqConnector getInstance() {
        if (instance == null)
            return new JooqConnector();
        return instance;
    }

    public void createTables() throws IOException {
        final ClassLoader classLoader = JooqConnector.class.getClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream("sql/tables.sql");
        if (inputStream == null) {
            logger.warn("Could not read resource!");
            return;
        }

        final String parsedSQL = ByteString
                .read(inputStream, inputStream.available())
                .string(StandardCharsets.UTF_8);

        getContext().execute(parsedSQL);
        logger.info("Created necessary database tables!");
    }

    public DSLContext getContext() {
        if (hikariConfig == null || dsl == null) {
            configureHikari();
            configureDSL();
        }
        return dsl;
    }

    private void configureHikari() {
        logger.info("Configuring Hikari");
        final HikariConfig hikariConfig = new HikariConfig();
        final DataObject database = config.get("database");
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl(database.getString("url"));
        hikariConfig.setUsername(database.getString("username"));
        hikariConfig.setPassword(database.getString("password"));
        hikariConfig.setPoolName("Tickster-Hikari");

        System.getProperties().setProperty("org.jooq.no-logo", "true");
        System.getProperties().setProperty("org.jooq.no-tips", "true");

        this.hikariConfig = hikariConfig;
    }

    private void configureDSL() {
        logger.info("Configuring DSLContext");
        this.dsl = DSL.using(new HikariDataSource(hikariConfig), SQLDialect.POSTGRES);
    }
}
