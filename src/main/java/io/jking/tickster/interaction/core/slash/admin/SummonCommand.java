package io.jking.tickster.interaction.core.slash.admin;

import io.jking.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.interaction.impl.container.SlashContainer;
import io.jking.tickster.interaction.impl.sender.SlashSender;
import io.jking.tickster.interaction.response.Failure;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;


public class SummonCommand extends SlashContainer {
    public SummonCommand() {
        super("summon", "Summons the ticket portal message in the channel that is configured.");
        super.getData().setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        super.getData().setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    @Override
    public void onInteraction(SlashSender sender) {
        final long guildId = sender.getGuild().getIdLong();
        final GuildDataRecord data = sender.getGuildCache().get(guildId);
        if (data == null) {
            sender.reply(Failure.RETRIEVE).setEphemeral(true).queue();
            return;
        }

        final long portalChannelId = data.getTicketCreationChannelId();
        if (portalChannelId == 0L) {
            sender.reply(Failure.CUSTOM, "Portal channel not set.").setEphemeral(true).queue();
            return;
        }

        final TextChannel channel = sender.getGuild().getTextChannelById(portalChannelId);
        if (channel == null) {
            sender.reply(Failure.CUSTOM, "Could not retrieve portal channel.").setEphemeral(true).queue();
            return;
        }

        if (!channel.canTalk()) {
            sender.reply(Failure.PERMISSION, Permission.MESSAGE_SEND).setEphemeral(true).queue();
            return;
        }


        sender.deferReply(true).queue(deferred -> {
            final String avatarUrl  = sender.getSelfUser().getDefaultAvatarUrl();
            final EmbedBuilder portalEmbed = new EmbedBuilder()
                    .setColor(Color.CYAN.brighter())
                    .setTitle("Ticket Portal")
                    .setThumbnail(avatarUrl)
                    .setDescription("To create a ticket, click the **button** below to get started.")
                    .setFooter("Please note: You can only have one ticket open at a time per-server.");

            final Button button = Button.secondary("create_ticket", "Create Ticket").withEmoji(Emoji.fromUnicode("\uD83C\uDFAB"));

            channel.sendMessageEmbeds(portalEmbed.build()).setActionRow(button)
                    .flatMap(success -> deferred.editOriginal("Portal message summoned: " + success.getJumpUrl()))
                    .queue();
        });
    }
}
