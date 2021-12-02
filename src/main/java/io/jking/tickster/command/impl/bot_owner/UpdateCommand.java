package io.jking.tickster.command.impl.bot_owner;

import io.jking.tickster.command.Command;
import io.jking.tickster.command.CommandContext;

public class UpdateCommand extends Command {
    public UpdateCommand() {
        super("Update", "Update Commands", null);
        setDefaultEnabled(false);
    }

    @Override
    public void onCommand(CommandContext ctx) {

    }
}
