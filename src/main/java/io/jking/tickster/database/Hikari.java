package io.jking.tickster.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.jking.tickster.core.Config;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;

import javax.sql.DataSource;

public class Hikari {

    private final DataSource dataSource;
    private final DefaultConfiguration defaultConfiguration;

    public Hikari(Config config) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl(config.getDBUrl());
        hikariConfig.setUsername(config.getDBUsername());
        hikariConfig.setPassword(config.getDBPassword());
        hikariConfig.setMaximumPoolSize(6);
        hikariConfig.setPoolName("Database-Pool");

        System.getProperties().setProperty("org.jooq.no-logo", "true");
        System.getProperties().setProperty("org.jooq.no-tips", "true");

        this.dataSource = new HikariDataSource(hikariConfig);
        this.defaultConfiguration = new DefaultConfiguration();
        defaultConfiguration.setDataSource(dataSource);
        defaultConfiguration.setSQLDialect(SQLDialect.POSTGRES);

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public DefaultConfiguration getDefaultConfiguration() {
        return defaultConfiguration;
    }
}
