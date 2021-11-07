package io.jking.tickster.handlers;

import io.jking.tickster.commands.utility.TestCommand;
import io.jking.tickster.objects.command.Command;
import io.jking.tickster.objects.command.CommandContext;
import io.jking.tickster.objects.command.CommandError;
import io.jking.tickster.objects.command.CommandRegistry;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;


public class InteractionHandler implements EventListener {

    private final Permission[] requiredPermissions = {
            Permission.MANAGE_CHANNEL, Permission.MANAGE_SERVER,
            Permission.MESSAGE_WRITE, Permission.MANAGE_PERMISSIONS
    };

    private final CommandRegistry commandRegistry = new CommandRegistry()
            .addCommands(new TestCommand());

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof SlashCommandEvent)
            onSlashCommand((SlashCommandEvent) event);
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

        final CommandContext commandContext = new CommandContext(event);
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
