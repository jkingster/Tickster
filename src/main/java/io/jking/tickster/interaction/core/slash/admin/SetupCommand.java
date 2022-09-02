package io.jking.tickster.interaction.core.slash.admin;

import io.jking.tickster.interaction.impl.container.SlashContainer;
import io.jking.tickster.interaction.impl.sender.SlashSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SetupCommand extends SlashContainer {
    public SetupCommand() {
        super("setup", "Configure the settings for this server.", Permission.ADMINISTRATOR);
        super.getData().setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        super.putSubContainers(getPortalContainer());
    }

    @Override
    public void onInteraction(SlashSender sender) {
    }

    private SubCommandContainer getPortalContainer() {
        return new SubCommandContainer("portal", "Configure the portal channel.",
                new OptionData(OptionType.CHANNEL, "portal", "The channel where tickets are created.", true)) {
            @Override
            public void onInteraction(SlashSender sender) {

            }
        };
    }
}
