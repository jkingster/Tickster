package io.jking.tickster.interaction.button.object;

public interface IButton {

    void onButtonPress(ButtonContext context);

    String buttonId();
}
