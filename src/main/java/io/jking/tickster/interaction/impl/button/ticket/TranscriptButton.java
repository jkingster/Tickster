package io.jking.tickster.interaction.impl.button.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import io.jking.tickster.object.MessageData;
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

            context.getTicketCache().update(channelId, GUILD_TICKETS.TRANSCRIPT, JSON.json(transcript));

            context.getChannel()
                    .sendFile(transcript.getBytes(StandardCharsets.UTF_8), "transcript.json")
                    .content("Here is your prettified transcript **(JSON)**.")
                    .queue();

        } catch (JsonProcessingException e) {
            context.replyError(ErrorType.CUSTOM, "An error occurred building your transcript.");
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
