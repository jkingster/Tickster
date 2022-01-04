package io.jking.tickster.interaction.select.impl;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.impl.SelectSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.select.AbstractSelect;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.Collections;

public class CommandSelect extends AbstractSelect {

    private final CommandRegistry registry;

    public CommandSelect(CommandRegistry registry) {
        super("menu:help_category");
        this.registry = registry;
    }

    @Override
    public void onSelectPress(SelectSender sender) {
        final SelectOption selectOption = sender.getSelectedOption();
        if (selectOption == null) {
            sender.replyErrorEphemeral(
                    Error.UNKNOWN
            ).queue();
            return;
        }

        final String value = selectOption.getValue();
        final String commandName = value.split(":")[1];
        if (commandName == null || commandName.isEmpty()) {
            sender.replyErrorEphemeral(
                    Error.CUSTOM,
                    "Parsing command name error."
            ).queue();
            return;
        }

        final AbstractCommand command = registry.get(commandName);
        if (command == null) {
            sender.replyErrorEphemeral(
                    Error.CUSTOM,
                    "Could not find that command!"
            ).queue();
            return;
        }

        final Member member = sender.getMember();
        if (!MiscUtil.isSupport(sender.getGuildRecord(), member)) {
            sender.replyErrorEphemeral(Error.CUSTOM, "You do not have the support role, you cannot view that command!").queue();
            return;
        }

        if (!member.hasPermission(command.getPermission())) {
            sender.replyErrorEphemeral(
                    Error.PERMISSION,
                    member.getUser().getAsTag(),
                    command.getPermission()
            ).queue();
            return;
        }


        sender.deferEdit().queue(deferred -> sender.getHook().editOriginalEmbeds(command.getAsEmbed().build())
                .setActionRows(Collections.emptyList())
                .queue());
    }

}
