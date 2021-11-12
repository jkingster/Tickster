package io.jking.tickster.interaction.impl.button.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.impl.slash.object.type.ErrorType;
import io.jking.tickster.interaction.type.IButton;
import io.jking.tickster.object.MessageData;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class DeleteTicketButton implements IButton {

    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit().queue(success -> {
            context.getGuildCache().retrieve(context.getGuild().getIdLong(), record -> {
                final TextChannel logChannel = context.getGuild().getTextChannelById(record.getLogChannel());
                if (logChannel != null && logChannel.canTalk()) {
                    context.getChannel().getIterableHistory().takeAsync(1000)
                            .whenCompleteAsync((messages, throwable) -> {

                                if (throwable != null)
                                    return;

                                final List<MessageData> filteredList = messages.parallelStream()
                                        .filter(message -> !message.getAuthor().isBot())
                                        .sorted(Comparator.comparing(Message::getTimeCreated))
                                        .map(MessageData::new)
                                        .collect(Collectors.toUnmodifiableList());


                                if (filteredList.isEmpty())
                                    return;

                                buildTranscript(logChannel, context.getUser(), filteredList);
                            });
                }
            }, null);

            context.getHook().editOriginal("This ticket will now be deleted.")
                    .setEmbeds(Collections.emptyList())
                    .setActionRows(Collections.emptyList())
                    .delay(10, TimeUnit.SECONDS)
                    .flatMap(__ -> context.getChannel().delete())
                    .onErrorFlatMap(throwable -> {
                        context.getChannel()
                                .sendMessageEmbeds(EmbedFactory.getError(ErrorType.CUSTOM, "This ticket channel could not be deleted due to an error.").build())
                                .queue();
                        return null;
                    })
                    .queue(deleted -> {
                        final long guildId = context.getGuild().getIdLong();
                        final long channelId = context.getChannel().getIdLong();
                        context.getDatabase().getDSL().deleteFrom(GUILD_TICKETS)
                                .where(GUILD_TICKETS.GUILD_ID.eq(guildId))
                                .and(GUILD_TICKETS.CHANNEL_ID.eq(channelId))
                                .executeAsync(context.getDatabase().getExecutor());
                    });
        });


    }

    private void buildTranscript(TextChannel logChannel, User user, List<MessageData> messageList) {
        try {
            final String jsonify = new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(messageList);

            final EmbedBuilder embed = EmbedFactory.getDefault()
                    .setTitle("Ticket Deleted")
                    .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
                    .setDescription("Here is the ticket transcript, in prettified JSON format.")
                    .setFooter("Please note, some message(s) possibly got omitted due to discord caching.")
                    .setTimestamp(Instant.now());

            logChannel.sendMessageEmbeds(embed.build())
                    .addFile(jsonify.getBytes(StandardCharsets.UTF_8), "transcript.json")
                    .queue();

        } catch (JsonProcessingException ignored) {
        }
    }

    @Override
    public String componentId() {
        return "ticket_delete";
    }
}
