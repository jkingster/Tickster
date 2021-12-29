package io.jking.tickster.interaction.core;

import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;

public interface IReply {
    
    InteractionHook getHook();

    default WebhookMessageAction<Message> reply(String content) {
        return getHook().sendMessage(content);
    }

    default WebhookMessageAction<Message> reply(EmbedBuilder embed) {
        return getHook().sendMessageEmbeds(embed.build());
    }

    default WebhookMessageAction<Message> replyEphemeral(String content) {
        return getHook().sendMessage(content).setEphemeral(true);
    }

    default WebhookMessageAction<Message> replyEphemeral(EmbedBuilder embed) {
        return getHook().sendMessageEmbeds(embed.build()).setEphemeral(true);
    }

    default WebhookMessageAction<Message> replySuccess(Success success, Object... objects) {
        return reply(EmbedUtil.getSuccess(success, objects));
    }

    default WebhookMessageAction<Message> replySuccessEphemeral(Success success, Object... objects) {
        return replyEphemeral(EmbedUtil.getSuccess(success, objects));
    }

    default WebhookMessageAction<Message> replyError(Error error, Object... objects) {
        return reply(EmbedUtil.getError(error, objects));
    }

    default WebhookMessageAction<Message> replyErrorEphemeral(Error error, Object... objects) {
        return replyEphemeral(EmbedUtil.getError(error, objects));
    }

}
