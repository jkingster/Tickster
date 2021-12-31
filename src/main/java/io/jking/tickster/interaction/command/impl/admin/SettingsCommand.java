package io.jking.tickster.interaction.command.impl.admin;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;

import static io.jking.tickster.jooq.tables.GuildData.GUILD_DATA;

public class SettingsCommand extends AbstractCommand {

    public SettingsCommand() {
        super("settings", "Configure settings for this server.", CommandCategory.ADMIN);
        final SubcommandData logChannel = new SubcommandData("logs", "Sets the log channel.")
                .addOption(OptionType.CHANNEL, "log-channel", "Specific channel.", true);

        final SubcommandData supportRole = new SubcommandData("support", "Sets the ticket support role.")
                .addOption(OptionType.ROLE, "support-role", "Specific role.", true);

        final SubcommandData inviteChannel = new SubcommandData("invite", "Sets the invite logging channel.")
                .addOption(OptionType.CHANNEL, "invite-channel", "Specific channel.", true);

        final SubcommandData category = new SubcommandData("category", "Sets the category where tickets are created under.")
                .addOption(OptionType.STRING, "ticket-category", "Category ID/Name.", true);

        addSubCommands(logChannel, supportRole, inviteChannel, category);
    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final String subCommandName = sender.getSubCommandName();
        final long guildId = sender.getGuild().getIdLong();
        switch (subCommandName.toLowerCase()) {
            case "logs" -> setLogChannel(sender, guildId);
            case "support" -> setSupportRole(sender, guildId);
            case "invite" -> setInviteChannel(sender, guildId);
            case "category" -> setTicketCategory(sender, guildId);
        }
    }

    private void setLogChannel(SlashSender sender, long guildId) {
        final TextChannel channel = sender.getChannelOption("log-channel");
        if (channel == null) {
            sender.replyErrorEphemeral(Error.ARGUMENTS, this.getName()).queue();
            return;
        }

        if (!channel.canTalk()) {
            sender.replyErrorEphemeral(Error.CUSTOM, "I cannot talk in that channel, give me permissions and try again!").queue();
            return;
        }

        sender.getGuildCache().update(guildId, GUILD_DATA.LOG_ID, channel.getIdLong());
        sender.replySuccessEphemeral(Success.UPDATE, "Log Channel").queue();
    }

    private void setSupportRole(SlashSender sender, long guildId) {
        final Role role = sender.getRoleOption("support-role");
        if (role == null) {
            sender.replyErrorEphemeral(Error.ARGUMENTS, this.getName());
            return;
        }

        if (!sender.getSelfMember().canInteract(role)) {
            sender.replyErrorEphemeral(Error.CUSTOM, "I cannot interact with that role. Give me permissions and try again!").queue();
            return;
        }

        sender.getGuildCache().update(guildId, GUILD_DATA.SUPPORT_ID, role.getIdLong());
        sender.replySuccessEphemeral(Success.UPDATE, "Ticket Support Role").queue();
    }

    private void setInviteChannel(SlashSender sender, long guildId) {
        final TextChannel channel = sender.getChannelOption("invite-channel");
        if (channel == null) {
            sender.replyErrorEphemeral(Error.ARGUMENTS, this.getName()).queue();
            return;
        }

        if (!channel.canTalk()) {
            sender.replyErrorEphemeral(Error.CUSTOM, "I cannot talk in that channel, give me permissions and try again!").queue();
            return;
        }

        sender.getGuildCache().update(guildId, GUILD_DATA.INVITE_ID, channel.getIdLong());
        sender.replySuccessEphemeral(Success.UPDATE, "Invite Channel").queue();
    }

    private void setTicketCategory(SlashSender sender, long guildId) {
        final String categoryString = sender.getStringOption("ticket-category").toLowerCase();

        if (MiscUtil.isSnowflake(categoryString)) {
            final Category category = sender.getGuild().getCategoryById(categoryString);
            if (category == null) {
                sender.replyErrorEphemeral(Error.ARGUMENTS, this.getName()).queue();
                return;
            }
            updateCategory(sender, guildId, category.getIdLong());
            return;
        }

        final List<Category> categoryList = sender.getGuild().getCategoriesByName(categoryString, true);
        if (categoryList.isEmpty()) {
            sender.replyErrorEphemeral(Error.CUSTOM, "No categories were found with that name!").queue();
            return;
        }

        final Category category = categoryList.get(0);
        updateCategory(sender, guildId, category.getIdLong());
    }

    private void updateCategory(SlashSender sender, long guildId, long categoryId) {
        sender.getGuildCache().update(guildId, GUILD_DATA.CATEGORY_ID, categoryId);
        sender.replySuccessEphemeral(Success.UPDATE, "Ticket Category").queue();
    }

}
