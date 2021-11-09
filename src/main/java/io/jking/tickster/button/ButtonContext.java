package io.jking.tickster.button;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.cache.impl.GuildCache;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;


public class ButtonContext {

    private final ButtonClickEvent event;
    private final Cache cache;

    public ButtonContext(ButtonClickEvent event, Cache cache) {
        this.event = event;
        this.cache = cache;
    }

    public ButtonClickEvent getEvent() {
        return event;
    }

    public Cache getCache() {
        return cache;
    }

    public GuildCache getGuildCache() {
        return cache.getGuildCache();
    }

    public TextChannel getChannel() {
        return getEvent().getTextChannel();
    }

    public Guild getGuild() {
        return getEvent().getGuild();
    }

    public String getComponentId() {
        return getEvent().getComponentId();
    }

    public String getId() {
        return getEvent().getId();
    }

    public InteractionHook getHook() {
        return getEvent().getHook();
    }

    public ButtonInteraction getInteraction() {
        return getEvent().getInteraction();
    }

    public Member getMember() {
        return getEvent().getMember();
    }

    public User getUser() {
        return getEvent().getUser();
    }

    public ReplyAction reply(String content) {
        return getInteraction().reply(content);
    }
}
