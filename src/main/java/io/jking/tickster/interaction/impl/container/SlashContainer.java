package io.jking.tickster.interaction.impl.container;

import io.jking.tickster.interaction.InteractionContainer;
import io.jking.tickster.interaction.InteractionSender;
import io.jking.tickster.interaction.impl.sender.SlashSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class SlashContainer extends InteractionContainer<SlashSender> {

    private final SlashCommandData data;
    private final Map<String, SubCommandContainer> subCommandMap;
    private final Permission       permission;

    public SlashContainer(String interactionKey, String interactionDescription) {
        super(interactionKey, interactionDescription);
        this.data = Commands.slash(interactionKey, interactionDescription);
        this.subCommandMap = new HashMap<>();
        this.permission = Permission.MESSAGE_SEND;
    }

    public SlashContainer(String interactionKey, String interactionDescription, Permission permission) {
        super(interactionKey, interactionDescription);
        this.data = Commands.slash(getInteractionKey(), getInteractionDescription());
        this.subCommandMap = new HashMap<>();
        this.permission = permission;
    }

    public SlashCommandData getData() {
        return data;
    }

    public Permission getPermission() {
        return permission;
    }

    public void putSubContainers(SubCommandContainer... containers) {
        for (SubCommandContainer container : containers) {
            final String key = container.getKey();
            final String description = container.getDescription();
            this.subCommandMap.put(key, container);
            this.getData().addSubcommands(
                    new SubcommandData(key, description).addOptions(container.getOptionData())
            );
        }
    }


    public SubCommandContainer getSubContainer(String subCommandName) {
        return subCommandMap.getOrDefault(subCommandName.toLowerCase(), null);
    }

    public void addSubCommandGroup(SubcommandGroupData... subcommandGroupData) {
        this.getData().addSubcommandGroups(subcommandGroupData);
    }

    public static abstract class SubCommandContainer extends SlashContainer {

        private final String key;
        private final String description;
        private OptionData[] optionData;

        public SubCommandContainer(String key, String description, OptionData... optionData) {
            super(key, description);
            this.key = key;
            this.description = description;
            this.optionData = optionData;
            super.getData().addSubcommands(new SubcommandData(key, description).addOptions(optionData));
        }

        public String getKey() {
            return key;
        }

        public String getDescription() {
            return description;
        }

        public OptionData[] getOptionData() {
            return optionData;
        }
    }
}
