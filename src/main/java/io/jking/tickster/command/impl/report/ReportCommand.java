package io.jking.tickster.command.impl.report;

import io.jking.tickster.command.Category;
import io.jking.tickster.command.Command;
import io.jking.tickster.command.CommandContext;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.jooq.tables.records.GuildReportsRecord;
import io.jking.tickster.object.CButton;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.jking.tickster.jooq.tables.GuildReports.GUILD_REPORTS;

public class ReportCommand extends Command {
    public ReportCommand() {
        super("report", "Report up to 3 user(s) at a time.", Category.REPORTS);
        addOptions(
                new OptionData(OptionType.USER, "user", "The user you want to report.", true),
                new OptionData(OptionType.STRING, "reason", "Reason for reporting.", true),
                new OptionData(OptionType.USER, "user-2", "Another user you want to report.", false),
                new OptionData(OptionType.USER, "user-3", "Another user you want to report.", false)
        );
    }

    @Override
    public void onCommand(CommandContext ctx) {
        final List<OptionMapping> options = ctx.getOptionsByType(OptionType.USER);

        if (options.isEmpty()) {
            ctx.replyError(ErrorType.ARGS);
            return;
        }

        final String reason = ctx.getOptionString("reason");
        if (reason == null || reason.isEmpty()) {
            ctx.replyError(ErrorType.ARGS, "The report reason.");
            return;
        }

        if (options.size() == 1) {
            final Member target = options.get(0).getAsMember();

            if (target == null) {
                ctx.replyError(ErrorType.UNKNOWN);
                return;
            }

            sendReport(ctx, List.of(target), reason);
            return;
        }

        final List<Member> memberList = options.parallelStream()
                .map(OptionMapping::getAsMember)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());

        if (memberList.isEmpty()) {
            ctx.replyError(ErrorType.UNKNOWN);
            return;
        }

        sendReport(ctx, memberList, reason);
    }

    private void sendReport(CommandContext ctx, List<Member> reportedMembers, String reason) {
        final Long[] memberIds = toLongArray(reportedMembers);
        final long selfId = ctx.getSelf().getIdLong();

        if (containsSelf(memberIds, selfId)) {
            ctx.replyError(ErrorType.CUSTOM, "You cannot report me. Nice try though!");
            return;
        }

        if (containsSelf(memberIds, ctx.getAuthor().getIdLong())) {
            ctx.replyError(ErrorType.CUSTOM, "You cannot report yourself...");
            return;
        }

        ctx.retrieveRecord(record -> {
            final TextChannel reportChannel = ctx.getGuild().getTextChannelById(record.getReportChannel());
            if (reportChannel == null || !reportChannel.canTalk()) {
                ctx.replyError(ErrorType.CUSTOM, "The report channel is unset or could not be found!");
                return;
            }

            final long guildId = ctx.getGuild().getIdLong();
            final long issuerId = ctx.getAuthor().getIdLong();
            final LocalDateTime timestamp = LocalDateTime.now();
            final String uuid = UUID.randomUUID().toString();


            final GuildReportsRecord reportRecord = GUILD_REPORTS.newRecord()
                    .values(guildId, memberIds, issuerId, reason, timestamp, uuid);

            ctx.getDatabase().getDSL().insertInto(GUILD_REPORTS)
                    .set(reportRecord)
                    .executeAsync()
                    .thenAcceptAsync(action -> {
                        final EmbedBuilder newReport = EmbedFactory.getReportedCreated(ctx.getAuthor());
                        ctx.reply(newReport).setEphemeral(true).queue();
                        reportChannel.sendMessageEmbeds(EmbedFactory.getNewReport(ctx.getAuthor()).build())
                                .content(uuid)
                                .setActionRow(
                                        CButton.PRIMARY.format("view_report", "View Report", Emoji.fromUnicode("\uD83D\uDD0D")),
                                        CButton.DANGER.format("delete_report", "Delete Report", Emoji.fromUnicode("\uD83D\uDD28"))
                                )
                                .queue();

                        ctx.getReportCache().put(uuid, reportRecord);
                    })
                    .exceptionallyAsync(throwable -> {
                        ctx.replyError(ErrorType.CUSTOM, "The reported could not be created!");
                        return null;
                    });
        }, error -> ctx.replyUnknown());
    }

    private Long[] toLongArray(List<Member> memberList) {
        final Long[] array = new Long[memberList.size()];
        for (int i = 0; i < array.length; i++) {
            final long id = memberList.get(i).getIdLong();
            array[i] = id;
        }
        return array;
    }

    private boolean containsSelf(Long[] memberIds, long selfId) {
        for (long memberId : memberIds) {
            if (memberId == selfId)
                return true;
        }
        return false;
    }
}
