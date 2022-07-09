package io.jking.tickster.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.jking.tickster.core.Config;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnector {

    private static final Logger            logger   = LoggerFactory.getLogger(DatabaseConnector.class);
   private static final  DatabaseConnector instance = new DatabaseConnector();

    private HikariConfig hikariConfig;
    private DSLContext dsl;

    private DatabaseConnector() {
        configureSettings();
    }

    public static DatabaseConnector getInstance() {
        if (instance == null)
            return new DatabaseConnector();
        return instance;
    }

    public DSLContext getContext() {
        if (hikariConfig == null || dsl == null) {
            configureSettings();
        }
        return dsl;
    }

    private void configureSettings() {
        final Config config = Config.getInstance();
        final DataObject dbData = config.getDataObject("database");
        if (dbData == null) {
            logger.warn("Could not find database info via config file!");
            return;
        }

        logger.info("Configuring Hikari.");
        this.hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl(dbData.getString("url"));
        hikariConfig.setUsername(dbData.getString("username"));
        hikariConfig.setPassword(dbData.getString("password"));
        hikariConfig.setPoolName("[Tickster] Hikari-Pool");

        System.getProperties().setProperty("org.jooq.no-logo", "true");
        System.getProperties().setProperty("org.jooq.no-tips", "true");

        logger.info("Configuring DSLContext.");
        this.dsl = DSL.using(new HikariDataSource(hikariConfig), SQLDialect.POSTGRES);
    }

}
