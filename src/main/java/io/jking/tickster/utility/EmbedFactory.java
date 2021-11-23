package io.jking.tickster.utility;

import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.command.type.SuccessType;
import io.jking.tickster.jooq.tables.records.GuildReportsRecord;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class EmbedFactory {
    private EmbedFactory() {
    }

    private static String getRandomColor() {
        final int random = ThreadLocalRandom.current().nextInt(2) + 1;
        return random == 1 ? "#FF7300" : "#008DF5";
    }

    public static EmbedBuilder getDefault() {
        return new EmbedBuilder()
                .setColor(Color.decode(getRandomColor()));
    }

    public static EmbedBuilder getError(ErrorType errorType, Object... objects) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(String.format("**An error occurred.**\n**Code:** %s\n**Response:** ```%s```",
                        errorType.getName(), errorType.getErrorResponse().formatted(objects)))
                .setTimestamp(Instant.now());
    }

    public static EmbedBuilder getSuccess(SuccessType successType, Object... objects) {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setDescription(String.format("**Action Successful**\n**Action Type:** `%s`\n```%s```",
                        successType.getName(), successType.getSuccessType(objects)))
                .setTimestamp(Instant.now());
    }

    public static EmbedBuilder getNewTicket(User user) {
        return new EmbedBuilder()
                .setColor(Color.decode(getRandomColor()))
                .setAuthor("Ticket Created", null, user.getEffectiveAvatarUrl())
                .setDescription("Please wait for a staff member. In the meantime, please provide any necessary information.")
                .setTimestamp(Instant.now())
                .setFooter("Creator ID: " + user.getId());
    }

    public static EmbedBuilder getSelectionEmbed(Member member, String content) {
        return getDefault()
                .setAuthor(content, null, member.getUser().getEffectiveAvatarUrl())
                .setDescription("Please select an option to continue.");
    }


    public static EmbedBuilder getNewReport(User author) {
        return new EmbedBuilder().setColor(Color.RED).setAuthor(author.getAsTag() + " created a new report!")
                .setDescription("**Click the view report button to see it!**")
                .setTimestamp(Instant.now());
    }

    public static EmbedBuilder getReportedCreated(User author) {
        return new EmbedBuilder().setColor(Color.RED)
                .setDescription(String.format("**%s**: Your report was successfuly created!", author.getAsTag()))
                .setTimestamp(Instant.now());
    }

    public static EmbedBuilder getReport(GuildReportsRecord record, List<Member> reportedUsers, Member creator) {
        final EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.RED)
                .setAuthor("Viewing Report: " + record.getUuid());


        final StringBuilder memberList = new StringBuilder();
        if (reportedUsers.isEmpty()) {
            for (long id : record.getReportedUsers()) {
                memberList.append(id).append("\n");
            }
        } else {
            reportedUsers.forEach(member -> memberList.append(member.getUser().getAsTag()).append("\n"));
        }

        embed.setDescription(String.format("**Reported Member(s):** ```%s```\n**Reason:** ```%s```", memberList, record.getReportReason()))
                .setTimestamp(record.getReportTimestamp());

        if (creator != null) {
            embed.setFooter("Report created by: " + creator.getUser().getAsTag());
        } else {
            embed.setFooter("Report created by: " + record.getIssuerId());
        }

        return embed;
    }

    public static EmbedBuilder getTicket(GuildTicketsRecord record) {
        final EmbedBuilder embed = getDefault().setAuthor("Viewing Ticket: " + record.getChannelId())
                .setTimestamp(record.getTicketTimestamp())
                .setDescription(String.format("**Status:** [%s]", MiscUtil.getStatus(record.getOpen())));

        if (record.getTranscript() == null) {
            embed.appendDescription("\nThere is no transcript to send over..");
        }

        if (!record.getOpen()) {
            embed.setFooter("This ticket is scheduled to be deleted...");
        }

        return embed;
    }

}
