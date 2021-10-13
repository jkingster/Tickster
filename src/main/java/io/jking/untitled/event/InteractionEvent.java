package io.jking.untitled.event;

import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandRegistry;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class InteractionEvent extends ListenerAdapter {

    private final Map<String, Command> BUTTON_MAP = new HashMap<>();
    private final Map<String, Command> SELECTION_MAP = new HashMap<>();

    private final CommandRegistry registry;

    public InteractionEvent(CommandRegistry registry) {
        this.registry = registry;
        loadMappings();
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        final String componentId = event.getComponentId();
        final Command command = BUTTON_MAP.getOrDefault(componentId, null);

        if (command == null)
            return;

        command.onButtonClick(event);
    }

    @Override
    public void onSelectionMenu(@NotNull SelectionMenuEvent event) {
        final String componentId = event.getComponentId();
        final Command command = SELECTION_MAP.getOrDefault(componentId, null);

        if (command == null)
            return;

        command.onSelectionMenu(event);
    }

    private void loadMappings() {
        for (Command command : registry.getCommands()) {
            if (command.hasButtons())
                command.getButtonKeys().forEach(key -> BUTTON_MAP.put(key, command));

            if (command.hasSelections())
                command.getSelectionKeys().forEach(key -> SELECTION_MAP.put(key, command));
        }
    }
}
