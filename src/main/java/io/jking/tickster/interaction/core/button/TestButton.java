package io.jking.tickster.interaction.core.button;

import io.jking.tickster.interaction.impl.container.ButtonContainer;
import io.jking.tickster.interaction.impl.sender.ButtonSender;

public class TestButton extends ButtonContainer {
    public TestButton() {
        super("test", "N/A", true);
    }

    @Override
    public void onInteraction(ButtonSender sender) {
        sender.getEvent().reply("Hello again.").setEphemeral(true).queue();
    }
}
