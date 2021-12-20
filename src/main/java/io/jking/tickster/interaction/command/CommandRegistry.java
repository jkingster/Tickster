package io.jking.tickster.interaction.command;

import io.jking.tickster.commands.bot_owner.UpdateCommand;
import io.jking.tickster.commands.info.AboutCommand;
import io.jking.tickster.commands.info.InfoCommand;
import io.jking.tickster.commands.utility.SnowflakeCommand;
import io.jking.tickster.interaction.core.Registry;

public class CommandRegistry extends Registry<AbstractCommand> {

    public CommandRegistry() {
        put("update", new UpdateCommand(this));
        put("info", new InfoCommand());
        put("about", new AboutCommand());
        put("snowflake", new SnowflakeCommand());
    }

}
