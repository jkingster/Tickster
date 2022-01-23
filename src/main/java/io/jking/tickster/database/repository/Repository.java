package io.jking.tickster.database.repository;

import io.jking.tickster.database.JooqConnector;
import org.jooq.DSLContext;

import java.util.List;

public abstract class Repository<T> {

    private final JooqConnector jooqConnector = JooqConnector.getInstance();

    public abstract int save(T wrapper);

    public abstract int delete(long id);

    public abstract T fetch(long id);

    public abstract List<T> getAll();

    public DSLContext dsl() {
        return jooqConnector.getContext();
    }

}
