package io.jking.tickster.event;

import io.jking.tickster.database.impl.GuildRepo;
import io.jking.tickster.interaction.InteractionRegistry;
import io.jking.tickster.interaction.impl.container.ButtonContainer;
import io.jking.tickster.interaction.impl.container.SlashContainer;
import io.jking.tickster.interaction.impl.sender.ButtonSender;
import io.jking.tickster.interaction.impl.sender.SlashSender;
import io.jking.tickster.utility.MiscUtil;
import io.r2dbc.spi.Parameter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class InteractionEvent implements EventListener {

    private final GuildRepo                                     guildRepo = GuildRepo.getInstance();
    private final InteractionRegistry.Registry<ButtonContainer> buttonRegistry;
    private final InteractionRegistry.Registry<SlashContainer>  slashRegistry;

    public InteractionEvent(InteractionRegistry interactionRegistry) {
        this.buttonRegistry = interactionRegistry.getButtonMap();
        this.slashRegistry = interactionRegistry.getSlashMap();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GenericInteractionCreateEvent) {
            final GenericInteractionCreateEvent casted = (GenericInteractionCreateEvent) event;
            Guild guild = casted.getGuild();
            if (guild == null)
                return;

            Member member = casted.getMember();
            if (member == null)
                return;

            if (casted instanceof ButtonInteractionEvent)
                onButtonInteraction((ButtonInteractionEvent) event);

            if (casted instanceof SlashCommandInteractionEvent)
                onSlashInteraction((SlashCommandInteractionEvent) event);
        }
    }

    private void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        ButtonContainer buttonContainer = buttonRegistry.get(buttonId);
        if (buttonContainer == null)
            return;

        if (buttonContainer.requiresTicketMaster()) {
            long ticketMasterId = guildRepo.retrieve(event.getGuild().getIdLong()).getTicketMasterRoleId();
            if (ticketMasterId == 0L)
                return;

            Member member = event.getMember();
            if (MiscUtil.hasRole(member, ticketMasterId)) {
                buttonContainer.onInteraction(new ButtonSender(event));
                return;
            }
        }

        buttonContainer.onInteraction(new ButtonSender(event));
    }

    private void onSlashInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        SlashContainer slashContainer = slashRegistry.get(commandName);
        if (slashContainer == null)
            return;

        String subCommandName = event.getSubcommandName();
        if (subCommandName == null) {
            slashContainer.onInteraction(new SlashSender(event));
            return;
        }

        SlashContainer.SubCommandContainer subCommand = slashContainer.getSubContainer(subCommandName);
        if (subCommand == null)
            return;

        subCommand.onInteraction(new SlashSender(event));
    }
}
