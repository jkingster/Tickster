package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.core.IReply;
import io.jking.tickster.interaction.core.InteractionSender;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;


public class ButtonSender extends InteractionSender<ButtonInteractionEvent> implements IReply {
    public ButtonSender(ButtonInteractionEvent event, Database database, CacheManager cache) {
        super(event, database, cache);
    }

    public MessageEditCallbackAction deferEdit() {
        return getEvent().deferEdit();
    }

    public ReplyCallbackAction deferReply() {
        return getEvent().deferReply();
    }
    public String getComponentId() {
        return getEvent().getComponentId();
    }

    @Override
    public InteractionHook getHook() {
        return getEvent().getHook();
    }
}
