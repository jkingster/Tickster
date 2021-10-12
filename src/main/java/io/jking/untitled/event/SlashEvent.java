package io.jking.untitled.event;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import io.jking.untitled.command.CommandRegistry;
import io.jking.untitled.core.Config;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashEvent extends ListenerAdapter {

    private final CommandRegistry registry;

    private final Config config;

    public SlashEvent(CommandRegistry registry, Config config) {
        this.registry = registry;
        this.config = config;
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getUser().isBot())
            return;

        if (!event.isFromGuild())
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

        command.onCommand(new CommandContext(event, config));
    }

    private boolean isOwner(long targetId) {
        return config.getObject("bot")
                .getArray("owner_ids")
                .toList()
                .stream()
                .anyMatch(id -> (long) id == targetId);
    }
}
