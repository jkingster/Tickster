package io.jking.tickster.interaction.impl.button;

import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import net.dv8tion.jda.api.interactions.InteractionHook;


public class GarbageButton implements IButton {

    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit()
                .flatMap(InteractionHook::deleteOriginal)
                .queue();
    }

    @Override
    public String componentId() {
        return "button_trash";
    }
}




