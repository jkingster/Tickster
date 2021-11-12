package io.jking.tickster.handler;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.RegistryHandler;
import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.context.CommandContext;
import io.jking.tickster.interaction.impl.slash.impl.utility.HelpCommand;
import io.jking.tickster.interaction.impl.slash.object.Command;
import io.jking.tickster.interaction.impl.slash.object.CommandError;
import io.jking.tickster.interaction.impl.slash.object.CommandRegistry;
import io.jking.tickster.interaction.impl.slash.object.type.ErrorType;
import io.jking.tickster.interaction.type.IButton;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenuInteraction;
import org.jetbrains.annotations.NotNull;


public class InteractionHandler implements EventListener {

    private final Permission[] requiredPermissions = {
            Permission.MANAGE_CHANNEL, Permission.MANAGE_SERVER,
            Permission.MESSAGE_WRITE, Permission.MANAGE_PERMISSIONS
    };

    private final CommandRegistry commandRegistry;
    private final RegistryHandler handler;
    private final Database database;
    private final Cache cache;

    public InteractionHandler(CommandRegistry registry, Database database, Cache cache) {
        this.commandRegistry = registry;
        this.handler = new RegistryHandler().registerButtons();
        registry.addCommand(new HelpCommand(registry));
        this.database = database;
        this.cache = cache;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof SlashCommandEvent)
            onSlashCommand((SlashCommandEvent) event);
        else if (event instanceof ButtonInteraction)
            onButtonInteraction((ButtonClickEvent) event);
        else if (event instanceof SelectionMenuInteraction)
            onSelectionInteraction((SelectionMenuEvent) event);

    }

    private void onSelectionInteraction(SelectionMenuEvent event) {
    }

    private void onButtonInteraction(ButtonClickEvent event) {
        final IButton button = handler.getButton(event.getComponentId());
        if (button == null)
            return;

        button.onInteraction(new ButtonContext(event, database, cache));
    }

    private void onSlashCommand(SlashCommandEvent event) {
        if (!event.isFromGuild())
            return;

        final Guild guild = event.getGuild();
        if (guild == null)
            return;

        final Member member = event.getMember();
        if (member == null)
            return;

        final Member self = event.getGuild().getSelfMember();
        if (!self.hasPermission(requiredPermissions)) {
            sendPrivateMessage(member);
            return;
        }

        final Command command = commandRegistry.getCommand(event.getName());
        if (command == null)
            return;

        if (!member.hasPermission(command.getPermission())) {
            event.replyEmbeds(EmbedFactory.getError(
                    ErrorType.PERMISSION,
                    member.getUser().getAsTag(),
                    command.getPermission())
                    .build())
                    .setEphemeral(true)
                    .queue();
            return;
        }

        final CommandContext commandContext = new CommandContext(event, database, cache);
        final CommandError errorContext = new CommandError(commandContext);

        command.onCommand(commandContext, errorContext);
    }

    private void sendPrivateMessage(Member member) {
        if (member == null)
            return;

        member.getUser().openPrivateChannel()
                .flatMap(privateChannel -> privateChannel.sendMessage("I am missing required permission(s) to function properly in this server."))
                .queue();
    }
}
