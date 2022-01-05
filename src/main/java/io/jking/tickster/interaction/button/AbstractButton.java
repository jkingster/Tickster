package io.jking.tickster.interaction.button;

import io.jking.tickster.interaction.core.impl.ButtonSender;

public abstract class AbstractButton {

    private final String buttonId;

    public AbstractButton(String buttonId) {
        this.buttonId = buttonId;
    }

    public abstract void onButtonPress(ButtonSender sender);

    public String getButtonId() {
        return buttonId;
    }

}
