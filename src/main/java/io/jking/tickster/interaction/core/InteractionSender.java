package io.jking.tickster.interaction.core;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.cache.impl.TicketCache;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;


public class InteractionSender<T extends GenericInteractionCreateEvent> {

    private final T event;
    private final Database database;
    private final CacheManager cache;

    public InteractionSender(T event, Database database, CacheManager cache) {
        this.event = event;
        this.database = database;
        this.cache = cache;
    }

    public T getEvent() {
        return event;
    }

    public Interaction getInteraction() {
        return event.getInteraction();
    }

    public Database getDatabase() {
        return database;
    }

    public CacheManager getCache() {
        return cache;
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

    public MessageAction sendMessage(String content) {
        return getTextChannel().sendMessage(content);
    }

    public MessageAction sendMessage(EmbedBuilder embed) {
        return getTextChannel().sendMessageEmbeds(embed.build());
    }

    public RestAction<Member> retrieveMember(long id) {
        return getGuild().retrieveMemberById(id);
    }

    public GuildCache getGuildCache() {
        return getCache().getGuildCache();
    }

    public TicketCache getTicketCache() {
        return getCache().getTicketCache();
    }

    public GuildDataRecord getGuildRecord() {
        return getCache().getGuildCache().fetchOrGet(getGuild().getIdLong());
    }

    public GuildTicketsRecord getTicketRecord() {
        return getCache().getTicketCache().fetchOrGet(getTextChannel().getIdLong());
    }


}