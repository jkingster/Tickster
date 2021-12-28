package io.jking.tickster.cache.impl;

import io.jking.tickster.object.InviteData;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.util.Map;

public class InviteCache {

    private final Map<String, InviteData> INVITE_MAP = ExpiringMap.builder()
            .expirationPolicy(ExpirationPolicy.ACCESSED)
            .build();

    public void put(InviteData inviteData) {
        this.INVITE_MAP.put(inviteData.getCode(), inviteData);
    }

    public InviteData get(String code) {
        return INVITE_MAP.getOrDefault(code, null);
    }

    public void delete(String code) {
        INVITE_MAP.remove(code);
    }

}
