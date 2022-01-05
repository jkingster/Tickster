package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.core.Tickster;
import io.jking.tickster.interaction.core.InteractionSender;
import io.jking.tickster.interaction.core.reply.IComponentReply;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;


public class ButtonSender extends InteractionSender<ButtonInteractionEvent> implements IComponentReply<ButtonInteractionEvent> {
    public ButtonSender(Tickster tickster, ButtonInteractionEvent event) {
        super(tickster, event);
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


}
