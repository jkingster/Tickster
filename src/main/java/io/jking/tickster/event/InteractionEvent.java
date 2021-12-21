package io.jking.tickster.event;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.Error;
import io.jking.tickster.interaction.core.impl.SlashContext;
import io.jking.tickster.utility.EmbedUtil;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class InteractionEvent implements EventListener {

    private final CommandRegistry commandRegistry;
    private final Database database;
    private final CacheManager cache;

    public InteractionEvent(CommandRegistry commandRegistry, Database database, CacheManager cache) {
        this.commandRegistry = commandRegistry;
        this.database = database;
        this.cache = cache;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof SlashCommandEvent)
            onSlashCommand((SlashCommandEvent) event);
    }

    private void onSlashCommand(SlashCommandEvent event) {
        if (!event.isFromGuild()) {
            final User user = event.getUser();
            MiscUtil.sendPrivateMessage(user, "I do not operate in private messages. I only work in servers!");
            return;
        }

        final Guild guild = event.getGuild();
        if (guild == null)
            return;

        final Member member = event.getMember();
        if (member == null)
            return;

        final TextChannel channel = event.getTextChannel();
        if (!channel.canTalk()) {
            final User user = event.getUser();
            MiscUtil.sendPrivateMessage(user, "I cannot talk in that channel!");
            return;
        }

        final String commandName = event.getName();
        final AbstractCommand command = commandRegistry.get(commandName);

        if (command == null)
            return;

        final CommandCategory category = command.getCategory();
        if (category == CommandCategory.BOT_OWNER) {

            final long ownerId = category.getUserId();
            if (ownerId != member.getIdLong()) {
                event.replyEmbeds(EmbedUtil.getError(Error.ACCESS).build())
                        .setEphemeral(true)
                        .queue();
                return;
            }
        }

        if (category == CommandCategory.TICKET_MANAGEMENT) {
            // TODO: Check if member has ticket manager role, if not early return.
        }

        final Permission permission = command.getPermission();
        if (!member.hasPermission(permission)) {
            event.replyEmbeds(EmbedUtil.getError(Error.PERMISSION, member.getUser().getAsTag(), permission).build())
                    .setEphemeral(true)
                    .queue();
            return;
        }

        final Member self = guild.getSelfMember();
        if (!self.hasPermission(permission)) {
            event.replyEmbeds(EmbedUtil.getError(Error.PERMISSION, self.getUser().getAsTag(), permission).build())
                    .setEphemeral(true)
                    .queue();
            return;
        }

        command.onSlashCommand(new SlashContext(event, database, cache));
    }
}
