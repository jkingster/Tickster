package io.jking.tickster.event;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.core.Tickster;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.button.ButtonRegistry;
import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandFlag;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.impl.ButtonSender;
import io.jking.tickster.interaction.core.impl.SelectSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.select.AbstractSelect;
import io.jking.tickster.interaction.select.SelectRegistry;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class InteractionEvent implements EventListener {

    private final Tickster tickster;
    private final CommandRegistry commandRegistry;
    private final ButtonRegistry buttonRegistry;
    private final SelectRegistry selectRegistry;
    private final Database database;
    private final CacheManager cache;

    public InteractionEvent(Tickster tickster,
                            CommandRegistry commandRegistry,
                            ButtonRegistry buttonRegistry,
                            SelectRegistry selectRegistry,
                            Database database,
                            CacheManager cache) {
        this.tickster = tickster;
        this.commandRegistry = commandRegistry;
        this.buttonRegistry = buttonRegistry;
        this.selectRegistry = selectRegistry;
        this.database = database;
        this.cache = cache;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof SlashCommandInteractionEvent)
            onSlashCommand((SlashCommandInteractionEvent) event);
        else if (event instanceof ButtonInteractionEvent)
            onButtonClick((ButtonInteractionEvent) event);
        else if (event instanceof SelectMenuInteractionEvent)
            onSelectMenu((SelectMenuInteractionEvent) event);
    }

    private void onButtonClick(ButtonInteractionEvent event) {
        final Guild guild = event.getGuild();
        if (guild == null)
            return;

        final long guildId = guild.getIdLong();
        if (cache.getBlacklistCache().isBlacklisted(guildId))
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

        button.onButtonPress(new ButtonSender(tickster, event));
        buttonRegistry.incrementUses();
    }

    private void onSlashCommand(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild())
            return;

        final Guild guild = event.getGuild();
        if (guild == null)
            return;

        final long guildId = guild.getIdLong();
        if (cache.getBlacklistCache().isBlacklisted(guildId))
            return;

        final Member member = event.getMember();
        if (member == null)
            return;

        final String commandName = event.getName();
        final AbstractCommand command = commandRegistry.get(commandName);
        if (command == null)
            return;

        final CommandFlag[] flags = command.getFlags();
        // TODO

        final Permission requiredPermission = command.getPermission();
        final Member self = guild.getSelfMember();
        if (!self.hasPermission(requiredPermission)) {
            event.replyEmbeds(EmbedUtil.getError(
                    Error.PERMISSION,
                    self.getUser().getAsTag(),
                    requiredPermission
            ).build()).queue();
            return;
        }

        if (!member.hasPermission(requiredPermission)) {
            event.replyEmbeds(EmbedUtil.getError(
                    Error.PERMISSION,
                    member.getUser().getAsTag(),
                    requiredPermission
            ).build()).queue();
            return;
        }

//        command.onSlashCommand(new SlashSender(tickster, event, flag == CommandFlag.EPHEMERAL));
    }

    private void onSelectMenu(SelectMenuInteractionEvent event) {
        final Guild guild = event.getGuild();
        if (guild == null)
            return;

        final String componentId = event.getComponentId();
        final AbstractSelect abstractSelect = selectRegistry.get(componentId);
        if (abstractSelect == null)
            return;

        abstractSelect.onSelectPress(new SelectSender(tickster, event));
        selectRegistry.incrementUses();
    }

    private boolean isDeveloper(long memberId, long... developerIds) {
        for (long id : developerIds) {
            if (id == memberId)
                return true;
        }
        return false;
    }

}
