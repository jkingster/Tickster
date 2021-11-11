package io.jking.tickster.interaction.button.object;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.slash.object.type.ErrorType;
import io.jking.tickster.utility.EmbedFactory;
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
    private final Database database;
    private final Cache cache;

    public ButtonContext(ButtonClickEvent event, Database database, Cache cache) {
        this.event = event;
        this.database = database;
        this.cache = cache;
    }

    public ButtonClickEvent getEvent() {
        return event;
    }

    public Database getDatabase() {
        return database;
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

    public ReplyAction reply(String content, Object... objects) {
        return getInteraction().reply(content.formatted(objects));
    }

    public ReplyAction replyError(ErrorType errorType, Object... objects) {
        return getInteraction().replyEmbeds(EmbedFactory.getError(errorType, objects).build());
    }


}
