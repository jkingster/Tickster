package io.jking.tickster.interaction.command;

import io.jking.tickster.interaction.command.impl.bot_owner.UpdateCommand;
import io.jking.tickster.interaction.command.impl.info.AboutCommand;
import io.jking.tickster.interaction.command.impl.info.InfoCommand;
import io.jking.tickster.interaction.command.impl.ticket_manage.TManageCommand;
import io.jking.tickster.interaction.command.impl.utility.SnowflakeCommand;
import io.jking.tickster.interaction.command.impl.utility.TestCommand;
import io.jking.tickster.interaction.core.Registry;

public class CommandRegistry extends Registry<AbstractCommand> {

    public CommandRegistry() {
        put("update", new UpdateCommand(this));
        put("info", new InfoCommand());
        put("about", new AboutCommand());
        put("snowflake", new SnowflakeCommand());
        put("test", new TestCommand());
        put("tmanage", new TManageCommand());
    }

}
