package io.jking.tickster.interaction.core.slash.admin;

import io.jking.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.database.impl.GuildRepo;
import io.jking.tickster.interaction.impl.container.SlashContainer;
import io.jking.tickster.interaction.impl.sender.SlashSender;
import io.jking.tickster.interaction.response.Failure;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SetupCommand extends SlashContainer {

    private final GuildRepo guildRepo = GuildRepo.getInstance();

    public SetupCommand() {
        super("setup", "Configure the settings for this server.", Permission.ADMINISTRATOR);
        super.getData().setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        super.putSubContainers(getPortalContainer());
    }

    @Override
    public void onInteraction(SlashSender sender) {
    }

    private SubCommandContainer getLoggingContainer() {
        return new SubCommandContainer("logs", "Configure the logging channel.",
                new OptionData(OptionType.CHANNEL, "logs", "The channel where logs are sent.", true)) {
            @Override
            public void onInteraction(SlashSender sender) {

            }
        };
    }

    private SubCommandContainer getPortalContainer() {
        return new SubCommandContainer("portal", "Configure the portal channel.",
                new OptionData(OptionType.CHANNEL, "portal", "The channel where tickets are created.", true)) {
            @Override
            public void onInteraction(SlashSender sender) {
                final long guildId = sender.getGuild().getIdLong();
                final GuildDataRecord guildData = sender.getGuildCache().get(guildId);

                if (guildData == null) {
                    sender.reply(Failure.RETRIEVE).setEphemeral(true).queue();
                    return;
                }

                final long portalChannelId = guildData.getTicketCreationChannelId();
                final GuildChannelUnion portalChannelUnion = sender.getChannel("portal");
                if (portalChannelUnion instanceof TextChannel) {
                    final TextChannel portalChannel = portalChannelUnion.asTextChannel();
                    if (!portalChannel.canTalk()) {
                        sender.reply(Failure.PERMISSION, Permission.MESSAGE_SEND).setEphemeral(true).queue();
                        return;
                    }

                    final long channelId = portalChannel.getIdLong();
                    if (portalChannelId == channelId) {
                        sender.reply(Failure.MATCHING).setEphemeral(true).queue();
                        return;
                    }

                    guildRepo.update(guildData.setTicketCreationChannelId(channelId), (updatedRecord, status) -> {
                        if (!status) {
                            sender.reply(Failure.UPDATED).setEphemeral(true).queue();
                            return;
                        }

                        sender.reply(EmbedFactory.getUpdated("Portal Channel Set: %s `(%s)`", portalChannel.getAsMention(), portalChannelId))
                                .setEphemeral(true)
                                .queue();
                    });
                    return;
                }
                sender.reply(Failure.CUSTOM, "Must select a valid TextChannel!").setEphemeral(true).queue();
            }
        };
    }
}
