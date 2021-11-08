package io.jking.tickster.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.utils.Checks;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;

public class Hikari {

    private final HikariDataSource hikariDataSource;

    public Hikari(DataObject dataObject) {
        this.hikariDataSource = initHikari(dataObject);
    }

    private HikariDataSource initHikari(DataObject dataObject) {
        Checks.notNull(dataObject, "Config DataObject");

        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        hikariConfig.setUsername(dataObject.getObject("database").getString("username"));
        hikariConfig.setPassword(dataObject.getObject("database").getString("password"));

        System.getProperties().setProperty("org.jooq.no-logo", "true");
        System.getProperties().setProperty("org.jooq.no-tips", "true");

        return new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        final Connection connection = hikariDataSource.getConnection();
        if (connection == null) {
            return getConnection();
        }
        return connection;
    }

    public DSLContext getDSL() {
        try {
            final Connection connection = getConnection();
            return DSL.using(connection);
        } catch (Exception ignored) {
            return getDSL();
        }
    }

}
