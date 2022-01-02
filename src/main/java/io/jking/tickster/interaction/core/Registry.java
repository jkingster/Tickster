package io.jking.tickster.interaction.core;

import java.util.HashMap;
import java.util.Map;

public class Registry<T> {

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
}
