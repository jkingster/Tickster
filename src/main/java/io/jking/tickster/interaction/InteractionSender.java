package io.jking.tickster.interaction;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.Interaction;

public class InteractionSender<T extends GenericInteractionCreateEvent> {

    private final T event;

    public InteractionSender(T event) {
        this.event = event;
    }

    public T getEvent() {
        return this.event;
    }

    public JDA getJDA() {
        return getEvent().getJDA();
    }

    public Guild getGuild() {
        return getEvent().getGuild();
    }

    public Interaction getInteraction() {
        return getEvent().getInteraction();
    }

    public Member getMember() {
        return getEvent().getMember();
    }

    public User getUser() {
        return getEvent().getUser();
    }

    public MessageChannel getMessageChannel() {
        return getEvent().getMessageChannel();
    }

    public TextChannel getTextChannel() {
        return getEvent().getTextChannel();
    }

    public Member getSelfMember() {
        return getGuild().getSelfMember();
    }

    public User getSelfUser() {
        return getJDA().getSelfUser();
    }
}
