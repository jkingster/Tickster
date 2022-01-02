package io.jking.tickster.interaction.core.reply;

import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public interface IComponentReply<T extends GenericComponentInteractionCreateEvent> {

    T getEvent();

    default InteractionHook getHook() {
        return getEvent().getHook();
    }

    default ReplyCallbackAction deferReply() {
        return getEvent().deferReply();
    }

    default ReplyCallbackAction deferReply(boolean ephemeral) {
        return getEvent().deferReply(ephemeral);
    }

    default MessageEditCallbackAction deferEdit() {
        return getEvent().deferEdit();
    }

    default ReplyCallbackAction reply(String content) {
        return getEvent().reply(content);
    }

    default ReplyCallbackAction reply(EmbedBuilder embed) {
        return getEvent().replyEmbeds(embed.build());
    }

    default ReplyCallbackAction replyEphemeral(String content) {
        return getEvent().reply(content).setEphemeral(true);
    }

    default ReplyCallbackAction replyEphemeral(EmbedBuilder embed) {
        return getEvent().replyEmbeds(embed.build()).setEphemeral(true);
    }

    default ReplyCallbackAction replyEphemeralFormat(String pattern, Object... objects) {
        return replyEphemeral(String.format(pattern, objects));
    }

    default ReplyCallbackAction replySuccess(Success success, Object... objects) {
        return reply(EmbedUtil.getSuccess(success, objects));
    }

    default ReplyCallbackAction replySuccessEphemeral(Success success, Object... objects) {
        return replyEphemeral(EmbedUtil.getSuccess(success, objects));
    }

    default ReplyCallbackAction replyError(Error error, Object... objects) {
        return reply(EmbedUtil.getError(error, objects));
    }

    default ReplyCallbackAction replyErrorEphemeral(Error error, Object... objects) {
        return replyEphemeral(EmbedUtil.getError(error, objects));
    }

}
