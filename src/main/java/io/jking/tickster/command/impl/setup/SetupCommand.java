package io.jking.tickster.command.impl.setup;

import io.jking.tickster.command.Category;
import io.jking.tickster.command.Command;
import io.jking.tickster.command.CommandContext;
import io.jking.tickster.command.CommandError;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.command.type.SuccessType;
import io.jking.tickster.object.CButton;
import io.jking.tickster.utility.EmbedFactory;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import static io.jking.tickster.jooq.tables.GuildData.GUILD_DATA;

public class SetupCommand extends Command {

    public SetupCommand() {
        super("setup", "Configure settings for this server.", Category.SETUP, Permission.ADMINISTRATOR);
        addSubcommands(
                new SubcommandData("ticket_manager", "The configured ticket manager role.")
                        .addOption(OptionType.ROLE, "ticket_role", "The mentioned role."),

                new SubcommandData("ticket_channel", "The configured ticket channel.")
                        .addOption(OptionType.CHANNEL, "ticket_channel", "The mentioned channel."),

                new SubcommandData("log_channel", "The configured logs channel.")
                        .addOption(OptionType.CHANNEL, "log_channel", "The mentioned channel."),

                new SubcommandData("ticket_category", "The category where tickets will be created under.")
                        .addOption(OptionType.STRING, "category", "The mentioned category."),

                new SubcommandData("report_channel", "The channel where reports are sent.")
                        .addOption(OptionType.CHANNEL, "report_channel", "The mentioned report channel.")
        );
    }

    @Override
    public void onCommand(CommandContext ctx, CommandError err) {
        final String subCommand = ctx.getSubCommand();

        switch (subCommand.toLowerCase()) {
            case "ticket_manager" -> setupTicketManager(ctx, err);
            case "ticket_channel" -> setupTicketChannel(ctx, err);
            case "log_channel" -> setupLogChannel(ctx, err);
            case "ticket_category" -> setupTicketCategory(ctx, err);
            case "report_channel" -> setupReportChannel(ctx, err);
        }
    }

    private void setupReportChannel(CommandContext ctx, CommandError err) {
        final TextChannel channel = ctx.getOptionChannel("report_channel");
        if (cantAccess(channel, ctx.getSelf(), err))
            return;

        final long guildId = ctx.getGuild().getIdLong();
        ctx.getGuildCache().update(guildId, GUILD_DATA.REPORT_CHANNEL, channel.getIdLong(),
                (unused) -> ctx.sendSuccess(SuccessType.UPDATED, true, "The report channel", channel.getId()),
                (error) -> err.reply(ErrorType.CUSTOM, "Could not update report channel."));
    }

    private void setupTicketCategory(CommandContext ctx, CommandError err) {
        final long guildId = ctx.getGuild().getIdLong();
        final String categoryId = ctx.getOptionString("category");
        if (MiscUtil.containsAnyOption(categoryId, "0", "none")) {
            ctx.getGuildCache().update(guildId, GUILD_DATA.TICKET_CATEGORY, 0L,
                    (unused) -> ctx.sendSuccess(SuccessType.UPDATED, true, "The ticket category", 0),
                    (error) -> err.reply(ErrorType.CUSTOM, "Could not update ticket category.")
            );
            return;
        }

        if (categoryId == null || !MiscUtil.isSnowflake(categoryId)) {
            err.reply(ErrorType.INVALID_ID);
            return;
        }

        final net.dv8tion.jda.api.entities.Category category = ctx.getGuild().getCategoryById(categoryId);
        if (category == null) {
            err.reply(ErrorType.CUSTOM, "The ID you provided was not a category.");
            return;
        }

        ctx.getGuildCache().update(guildId, GUILD_DATA.TICKET_CATEGORY, category.getIdLong(),
                (unused) -> ctx.sendSuccess(SuccessType.UPDATED, true, "The ticket category", category.getIdLong()),
                (error) -> err.reply(ErrorType.CUSTOM, "Could not update ticket category.")
        );
    }

    private void setupTicketManager(CommandContext ctx, CommandError err) {
        final Role role = ctx.getOptionRole("ticket_role");
        if (role == null || !ctx.canInteract(role)) {
            err.reply(ErrorType.CANT_INTERACT, ctx.getSelf().getAsTag(), "the mentioned role.");
            return;
        }

        final long guildId = ctx.getGuild().getIdLong();

        ctx.getGuildCache().update(guildId, GUILD_DATA.TICKET_MANAGER, role.getIdLong(),
                (unused) -> ctx.sendSuccess(SuccessType.UPDATED, true, "The ticket manager role", role.getId()),
                (error) -> err.reply(ErrorType.CUSTOM, "Could not update ticket manager.")
        );
    }


    private void setupTicketChannel(CommandContext ctx, CommandError err) {
        final TextChannel channel = ctx.getOptionChannel("ticket_channel");
        if (cantAccess(channel, ctx.getSelf(), err))
            return;

        final long guildId = ctx.getGuild().getIdLong();
        ctx.getGuildCache().update(guildId, GUILD_DATA.TICKET_CHANNEL, channel.getIdLong(),
                (unused) -> {
                    ctx.sendSuccess(SuccessType.UPDATED, true, "The ticket channel", channel.getId());
                    createTicketInput(ctx, channel);
                }, (error) -> err.reply(ErrorType.CUSTOM, "Could not update ticket channel."));
    }

    private void setupLogChannel(CommandContext ctx, CommandError err) {
        final TextChannel channel = ctx.getOptionChannel("log_channel");
        if (cantAccess(channel, ctx.getSelf(), err))
            return;

        final long guildId = ctx.getGuild().getIdLong();
        ctx.getGuildCache().update(guildId, GUILD_DATA.LOG_CHANNEL, channel.getIdLong(),
                (unused) -> ctx.sendSuccess(SuccessType.UPDATED, true, "The log channel", channel.getId()),
                (error) -> err.reply(ErrorType.CUSTOM, "Could not update log channel.")
        );
    }

    private boolean cantAccess(TextChannel channel, User user, CommandError error) {
        if (channel == null || !channel.canTalk()) {
            error.reply(ErrorType.CANT_ACCESS, user.getAsTag(), "the mentioned channel.");
            return true;
        }
        return false;
    }

    private void createTicketInput(CommandContext ctx, TextChannel channel) {
        final EmbedBuilder embed = EmbedFactory.getDefault()
                .setDescription("To **create** a ticket, click the button below.\nPlease also consider using `/ticket create` for convenience.")
                .setFooter("Tickster • Easy ticket management and user reporting.", ctx.getSelf().getEffectiveAvatarUrl());

        channel.sendMessageEmbeds(embed.build())
                .setActionRow(CButton.SECONDARY.format("create_ticket", "Create Ticket", Emoji.fromUnicode("\uD83C\uDFAB")))
                .queue();
    }
}
