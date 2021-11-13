/*
 * This file is generated by jOOQ.
 */
package io.jking.tickster.jooq.tables.records;


import io.jking.tickster.jooq.tables.GuildTickets;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.TableRecordImpl;

import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GuildTicketsRecord extends TableRecordImpl<GuildTicketsRecord> implements Record7<Long, Long, Long, Long, LocalDateTime, Boolean, JSON> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.guild_tickets.guild_id</code>.
     */
    public GuildTicketsRecord setGuildId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.guild_tickets.guild_id</code>.
     */
    public Long getGuildId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.guild_tickets.channel_id</code>.
     */
    public GuildTicketsRecord setChannelId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.guild_tickets.channel_id</code>.
     */
    public Long getChannelId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.guild_tickets.category_id</code>.
     */
    public GuildTicketsRecord setCategoryId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.guild_tickets.category_id</code>.
     */
    public Long getCategoryId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.guild_tickets.creator_id</code>.
     */
    public GuildTicketsRecord setCreatorId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.guild_tickets.creator_id</code>.
     */
    public Long getCreatorId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.guild_tickets.ticket_timestamp</code>.
     */
    public GuildTicketsRecord setTicketTimestamp(LocalDateTime value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.guild_tickets.ticket_timestamp</code>.
     */
    public LocalDateTime getTicketTimestamp() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>public.guild_tickets.open</code>.
     */
    public GuildTicketsRecord setOpen(Boolean value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.guild_tickets.open</code>.
     */
    public Boolean getOpen() {
        return (Boolean) get(5);
    }

    /**
     * Setter for <code>public.guild_tickets.transcript</code>.
     */
    public GuildTicketsRecord setTranscript(JSON value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.guild_tickets.transcript</code>.
     */
    public JSON getTranscript() {
        return (JSON) get(6);
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, Long, Long, LocalDateTime, Boolean, JSON> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, Long, Long, Long, LocalDateTime, Boolean, JSON> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return GuildTickets.GUILD_TICKETS.GUILD_ID;
    }

    @Override
    public Field<Long> field2() {
        return GuildTickets.GUILD_TICKETS.CHANNEL_ID;
    }

    @Override
    public Field<Long> field3() {
        return GuildTickets.GUILD_TICKETS.CATEGORY_ID;
    }

    @Override
    public Field<Long> field4() {
        return GuildTickets.GUILD_TICKETS.CREATOR_ID;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return GuildTickets.GUILD_TICKETS.TICKET_TIMESTAMP;
    }

    @Override
    public Field<Boolean> field6() {
        return GuildTickets.GUILD_TICKETS.OPEN;
    }

    @Override
    public Field<JSON> field7() {
        return GuildTickets.GUILD_TICKETS.TRANSCRIPT;
    }

    @Override
    public Long component1() {
        return getGuildId();
    }

    @Override
    public Long component2() {
        return getChannelId();
    }

    @Override
    public Long component3() {
        return getCategoryId();
    }

    @Override
    public Long component4() {
        return getCreatorId();
    }

    @Override
    public LocalDateTime component5() {
        return getTicketTimestamp();
    }

    @Override
    public Boolean component6() {
        return getOpen();
    }

    @Override
    public JSON component7() {
        return getTranscript();
    }

    @Override
    public Long value1() {
        return getGuildId();
    }

    @Override
    public Long value2() {
        return getChannelId();
    }

    @Override
    public Long value3() {
        return getCategoryId();
    }

    @Override
    public Long value4() {
        return getCreatorId();
    }

    @Override
    public LocalDateTime value5() {
        return getTicketTimestamp();
    }

    @Override
    public Boolean value6() {
        return getOpen();
    }

    @Override
    public JSON value7() {
        return getTranscript();
    }

    @Override
    public GuildTicketsRecord value1(Long value) {
        setGuildId(value);
        return this;
    }

    @Override
    public GuildTicketsRecord value2(Long value) {
        setChannelId(value);
        return this;
    }

    @Override
    public GuildTicketsRecord value3(Long value) {
        setCategoryId(value);
        return this;
    }

    @Override
    public GuildTicketsRecord value4(Long value) {
        setCreatorId(value);
        return this;
    }

    @Override
    public GuildTicketsRecord value5(LocalDateTime value) {
        setTicketTimestamp(value);
        return this;
    }

    @Override
    public GuildTicketsRecord value6(Boolean value) {
        setOpen(value);
        return this;
    }

    @Override
    public GuildTicketsRecord value7(JSON value) {
        setTranscript(value);
        return this;
    }

    @Override
    public GuildTicketsRecord values(Long value1, Long value2, Long value3, Long value4, LocalDateTime value5, Boolean value6, JSON value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GuildTicketsRecord
     */
    public GuildTicketsRecord() {
        super(GuildTickets.GUILD_TICKETS);
    }

    /**
     * Create a detached, initialised GuildTicketsRecord
     */
    public GuildTicketsRecord(Long guildId, Long channelId, Long categoryId, Long creatorId, LocalDateTime ticketTimestamp, Boolean open, JSON transcript) {
        super(GuildTickets.GUILD_TICKETS);

        setGuildId(guildId);
        setChannelId(channelId);
        setCategoryId(categoryId);
        setCreatorId(creatorId);
        setTicketTimestamp(ticketTimestamp);
        setOpen(open);
        setTranscript(transcript);
    }
}