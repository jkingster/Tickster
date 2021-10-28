package io.jking.untitled.event;

import io.jking.untitled.command.CommandRegistry;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class StartEvent extends ListenerAdapter {

    private final CommandRegistry registry;

    public StartEvent(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getJDA().updateCommands().queue();
    }
}
