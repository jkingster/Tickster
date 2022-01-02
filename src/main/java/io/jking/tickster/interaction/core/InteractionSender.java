package io.jking.tickster.interaction.core;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.cache.impl.TicketCache;
import io.jking.tickster.core.Tickster;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.button.ButtonRegistry;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.select.SelectRegistry;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.sharding.ShardManager;


public class InteractionSender<T extends GenericInteractionCreateEvent> {

    private final Tickster tickster;
    private final T event;
    private final ShardManager shardManager;
    private final CommandRegistry commandRegistry;
    private final SelectRegistry selectRegistry;
    private final ButtonRegistry buttonRegistry;
    private final Database database;
    private final CacheManager cache;

    public InteractionSender(Tickster tickster, T event) {
        this.tickster = tickster;
        this.event = event;
        this.shardManager = tickster.getShardManager();
        this.commandRegistry = tickster.getCommandRegistry();
        this.selectRegistry = tickster.getSelectRegistry();
        this.buttonRegistry = tickster.getButtonRegistry();
        this.database = tickster.getDatabase();
        this.cache = tickster.getCacheManager();
    }

    public T getEvent() {
        return event;
    }

    public Interaction getInteraction() {
        return event.getInteraction();
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

    public Tickster getTickster() {
        return tickster;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public SelectRegistry getSelectRegistry() {
        return selectRegistry;
    }

    public ButtonRegistry getButtonRegistry() {
        return buttonRegistry;
    }

    public Database getDatabase() {
        return database;
    }

    public CacheManager getCache() {
        return cache;
    }
}