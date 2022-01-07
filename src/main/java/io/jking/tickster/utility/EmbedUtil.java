package io.jking.tickster.utility;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;


import java.awt.*;
import java.time.Instant;
import java.util.List;


public final class EmbedUtil {
    public static Color PRIMARY = Color.decode("#FF7300");
    public static Color SECONDARY = Color.decode("#008DF5").darker();

    private EmbedUtil() {

    }

    public static EmbedBuilder getDefault() {
        return new EmbedBuilder().setColor(PRIMARY);
    }

    public static EmbedBuilder getError(Error error, Object... objects) {
        return new EmbedBuilder().setColor(Color.RED)
                .setTimestamp(Instant.now())
                .setDescription(error.getDesc(objects))
                .setAuthor("Error");
    }

    public static EmbedBuilder getSuccess(Success success, Object... objects) {
        return new EmbedBuilder().setColor(Color.GREEN.darker())
                .setTimestamp(Instant.now())
                .setDescription(success.getDesc(objects))
                .setAuthor("Success");
    }

    public static EmbedBuilder getUserInfo(User user) {
        return new EmbedBuilder().setColor(SECONDARY)
                .setTitle("User Information")
                .setAuthor(user.getAsTag())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setDescription(String.format(
                        """
                        **Creation Date:** `%s`
                        **Account Age:** `%s` days.
                        **ID:** `%s`
                        """,
                        user.getTimeCreated().toLocalDate(),
                        MiscUtil.getDays(user.getTimeCreated()),
                        user.getIdLong())
                );
    }

    public static EmbedBuilder getMemberInfo(Member member) {
        return getUserInfo(member.getUser())
                .appendDescription(String.format(
                        """
                        **Joined** `%s` days ago.
                        **Join Date:** `%s`
                        """,
                        MiscUtil.getDays(member.getTimeJoined()),
                        member.getTimeJoined().toLocalDate())
                );
    }

    public static EmbedBuilder getTicketSummoner(User self) {
        return getDefault().setDescription(
                """
                To **create** a ticket, click the button below.
                Please also consider using `/ticket create` for convenience.
                """
        ).setFooter("Tickster • Easy ticket management.", self.getEffectiveAvatarUrl());
    }

    public static EmbedBuilder getNewTicket(Member member) {
        return getDefault().setDescription(
                """
                Ticket Support has been notified. Please wait patiently, provide any
                necessary details/information.
                """
        ).setTitle(member.getUser().getAsTag() + " Ticket")
                .setFooter("Creator ID: " + member.getIdLong(), member.getUser().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
    }

    public static EmbedBuilder getCategories(List<CommandCategory> categories) {
        return getDefault().setDescription("**Pick any category in the menu.**\n " + CommandCategory.getCategoriesPrinted(categories))
                .setTimestamp(Instant.now());
    }

    public static EmbedBuilder getCommands(CommandCategory category, List<AbstractCommand> commands) {
        return getDefault().setDescription(String.format(
                "**Category:** %s %s\n**Pick any command in the menu to view it.**\n%s",
                category.getEmoji().getAsMention(),
                category.getPrettifiedName(),
                CommandRegistry.getCommandsPrinted(commands))
        ).setTimestamp(Instant.now());
    }

}
