package io.jking.tickster.interaction.impl.button.report;

import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import io.jking.tickster.jooq.tables.records.GuildReportsRecord;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.utils.concurrent.Task;

import java.util.Collections;
import java.util.List;

public class ViewReportButton implements IButton {
    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit().flatMap(InteractionHook::retrieveOriginal).map(Message::getContentDisplay)
                .queue(original -> context.getReportCache().retrieve(original, record -> {
                    retrieveReportedUsers(context.getGuild(), record.getReportedUsers()).onSuccess(members -> {

                        if (members.isEmpty()) {
                            sendReport(context, record, Collections.emptyList());
                            return;
                        }

                        sendReport(context, record, members);
                    });
                }, error -> context.getHook().editOriginal(error.getMessage()).queue()));
    }

    private void sendReport(ButtonContext context, GuildReportsRecord record, List<Member> memberList) {
        if (memberList.isEmpty()) {
            context.getHook().sendMessageEmbeds(EmbedFactory.getReport(record, Collections.emptyList()).build())
                    .setContent(record.getUuid())
                    .addActionRow(Button.of(ButtonStyle.DANGER, "delete_report", "Delete Report", Emoji.fromUnicode("\uD83D\uDD28")))
                    .setEphemeral(true)
                    .queue();
        } else {
            context.getHook().sendMessageEmbeds(EmbedFactory.getReport(record, memberList).build())
                    .setContent(record.getUuid())
                    .addActionRow(Button.of(ButtonStyle.DANGER, "delete_report", "Delete Report", Emoji.fromUnicode("\uD83D\uDD28")))
                    .setEphemeral(true)
                    .queue();
        }
    }


    private Task<List<Member>> retrieveReportedUsers(Guild guild, Long[] memberIds) {
        return guild.retrieveMembersByIds(List.of(memberIds));
    }


    @Override
    public String componentId() {
        return "view_report";
    }
}
