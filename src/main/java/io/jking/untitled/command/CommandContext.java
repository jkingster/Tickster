package io.jking.untitled.command;

import io.jking.untitled.core.Config;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class CommandContext {

    private final SlashCommandEvent event;
    private final Config config;

    public CommandContext(SlashCommandEvent event, Config config) {
        this.event = event;
        this.config = config;
    }

    public SlashCommandEvent getEvent() {
        return event;
    }

    public Config getConfig() {
        return config;
    }

    public ReplyAction reply(String content) {
        return getEvent().reply(content);
    }
}
