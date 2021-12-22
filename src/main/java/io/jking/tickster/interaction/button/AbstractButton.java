package io.jking.tickster.interaction.button;

import io.jking.tickster.interaction.core.impl.ButtonContext;

public abstract class AbstractButton {

    private final String buttonId;

    public AbstractButton(String buttonId) {
        this.buttonId = buttonId;
    }

    public abstract void onButtonPress(ButtonContext context);

    public String getButtonId() {
        return buttonId;
    }

}
