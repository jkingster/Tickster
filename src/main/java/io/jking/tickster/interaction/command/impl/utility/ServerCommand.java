package io.jking.tickster.interaction.command.impl.utility;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandFlag;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class ServerCommand extends AbstractCommand {

    public ServerCommand() {
        super(
                "server",
                "Shows information about the server.",
                CommandCategory.INFO,
                CommandFlag.EPHEMERAL
        );
    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final long guildId = sender.getGuild().getIdLong();
        final long ownerId = sender.getGuild().getOwnerIdLong();
        final int memberCount = sender.getGuild().getMemberCount();
        final int totalBoosts = sender.getGuild().getBoostCount();
        final Guild.BoostTier boostTier = sender.getGuild().getBoostTier();
        final String boostName = Guild.BoostTier.fromKey(boostTier.getKey()).name();

        final String creationTime = sender.getGuild().getTimeCreated()
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("MM/dd/yyy HH:mm a"));

        sender.retrieveMember(ownerId).queue(member -> {
            final EmbedBuilder embed = EmbedUtil.getDefault()
                    .setTimestamp(Instant.now())
                    .setThumbnail(sender.getGuild().getIconUrl())
                    .setDescription(String.format(
                            """
                            **Server ID:** `%s`
                            **Owner:** `%s | %s`
                            **Member Count (approx.):** `%s`
                            **Total Boosts:** `%s`
                            **Boost Tier:** `%s`
                            **Server Created:** `%s`
                            """,
                            guildId, member.getUser().getAsTag(), ownerId,
                            memberCount, totalBoosts, boostName, creationTime)
                    );

            sender.reply(embed).queue();
        }, error -> sender.reply(Error.UNKNOWN).queue());
    }
}
