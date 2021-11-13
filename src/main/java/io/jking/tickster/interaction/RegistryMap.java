package io.jking.tickster.interaction;

import java.util.HashMap;
import java.util.Map;

public class RegistryMap<V extends InteractionImpl<?>> {

    private final Map<String, V> map;

    public RegistryMap() {
        this.map = new HashMap<>();
    }

    public RegistryMap<V> put(V value) {
        this.map.put(value.componentId(), value);
        return this;
    }

    public V get(String key) {
        return this.map.getOrDefault(key, null);
    }


}
