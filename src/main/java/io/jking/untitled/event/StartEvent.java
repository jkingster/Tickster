package io.jking.untitled.event;

import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public class StartEvent extends ListenerAdapter {

    private final CommandRegistry registry;

    public StartEvent(CommandRegistry registry) {
        this.registry = registry;
    }

    /**
     * This process will only add a command globally
     * if the command does not exist from the retrieved
     * list. However this presents the potential issue
     * that if we update any said command, we will have
     * to manually insert it again unless we can
     * think of some work around. For now this
     * can be ignored as all commands are in testing phase
     * and will only be inserted via Guild rather than globally.
     * {@link JDA#upsertCommand(CommandData)} versus {@link Guild#upsertCommand(CommandData)}
     * <p>
     * {@see {@link this#upsertCommand(Guild, Command)}}
     */
    @Override
    public void onReady(@NotNull ReadyEvent event) {
//        final JDA jda = event.getJDA();
//        jda.retrieveCommands().queue(commands -> {
//            for (Command command : registry.getCommands()) {
//                if (!command.isGlobal())
//                    continue;
//
//                final String commandName = command.getName();
//                if (commands.stream().noneMatch(cmd -> cmd.getName().equalsIgnoreCase(commandName))) {
//                    upsertCommand(jda, command);
//                }
//            }
//        });
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        final Guild guild = event.getGuild();
       registry.getCommands().forEach(command -> upsertCommand(guild, command));
    }

    private void upsertCommand(Guild guild, Command command) {
        guild.upsertCommand(command).queue(null, Throwable::printStackTrace);
    }

    private void upsertCommand(JDA jda, Command command) {
        jda.upsertCommand(command).queue(null, Throwable::printStackTrace);
    }
}
