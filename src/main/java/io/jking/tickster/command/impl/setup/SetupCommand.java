package io.jking.tickster.command.impl.setup;

import io.jking.tickster.command.Category;
import io.jking.tickster.command.Command;
import io.jking.tickster.command.CommandContext;
import io.jking.tickster.command.CommandError;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.command.type.SuccessType;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import static io.jking.untitled.jooq.tables.GuildData.GUILD_DATA;

public class SetupCommand extends Command {

    public SetupCommand() {
        super("setup", "Configure settings for this server.", Category.SETUP, Permission.ADMINISTRATOR);
        addSubcommands(
                new SubcommandData("ticket_manager", "The configured ticket manager role.")
                        .addOption(OptionType.ROLE, "ticket_role", "The mentioned role."),

                new SubcommandData("ticket_channel", "The configured ticket channel.")
                        .addOption(OptionType.CHANNEL, "ticket_channel", "The mentioned channel."),

                new SubcommandData("log_channel", "The configured logs channel.")
                        .addOption(OptionType.CHANNEL, "log_channel", "The mentioned channel.")
        );
    }

    @Override
    public void onCommand(CommandContext ctx, CommandError err) {
        final String subCommand = ctx.getSubCommand();

        switch (subCommand.toLowerCase()) {
            case "ticket_manager" -> setupTicketManager(ctx, err);
            case "ticket_channel" -> setupTicketChannel(ctx, err);
            case "log_channel" -> setupLogChannel(ctx, err);
        }
    }

    private void setupTicketManager(CommandContext ctx, CommandError err) {
        final Role role = ctx.getOptionRole("ticket_role");
        if (role == null || !ctx.canInteract(role)) {
            err.reply(ErrorType.CANT_INTERACT, ctx.getSelf().getAsTag(), "the mentioned role.");
            return;
        }

        final long guildId = ctx.getGuild().getIdLong();

        ctx.getGuildCache().update(guildId, GUILD_DATA.TICKET_MANAGER, role.getIdLong(), (unused, error) -> {
            if (error != null) {
                err.reply(ErrorType.UNKNOWN, "Could not update the ticket manager role!");
                return;
            }

            ctx.replySuccess(SuccessType.UPDATED, "The ticket manager role", role.getId());
        });
    }

    private void setupTicketChannel(CommandContext ctx, CommandError err) {
        final TextChannel channel = ctx.getOptionChannel("ticket_channel");
        if (cantAccess(channel, ctx.getSelf(), err))
            return;

        final long guildId = ctx.getGuild().getIdLong();
        ctx.getGuildCache().update(guildId, GUILD_DATA.TICKET_CHANNEL, channel.getIdLong(), (unused, error) -> {
            if (error != null) {
                err.reply(ErrorType.UNKNOWN, "Could not update the ticket channel!");
                return;
            }

            ctx.replySuccess(SuccessType.UPDATED, "The ticket channel", channel.getId());
            createTicketInput(ctx, channel);
        });
    }

    private void setupLogChannel(CommandContext ctx, CommandError err) {
        final TextChannel channel = ctx.getOptionChannel("log_channel");
        if (cantAccess(channel, ctx.getSelf(), err))
            return;

        final long guildId = ctx.getGuild().getIdLong();
        ctx.getGuildCache().update(guildId, GUILD_DATA.LOG_CHANNEL, channel.getIdLong(), (unused, error) -> {
            if (error != null) {
                err.reply(ErrorType.UNKNOWN, "Could not update the log channel!");
                return;
            }

            ctx.replySuccess(SuccessType.UPDATED, "The log channel", channel.getId());
        });
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
                .setActionRow(Button.of(ButtonStyle.SECONDARY, "create_ticket", "Create Ticket", Emoji.fromUnicode("\uD83C\uDFAB")))
                .queue();
    }
}
