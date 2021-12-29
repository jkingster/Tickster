package io.jking.tickster.interaction.command.impl.admin;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import io.jking.tickster.interaction.core.impl.SlashSender;
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

        addSubcommands(logChannel, supportRole, inviteChannel, category);
    }

    @Override
    public void onSlashCommand(SlashSender context) {
        final String subCommandName = context.getSubCommandName();
        final long guildId = context.getGuild().getIdLong();
        switch (subCommandName.toLowerCase()) {
            case "logs" -> setLogChannel(context, guildId);
            case "support" -> setSupportRole(context, guildId);
            case "invite" -> setInviteChannel(context, guildId);
            case "category" -> setTicketCategory(context, guildId);
        }
    }

    private void setLogChannel(SlashSender context, long guildId) {
        final TextChannel channel = context.getChannelOption("log-channel");
        if (channel == null) {
            context.replyErrorEphemeral(Error.ARGUMENTS, this.getName()).queue();
            return;
        }

        if (!channel.canTalk()) {
            context.replyErrorEphemeral(Error.CUSTOM, "I cannot talk in that channel, give me permissions and try again!").queue();
            return;
        }

        context.getGuildCache().update(guildId, GUILD_DATA.LOG_ID, channel.getIdLong());
        context.replySuccessEphemeral(Success.UPDATE, "Log Channel").queue();
    }

    private void setSupportRole(SlashSender context, long guildId) {
        final Role role = context.getRoleOption("support-role");
        if (role == null) {
            context.replyErrorEphemeral(Error.ARGUMENTS, this.getName());
            return;
        }

        if (!context.getSelfMember().canInteract(role)) {
            context.replyErrorEphemeral(Error.CUSTOM, "I cannot interact with that role. Give me permissions and try again!").queue();
            return;
        }

        context.getGuildCache().update(guildId, GUILD_DATA.SUPPORT_ID, role.getIdLong());
        context.replySuccessEphemeral(Success.UPDATE, "Ticket Support Role").queue();
    }

    private void setInviteChannel(SlashSender context, long guildId) {
        final TextChannel channel = context.getChannelOption("invite-channel");
        if (channel == null) {
            context.replyErrorEphemeral(Error.ARGUMENTS, this.getName()).queue();
            return;
        }

        if (!channel.canTalk()) {
            context.replyErrorEphemeral(Error.CUSTOM, "I cannot talk in that channel, give me permissions and try again!").queue();
            return;
        }

        context.getGuildCache().update(guildId, GUILD_DATA.INVITE_ID, channel.getIdLong());
        context.replySuccessEphemeral(Success.UPDATE, "Invite Channel").queue();
    }

    private void setTicketCategory(SlashSender context, long guildId) {
        final String categoryString = context.getStringOption("ticket-category").toLowerCase();

        if (MiscUtil.isSnowflake(categoryString)) {
            final Category category = context.getGuild().getCategoryById(categoryString);
            if (category == null) {
                context.replyErrorEphemeral(Error.ARGUMENTS, this.getName()).queue();
                return;
            }
            updateCategory(context, guildId, category.getIdLong());
            return;
        }

        final List<Category> categoryList = context.getGuild().getCategoriesByName(categoryString, true);
        if (categoryList.isEmpty()) {
            context.replyErrorEphemeral(Error.CUSTOM, "No categories were found with that name!").queue();
            return;
        }

        final Category category = categoryList.get(0);
        updateCategory(context, guildId, category.getIdLong());
    }

    private void updateCategory(SlashSender context, long guildId, long categoryId) {
        context.getGuildCache().update(guildId, GUILD_DATA.CATEGORY_ID, categoryId);
        context.replySuccessEphemeral(Success.UPDATE, "Ticket Category").queue();
    }

}
