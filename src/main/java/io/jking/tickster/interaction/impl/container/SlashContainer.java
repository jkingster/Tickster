package io.jking.tickster.interaction.impl.container;

import io.jking.tickster.interaction.InteractionContainer;
import io.jking.tickster.interaction.InteractionSender;
import io.jking.tickster.interaction.impl.sender.SlashSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.*;

public abstract class SlashContainer extends InteractionContainer<SlashSender> {

    private final SlashCommandData data;
    private final Permission       permission;

    public SlashContainer(String interactionKey, String interactionDescription) {
        super(interactionKey, interactionDescription);
        this.data = Commands.slash(interactionKey, interactionDescription);
        this.permission = Permission.MESSAGE_SEND;
    }

    public SlashContainer(String interactionKey, String interactionDescription, Permission permission) {
        super(interactionKey, interactionDescription);
        this.data = Commands.slash(getInteractionKey(), getInteractionDescription());
        this.permission = permission;
    }

    public SlashCommandData getData() {
        return data;
    }

    public Permission getPermission() {
        return permission;
    }

    public void addSubCommands(SubcommandData... subcommands) {
        for (SubcommandData data : subcommands)
            this.getData().addSubcommands(data);
    }

    public void addSubCommandGroup(SubcommandGroupData... subcommandGroupData) {
        this.getData().addSubcommandGroups(subcommandGroupData);
    }

    public static abstract class SubCommandContainer extends SlashContainer {

        public SubCommandContainer(String interactionKey, String interactionDescription, OptionData... optionData) {
            super(interactionKey, interactionDescription);
            super.getData().addSubcommands(new SubcommandData(interactionKey, interactionDescription).addOptions(optionData));
        }
    }
}
