package io.jking.untitled.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.jking.untitled.core.Config;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Hikari {

    private static Hikari hikari = new Hikari();

    private static final HikariDataSource dataSource;

    static {
        final DataObject dataObject = getConfig();
        if (dataObject == null) {
            throw new IllegalStateException("Config (Hikari) is null!");
        }

        final HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername(dataObject.getObject("database").getString("username"));
        config.setPassword(dataObject.getObject("database").getString("password"));

        dataSource = new HikariDataSource(config);
    }

    private Hikari(){}

    public static Hikari getInstance() {
        return hikari;
    }

    private static DataObject getConfig() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("config.json"))) {
            return DataObject.fromJson(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected HikariDataSource getDataSource() {
        return dataSource;
    }

    public DSLContext getDSL() {
        try {
            final Connection connection = getConnection();
            return DSL.using(connection);
        } catch (Exception ignored) {
            return getDSL();
        }
    }

    public Connection getConnection() throws SQLException {
        final Connection connection = dataSource.getConnection();
        if (connection == null) {
            return getConnection();
        }
        return connection;
    }

}
