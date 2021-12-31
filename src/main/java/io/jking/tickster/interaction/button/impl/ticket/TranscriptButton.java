package io.jking.tickster.interaction.button.impl.ticket;

import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.core.impl.ButtonSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class TranscriptButton extends AbstractButton {

    public TranscriptButton() {
        super("button:transcript:id:%s");
    }

    @Override
    public void onButtonPress(ButtonSender context) {
        final Member self = context.getSelfMember();
        if (!self.hasPermission(Permission.MESSAGE_HISTORY)) {
            context.replyErrorEphemeral(
                    Error.PERMISSION,
                    self.getUser().getAsTag(),
                    Permission.MESSAGE_HISTORY
            ).queue();
            return;
        }

        context.replyEphemeral("Generating your transcript now.").queue(ignored -> {
            context.getTextChannel().getIterableHistory().takeAsync(500)
                    .whenCompleteAsync((list, throwable) -> {
                        if (throwable != null) {
                            context.replyErrorEphemeral(Error.UNKNOWN).queue();
                            return;
                        }

                        final List<Message> filteredList = list.stream()
                                .filter(msg -> !msg.getAuthor().isBot())
                                .collect(Collectors.toUnmodifiableList());

                        if (filteredList.isEmpty()) {
                            context.replyErrorEphemeral(Error.CUSTOM, "No messages to build into a transcript.").queue();
                            return;
                        }

                        final String prettifiedContent = getPrettyTranscript(filteredList);

                        context.getUser().openPrivateChannel().queue(channel -> {
                            final EmbedBuilder embed = EmbedUtil.getDefault()
                                    .setTimestamp(Instant.now())
                                    .setAuthor(context.getUser().getAsTag() + ", here is your ticket transcript.")
                                    .setDescription(String.format(
                                            """
                                            **Server:** %s
                                            **Channel:** %s
                                                                                        
                                            Bot messages are omitted from this transcript.
                                            """,
                                            context.getGuild().getName(), context.getTextChannel().getName()))
                                    .setFooter("This transcript is not stored anywhere and cannot be accessed.");


                            channel.sendMessageEmbeds(embed.build())
                                    .addFile(prettifiedContent.getBytes(StandardCharsets.UTF_8), "transcript.txt")
                                    .queue();
                        }, error -> context.replyErrorEphemeral(
                                Error.CUSTOM,
                                "I cannot send you the transcript unless you have DM(s) opened!")
                                .queue());
                    });
        });
    }

    private String getPrettyTranscript(List<Message> filteredList) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (Message message : filteredList) {
            stringBuilder.append(String.format("[%s] ", message.getTimeCreated().toLocalDateTime()));
            stringBuilder.append(String.format("[%s | %s]: ", message.getAuthor().getAsTag(), message.getAuthor().getIdLong()));
            stringBuilder.append(String.format("%s\n", message.getContentDisplay()));
        }
        return stringBuilder.toString();
    }

}
