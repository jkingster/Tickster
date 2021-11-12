package io.jking.tickster.interaction.impl.slash.object;

import net.dv8tion.jda.api.entities.Member;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<Command> getCommands() {
        return COMMAND_MAP.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Category> getCategories(Member member) {
        return COMMAND_MAP.values()
                .parallelStream()
                .map(Command::getCategory)
                .filter(category -> category.isPermitted(member))
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Command> getCommandsByCategory(Category category, Member member) {
        return COMMAND_MAP
                .values()
                .parallelStream()
                .filter(command -> member.hasPermission(command.getPermission()))
                .sorted(Comparator.comparing(Command::getName))
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }
}
