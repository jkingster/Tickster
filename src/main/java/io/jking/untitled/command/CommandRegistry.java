package io.jking.untitled.command;

import io.jking.untitled.command.impl.bot_owner.TestCommand;
import io.jking.untitled.command.impl.utility.PingCommand;

import java.util.*;
import java.util.stream.Collectors;

public class CommandRegistry {

    private final Map<String, Command> COMMAND_MAP = new HashMap<>();

    public CommandRegistry() {
        addCommands(new TestCommand(), new PingCommand());
    }

    public void addCommand(Command command) {
        final String commandName = command.getName();
        COMMAND_MAP.put(commandName, command);
    }

    public void addCommands(Command... commands) {
        Arrays.asList(commands).forEach(this::addCommand);
    }

    public Command getCommand(String commandName) {
        return COMMAND_MAP.getOrDefault(commandName, null);
    }

    public List<Command> getCommands() {
        return COMMAND_MAP.values().stream().collect(Collectors.toUnmodifiableList());
    }

    public List<Command> getCommandsByCategory(Category category) {
        return COMMAND_MAP.values()
                .stream()
                .filter(command -> command.getCategory() == category)
                .sorted(Comparator.comparing(Command::getName))
                .collect(Collectors.toUnmodifiableList());
    }

}
