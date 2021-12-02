package io.jking.tickster.command.impl.bot_owner;

import io.jking.tickster.command.Command;
import io.jking.tickster.command.CommandContext;
import io.jking.tickster.command.CommandError;

public class UpdateCommand extends Command {
    public UpdateCommand() {
        super("Update", "Update Commands", null);
        setDefaultEnabled(false);
    }

    @Override
    public void onCommand(CommandContext ctx, CommandError err) {

    }
}
