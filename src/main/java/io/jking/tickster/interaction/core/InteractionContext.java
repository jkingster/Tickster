package io.jking.tickster.interaction.core;

import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class InteractionContext<T extends GenericInteractionCreateEvent> {

    private final T event;

    public InteractionContext(T event) {
        this.event = event;
    }

    public T getEvent() {
        return event;
    }

    public JDA getJDA() {
        return event.getJDA();
    }

    public Guild getGuild() {
        return event.getGuild();
    }

    public TextChannel getTextChannel() {
        return event.getTextChannel();
    }

    public Member getMember() {
        return event.getMember();
    }

    public User getUser() {
        return event.getUser();
    }

    public Member getSelfMember() {
        return getGuild().getSelfMember();
    }

    public User getSelfUser() {
        return getSelfMember().getUser();
    }

    public ReplyAction reply(String content) {
        return event.reply(content);
    }

    public ReplyAction reply(EmbedBuilder embed) {
        return event.replyEmbeds(embed.build());
    }

    public ReplyAction replyEphemeral(String content) {
        return event.reply(content).setEphemeral(true);
    }

    public ReplyAction replyEphemeral(EmbedBuilder embed) {
        return event.replyEmbeds(embed.build()).setEphemeral(true);
    }

    public ReplyAction replySuccess(Success success, Object... objects) {
        return reply(EmbedUtil.getSuccess(success, objects));
    }

    public ReplyAction replySuccessEphemeral(Success success, Object... objects) {
        return replyEphemeral(EmbedUtil.getSuccess(success, objects));
    }

    public ReplyAction replyError(Error error,  Object... objects) {
        return reply(EmbedUtil.getError(error, objects));
    }

    public ReplyAction replyErrorEphemeral(Error error,  Object... objects) {
        return reply(EmbedUtil.getError(error, objects));
    }

    public RestAction<Member> retrieveMember(long id) {
        return getGuild().retrieveMemberById(id);
    }


}
