package io.jking.tickster.interaction.button.impl.misc;

import io.jking.tickster.interaction.button.object.ButtonContext;
import io.jking.tickster.interaction.button.object.IButton;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class GarbageButton implements IButton {
    @Override
    public void onButtonPress(ButtonContext context) {
        context.getInteraction().deferEdit()
                .flatMap(InteractionHook::deleteOriginal)
                .queue();
    }

    @Override
    public String buttonId() {
        return "button_trash";
    }
}
