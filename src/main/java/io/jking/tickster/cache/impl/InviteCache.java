package io.jking.tickster.cache.impl;

import io.jking.tickster.data.InviteData;
import net.dv8tion.jda.api.entities.Invite;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InviteCache {

    private final Map<String, InviteData> INVITE_CACHE = new ConcurrentHashMap<>();

    public void put(Invite invite) {
        this.INVITE_CACHE.put(invite.getCode(), new InviteData(invite));
    }

    public InviteData get(String inviteCode) {
        return this.INVITE_CACHE.getOrDefault(inviteCode, null);
    }

    public void remote(String inviteCode) {
        this.INVITE_CACHE.remove(inviteCode);
    }

}
