package io.jking.tickster.object;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

public enum CButton {

    DANGER(),
    PRIMARY(),
    SECONDARY(),
    SUCCESS();


    public Button format(String buttonId, String label, Emoji emoji) {
        return switch (this) {
            case DANGER -> Button.of(ButtonStyle.DANGER, buttonId, label, emoji);
            case PRIMARY -> Button.of(ButtonStyle.PRIMARY, buttonId, label, emoji);
            case SECONDARY -> Button.of(ButtonStyle.SECONDARY, buttonId, label, emoji);
            case SUCCESS -> Button.of(ButtonStyle.SUCCESS, buttonId, label, emoji);
        };
    }

    public Button format(String buttonId, String label) {
        return switch (this) {
            case DANGER -> Button.of(ButtonStyle.DANGER, buttonId, label);
            case PRIMARY -> Button.of(ButtonStyle.PRIMARY, buttonId, label);
            case SECONDARY -> Button.of(ButtonStyle.SECONDARY, buttonId, label);
            case SUCCESS -> Button.of(ButtonStyle.SUCCESS, buttonId, label);
        };
    }


}
