package io.jking.untitled.event;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import io.jking.untitled.command.CommandRegistry;
import io.jking.untitled.core.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.jetbrains.annotations.NotNull;

public class SlashEvent extends ListenerAdapter {

    private final CommandRegistry registry;

    private final Config config;

    private final MessageEvent messageEvent;

    public SlashEvent(CommandRegistry registry, Config config, MessageEvent event) {
        this.registry = registry;
        this.config = config;
        this.messageEvent = event;
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (!event.isFromGuild()) {
            sendPrivateMessage(event.getUser(), "This bot does not process commands in private messages.");
            return;
        }

        final Guild guild = event.getGuild();
        if (guild == null)
            return;

        if (event.getUser().isBot())
            return;

        final Member member = event.getMember();
        if (member == null)
            return;

        final String commandName = event.getName();
        final Command command = registry.getCommand(commandName);

        if (command == null)
            return;

        if (command.getCategory() == Category.BOT_OWNER && !isOwner(member.getIdLong())) {
            // TODO: Implement _error_ handling and respond with an error that member has no access.
            return;
        }

        final Member self = guild.getSelfMember();
        final Permission permission = command.getPermission();

        if (!self.hasPermission(permission)) {
            sendPrivateMessage(member.getUser(), "I do not have the necessary permission(s) to execute this command.");
            return;
        }

        if (!member.hasPermission(permission)) {
            sendPrivateMessage(member.getUser(), "You do not have the necessary permission(s) to execute that command.");
            return;
        }

        command.onCommand(new CommandContext(event, config, messageEvent));
    }

    private void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .queue(null, new ErrorHandler().ignore(ErrorResponse.CANNOT_SEND_TO_USER));
    }

    private boolean isOwner(long targetId) {
        return config.getObject("bot")
                .getArray("owner_ids")
                .toList()
                .stream()
                .anyMatch(id -> (long) id == targetId);
    }
}
