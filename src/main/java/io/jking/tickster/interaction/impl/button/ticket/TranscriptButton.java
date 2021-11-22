package io.jking.tickster.interaction.impl.button.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import io.jking.tickster.object.MessageData;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.jooq.JSON;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class TranscriptButton implements IButton {

    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit().queue(deferred -> context.getChannel().getIterableHistory().takeAsync(1000)
                .whenCompleteAsync((messages, throwable) -> {
                    if (throwable != null) {
                        context.getHook().sendMessage("There was an error.")
                                .setEphemeral(true)
                                .queue();
                        return;
                    }

                    final List<MessageData> filteredList = messages.parallelStream()
                            .filter(message -> !message.getAuthor().isBot())
                            .sorted(Comparator.comparing(Message::getTimeCreated))
                            .map(MessageData::new)
                            .collect(Collectors.toUnmodifiableList());

                    if (filteredList.isEmpty()) {
                        context.getHook().sendMessage("No messages to build into a transcript!")
                                .setEphemeral(true)
                                .queue();
                        return;
                    }

                    sendTranscript(context, filteredList);
                }));
    }

    private void sendTranscript(ButtonContext context, List<MessageData> filteredList) {
        try {
            final String transcript = getJSONTranscript(filteredList);

            final long channelId = context.getChannel().getIdLong();
            context.getTicketCache().update(
                    channelId,
                    GUILD_TICKETS.TRANSCRIPT,
                    JSON.json(transcript),
                    success -> {
                        final EmbedBuilder embed = EmbedFactory.getDefault()
                                .setDescription("Here is your ticket transcript, in prettified JSON format.")
                                .setFooter("Please note, some message(s) possibly got omitted due to discord caching.");

                        context.getHook().sendMessageEmbeds(embed.build())
                                .addFile(transcript.getBytes(StandardCharsets.UTF_8), "transcript.json")
                                .queue();

                    }, error -> context.getHook().sendMessageEmbeds(EmbedFactory.getError(ErrorType.CUSTOM, "An error occurred building your transcript.").build())
                            .setEphemeral(true)
                            .queue()
            );

        } catch (JsonProcessingException e) {
            context.getHook().sendMessageEmbeds(EmbedFactory.getError(ErrorType.CUSTOM, "An error occurred building your transcript.").build())
                    .setEphemeral(true)
                    .queue();
        }
    }

    private String getJSONTranscript(List<MessageData> messageList) throws JsonProcessingException {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(messageList);
    }

    @Override
    public String componentId() {
        return "ticket_transcript";
    }
}
