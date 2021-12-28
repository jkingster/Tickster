package io.jking.tickster.event;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.button.ButtonRegistry;
import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.Error;
import io.jking.tickster.interaction.core.impl.ButtonContext;
import io.jking.tickster.interaction.core.impl.SlashContext;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.utility.EmbedUtil;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class InteractionEvent implements EventListener {

    private final CommandRegistry commandRegistry;
    private final ButtonRegistry buttonRegistry;
    private final Database database;
    private final CacheManager cache;

    public InteractionEvent(CommandRegistry commandRegistry, ButtonRegistry buttonRegistry, Database database, CacheManager cache) {
        this.commandRegistry = commandRegistry;
        this.buttonRegistry = buttonRegistry;
        this.database = database;
        this.cache = cache;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof SlashCommandEvent)
            onSlashCommand((SlashCommandEvent) event);
        else if (event instanceof ButtonClickEvent)
            onButtonClick((ButtonClickEvent) event);
    }

    private void onButtonClick(ButtonClickEvent event) {
        final Guild guild = event.getGuild();
        if (guild == null)
            return;

        final Member member = event.getMember();
        if (member == null)
            return;

        String buttonId = event.getComponentId();
        if (buttonId.contains("id")) {
            final String[] splitId = buttonId.split(":");
            final int length = splitId.length;
            final String id = splitId[length - 1];
            if (!id.equalsIgnoreCase(member.getId()))
                return;

            buttonId = buttonId.replace(id, "%s");
        }

        final AbstractButton button = buttonRegistry.get(buttonId);
        if (button == null)
            return;

        button.onButtonPress(new ButtonContext(event, database, cache));
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

        if (category == CommandCategory.TICKET_MANAGEMENT && !member.hasPermission(Permission.ADMINISTRATOR)) {
            final long guildId = event.getGuild().getIdLong();
            final GuildDataRecord record = cache.getGuildCache().fetchOrGet(guildId);

            if (record == null) {
                event.replyEmbeds(EmbedUtil.getError(Error.UNKNOWN).build())
                        .setEphemeral(true)
                        .queue();
                return;
            }

            final long supportId = record.getSupportId();
            final Role supportRole = event.getGuild().getRoleById(supportId);

            if (supportRole == null) {
                event.replyEmbeds(EmbedUtil.getError(Error.CUSTOM, "The ticket support role is not configured. You cannot utilize management commands until the role is set.").build())
                        .setEphemeral(true)
                        .queue();
                return;
            }

            if (!MiscUtil.hasRole(member, supportRole.getIdLong())) {
                event.replyEmbeds(EmbedUtil.getError(Error.CUSTOM, "You lack the Ticket Support role. You cannot utilize these commands.").build())
                        .setEphemeral(true)
                        .queue();
                return;
            }
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
