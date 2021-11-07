package io.jking.tickster.objects.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, Command> COMMAND_MAP;

    public CommandRegistry() {
        this.COMMAND_MAP = new HashMap<>();
    }

    public void addCommand(Command command) {
        this.COMMAND_MAP.put(command.getName(), command);
    }

    public Command getCommand(String name) {
        return this.COMMAND_MAP.getOrDefault(name, null);
    }

    public CommandRegistry addCommands(Command... commands) {
        Arrays.asList(commands).forEach(this::addCommand);
        return this;
    }
}
