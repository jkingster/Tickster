package io.jking.tickster.interaction.core.slash.utility;

import io.jking.tickster.interaction.impl.container.SlashContainer;
import io.jking.tickster.interaction.impl.sender.SlashSender;
public class TestCommand extends SlashContainer {

    public TestCommand() {
        super("test", "Testing my functionality.");
    }

    @Override
    public void onInteraction(SlashSender sender) {
        sender.reply("I am fully functional.").setEphemeral(true).queue();
    }
}
