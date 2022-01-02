package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.core.Tickster;
import io.jking.tickster.interaction.core.InteractionSender;
import io.jking.tickster.interaction.core.reply.IComponentReply;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class SelectSender extends InteractionSender<SelectMenuInteractionEvent> implements IComponentReply<SelectMenuInteractionEvent> {
    public SelectSender(Tickster tickster, SelectMenuInteractionEvent event) {
        super(tickster, event);
    }

    public MessageEditCallbackAction deferEdit() {
        return getEvent().deferEdit();
    }

    public ReplyCallbackAction deferReply() {
        return getEvent().deferReply();
    }

    public SelectOption getSelectedOption() {
        return getEvent().getSelectedOptions().get(0);
    }

}
