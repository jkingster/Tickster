package io.jking.tickster.button;

import java.util.HashMap;
import java.util.Map;

public class ButtonRegistry {
    private final Map<String, IButton> buttonMap;

    public ButtonRegistry() {
        this.buttonMap = new HashMap<>();
    }

    public void addButton(IButton button) {
        buttonMap.put(button.buttonId(), button);
    }

    public ButtonRegistry addButtons(IButton... buttons) {
        for (IButton button : buttons) {
            addButton(button);
        }
        return this;
    }

    public IButton getButton(String buttonId) {
        return buttonMap.getOrDefault(buttonId, null);
    }
}
