package io.jking.tickster.interaction;

import io.jking.tickster.interaction.impl.button.GarbageButton;
import io.jking.tickster.interaction.type.IButton;

public class RegistryHandler {

    private final RegistryMap<IButton> BUTTON_MAP = new RegistryMap<>();

    public RegistryHandler registerButtons() {
        BUTTON_MAP.put(new GarbageButton());
        return this;
    }

    public IButton getButton(String componentId) {
        return BUTTON_MAP.get(componentId);
    }

}
