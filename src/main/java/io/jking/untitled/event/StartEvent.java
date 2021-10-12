package io.jking.untitled.event;

import io.jking.untitled.command.CommandRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public class StartEvent extends ListenerAdapter {

    private final CommandRegistry registry;

    public StartEvent(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        final JDA jda = event.getJDA();
         //TODO: Implement feature where if global command ** DOES NOT ** exist, we upsert it. (see JDA#upsertCommand(CommandData)).
    }
}
