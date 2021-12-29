package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.core.IReply;
import io.jking.tickster.interaction.core.InteractionSender;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class SelectionSender extends InteractionSender<SelectMenuInteractionEvent> implements IReply {
    public SelectionSender(SelectMenuInteractionEvent event, Database database, CacheManager cache) {
        super(event, database, cache);
    }

    public MessageEditCallbackAction deferEdit() {
        return getEvent().deferEdit();
    }

    public ReplyCallbackAction deferReply() {
        return getEvent().deferReply();
    }

    @Override
    public InteractionHook getHook() {
        return getEvent().getHook();
    }
}
