package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.core.Tickster;
import io.jking.tickster.interaction.core.InteractionSender;
import io.jking.tickster.interaction.core.reply.ICommandReply;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;

public class UserContextSender extends InteractionSender<UserContextInteractionEvent> implements ICommandReply<UserContextInteractionEvent> {
    public UserContextSender(Tickster tickster, UserContextInteractionEvent event) {
        super(tickster, event);
    }
}
