package io.jking.tickster.interaction.core;

import java.util.HashMap;
import java.util.Map;

public class Registry<T> {

    private int uses = 0;

    private final Map<String, T> registryMap = new HashMap<>();

    public void put(String key, T value) {
        this.registryMap.put(key, value);
    }

    public T get(String key) {
        return this.registryMap.getOrDefault(key, null);
    }

    public Map<String, T> getMap() {
        return registryMap;
    }

    public int getUses() {
        return uses;
    }

    public void incrementUses() {
        this.uses++;
    }
}
