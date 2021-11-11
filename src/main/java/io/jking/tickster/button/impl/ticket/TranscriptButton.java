package io.jking.tickster.button.impl.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jking.tickster.button.ButtonContext;
import io.jking.tickster.button.IButton;
import io.jking.tickster.command.type.ErrorType;
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
    public void onButtonPress(ButtonContext context) {
        context.getInteraction().deferEdit().queue(success -> {
            context.getChannel().getIterableHistory().takeAsync(1000)
                    .whenCompleteAsync((messages, throwable) -> {
                        if (throwable != null) {
                            context.getHook().sendMessageEmbeds(EmbedFactory.getError(ErrorType.CUSTOM, "There was an error processing your transcript.").build())
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
                            context.getHook().sendMessageEmbeds(EmbedFactory.getError(ErrorType.CUSTOM, "There were no messages to transpire.").build())
                                    .setEphemeral(true)
                                    .queue();
                            return;
                        }

                        buildTranscript(context, filteredList);
                    });
        });
    }

    private void buildTranscript(ButtonContext context, List<MessageData> messageList) {
        try {
            final String jsonify = new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(messageList);

            final long guildId = context.getGuild().getIdLong();
            final long channelId = context.getChannel().getIdLong();

            context.getDatabase().getDSL().update(GUILD_TICKETS)
                    .set(GUILD_TICKETS.TRANSCRIPT, JSON.json(jsonify))
                    .where(GUILD_TICKETS.GUILD_ID.eq(guildId))
                    .and(GUILD_TICKETS.CHANNEL_ID.eq(channelId))
                    .executeAsync(context.getDatabase().getExecutor());

            final EmbedBuilder embed = EmbedFactory.getDefault()
                    .setDescription("Here is your ticket transcript, in prettified JSON format.")
                    .setFooter("Please note, some message(s) possibly got omitted due to discord caching.");

            context.getHook().sendMessageEmbeds(embed.build())
                    .addFile(jsonify.getBytes(StandardCharsets.UTF_8), "transcript.json")
                    .queue();

        } catch (JsonProcessingException e) {
            context.getHook().sendMessageEmbeds(EmbedFactory.getError(ErrorType.CUSTOM, "An error occurred building your transcript...").build())
                    .setEphemeral(true)
                    .queue();
        }
    }

    @Override
    public String buttonId() {
        return "ticket_transcript";
    }
}
