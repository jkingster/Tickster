package io.jking.tickster.interaction.command;

import io.jking.tickster.interaction.command.impl.admin.SettingsCommand;
import io.jking.tickster.interaction.command.impl.developer.BlacklistCommand;
import io.jking.tickster.interaction.command.impl.developer.UpdateCommand;
import io.jking.tickster.interaction.command.impl.info.AboutCommand;
import io.jking.tickster.interaction.command.impl.info.InfoCommand;
import io.jking.tickster.interaction.command.impl.ticket_support.SupportCommand;
import io.jking.tickster.interaction.command.impl.utility.*;
import io.jking.tickster.interaction.core.Registry;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommandRegistry extends Registry<AbstractCommand> {

    public CommandRegistry() {
        put("help", new HelpCommand(this));
        put("update", new UpdateCommand(this));
        put("info", new InfoCommand());
        put("about", new AboutCommand());
        put("snowflake", new SnowflakeCommand());
        put("test", new TestCommand());
        put("support", new SupportCommand());
        put("settings", new SettingsCommand());
        put("server", new ServerCommand());
        put("blacklist", new BlacklistCommand());
        put("stats", new StatsCommand());
    }

    public List<AbstractCommand> getCommandsByCategory(CommandCategory category, Member member) {
        return getMap()
                .values()
                .stream()
                .filter(command -> command.getCategory() == category && member.hasPermission(command.getPermission()))
                .sorted(Comparator.comparing(AbstractCommand::getName))
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    public List<AbstractCommand> getCommands() {
        return getMap()
                .values()
                .stream()
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

}
