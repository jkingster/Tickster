package io.jking.tickster.button;

public interface IButton {

    void onButtonPress(ButtonContext context);

    String buttonId();
}
