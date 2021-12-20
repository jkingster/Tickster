package io.jking.tickster.commands.utility;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.core.Error;
import io.jking.tickster.interaction.core.impl.SlashContext;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.MiscUtil;
import net.dv8tion.jda.api.utils.TimeUtil;

import java.time.format.DateTimeFormatter;

public class SnowflakeCommand extends AbstractCommand {

    public SnowflakeCommand() {
        super("snowflake", "Get information about a snowflake.", CommandCategory.UTILITY);
        addOption(OptionType.STRING, "snowflake", "The snowflake id.", true);
    }

    @Override
    public void onSlashCommand(SlashContext context) {
        final String snowflakeString = context.getStringOption("snowflake");
        if (snowflakeString == null) {
            context.replyErrorEphemeral(Error.ARGUMENTS, this.getName()).queue();
            return;
        }


        try {
            final long parsedSnowflake = MiscUtil.parseSnowflake(snowflakeString);
            final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yy HH:mm a");
            final String timestamp = TimeUtil.getTimeCreated(parsedSnowflake).format(dateTimeFormatter);

            final EmbedBuilder embedBuilder = new EmbedBuilder().setColor(EmbedUtil.SECONDARY)
                    .setAuthor("Snowflake Information")
                    .addField("Snowflake", String.format("**ID:** `%s`", parsedSnowflake), true)
                    .addField("Time Created", String.format("`%s`", timestamp), true);

            context.replyEphemeral(embedBuilder).queue();
        } catch (Exception e) {
            context.replyErrorEphemeral(Error.CUSTOM, "Could not parse that snowflake, provide a valid one!").queue();
        }
    }
}
