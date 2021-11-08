package io.jking.tickster.objects.command;

import io.jking.tickster.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import javax.swing.text.html.Option;

public class CommandContext {

    private final SlashCommandEvent event;
    private final Database database;

    public CommandContext(SlashCommandEvent event, Database database) {
        this.event = event;
        this.database = database;
    }

    public SlashCommandEvent getEvent() {
        return event;
    }

    public Database getDatabase() {
        return database;
    }

    public JDA getJda() {
        return event.getJDA();
    }

    public Guild getGuild() {
        return event.getGuild();
    }

    public User getSelf() {
        return event.getJDA().getSelfUser();
    }

    public ReplyAction replyFormatted(String content, Object... objects) {
        return reply(content.formatted(objects));
    }

    public ReplyAction reply(EmbedBuilder embedBuilder) {
        return event.replyEmbeds(embedBuilder.build());
    }

    public ReplyAction reply(String content) {
        return event.reply(content);
    }

    public String getSubCommand() {
        return getEvent().getSubcommandName();
    }

    public TextChannel getOptionChannel(String name) {
        final OptionMapping mapping = getMapping(name);
        if (mapping == null)
            return null;
        return (TextChannel) mapping.getAsMessageChannel();
    }

    public Member getSelfMember() {
        return getGuild().getSelfMember();
    }

    public boolean canInteract(Role role) {
        return getSelfMember().canInteract(role);
    }

    public Role getOptionRole(String name) {
        final OptionMapping mapping = getMapping(name);
        if (mapping == null)
            return null;
        return mapping.getAsRole();
    }

    private OptionMapping getMapping(String name) {
        return getEvent().getOption(name);
    }
}
