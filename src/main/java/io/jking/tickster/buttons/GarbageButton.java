package io.jking.tickster.buttons;

import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.core.impl.ButtonContext;

public class GarbageButton extends AbstractButton {
    public GarbageButton() {
        super("button:garbage");
    }

    @Override
    public void onButtonPress(ButtonContext context) {
       context.deferEdit().queue(deferred -> {
           context.getHook().deleteOriginal().queue();
       });
    }
}
