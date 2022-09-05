package io.jking.tickster.database;

import org.jooq.DSLContext;
import org.jooq.Record;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Repository<T extends Record> {

    private final DatabaseConnector connector = DatabaseConnector.getInstance();

    public abstract void save(T record);

    public abstract T retrieve(long id);

    public abstract void delete(long id);

    public abstract void update(T record, BiConsumer<T, Boolean> biConsumer);

    public DatabaseConnector getConnector() {
        return connector;
    }

    public DSLContext dsl() {
        return getConnector().getContext();
    }
}
