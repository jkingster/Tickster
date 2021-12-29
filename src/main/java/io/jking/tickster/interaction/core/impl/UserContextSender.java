package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.core.reply.ICommandReply;
import io.jking.tickster.interaction.core.InteractionSender;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;

public class UserContextSender extends InteractionSender<UserContextInteractionEvent> implements ICommandReply<UserContextInteractionEvent> {
    public UserContextSender(UserContextInteractionEvent event, Database database, CacheManager cache) {
        super(event, database, cache);
    }
}
