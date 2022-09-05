package io.jking.tickster.interaction;

import io.github.classgraph.ClassGraph;
import io.jking.tickster.interaction.impl.container.ButtonContainer;
import io.jking.tickster.interaction.impl.container.ModalContainer;
import io.jking.tickster.interaction.impl.container.SlashContainer;
import io.jking.tickster.logging.LogType;
import io.jking.tickster.logging.TicksterLogger;
import net.dv8tion.jda.api.JDA;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InteractionRegistry {

    private final Registry<ButtonContainer> buttonMap = new Registry<>();
    private final Registry<ModalContainer>  modalMap  = new Registry<>();
    private final Registry<SlashContainer>  slashMap  = new Registry<>();

    public void registerInteractions(JDA jda) {
        TicksterLogger.log(LogType.GENERIC, "Registering interactions.");
        try (var result = new ClassGraph().acceptPackages("io.jking.tickster.interaction.core").scan()) {
            for (var clazz : result.getAllClasses()) {
                var loadedClazz = clazz.loadClass();
                var declaredNewClazz = loadedClazz.getDeclaredConstructor().newInstance();
                if (loadedClazz.getSuperclass() == ButtonContainer.class) {
                    var casted = (ButtonContainer) declaredNewClazz;
                    buttonMap.put(casted.getInteractionKey(), casted);
                } else if (loadedClazz.getSuperclass() == ModalContainer.class) {
                    var casted = (ModalContainer) declaredNewClazz;
                    modalMap.put(casted.getInteractionKey(), casted);
                } else if (loadedClazz.getSuperclass() == SlashContainer.class) {
                    var casted = (SlashContainer) declaredNewClazz;
                    slashMap.put(casted.getInteractionKey(), casted);
                }
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        jda.updateCommands().addCommands(
                        slashMap.getValues()
                                .stream()
                                .map(SlashContainer::getData)
                                .collect(Collectors.toList()))
                .queue();
    }

    public Registry<ButtonContainer> getButtonMap() {
        return buttonMap;
    }

    public Registry<ModalContainer> getModalMap() {
        return modalMap;
    }

    public Registry<SlashContainer> getSlashMap() {
        return slashMap;
    }

    public static class Registry<T extends InteractionContainer<?>> {

        private final Map<String, T> map;

        public Registry() {
            this.map = new HashMap<>();
        }

        public void put(String id, T value) {
            this.map.put(id, value);
        }

        public T get(String id) {
            return this.map.getOrDefault(id, null);
        }

        public List<T> getValues() {
            return map.values().stream().collect(Collectors.toUnmodifiableList());
        }
    }

}
