package io.jking.untitled.cache.impl;

import io.jking.untitled.cache.ICache;
import io.jking.untitled.database.Hikari;
import io.jking.untitled.jooq.tables.GuildData;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.util.Map;
import java.util.function.Consumer;

public class GuildCache implements ICache<Long, GuildData> {

    private final Map<Long, GuildData> guildMap;

    public GuildCache() {
        this.guildMap = ExpiringMap.builder()
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .build();
    }


    @Override
    public void put(Long key, GuildData value) {
    }

    @Override
    public void delete(Long key) {

    }

    @Override
    public void retrieve(Long key, Consumer<GuildData> value) {

    }

    @Override
    public void update(Long key, GuildData newValue) {

    }

    @Override
    public GuildData get(Long key) {
        return null;
    }
}
