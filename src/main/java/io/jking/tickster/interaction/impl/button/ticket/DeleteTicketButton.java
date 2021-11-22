package io.jking.tickster.interaction.impl.button.ticket;

import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.jooq.JSON;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
public class DeleteTicketButton implements IButton {

    // TODO:
    // - Something to think about, do we want to rebuild the transcript when we are deleting the channel?
    // - Or do we want to negate that process entirely, and push it to a further build if there's a demand for it?
    // - Otherwise I don't see a point.

    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit().queue(deferred -> {
            final long channelId = context.getChannel().getIdLong();
            context.getTicketCache().retrieve(channelId, ticket -> {
                final JSON transcript = ticket.getTranscript();
                if (transcript != null) {
                    sendTranscriptPrivately(context.getUser(), transcript.data());
                }

                context.getChannel().delete().queue(null, new ErrorHandler().ignore(Arrays.asList(ErrorResponse.values())));
            }, error -> context.replyError(ErrorType.CUSTOM, "An error occurred deleting your ticket."));
        });
    }

    private void sendTranscriptPrivately(User user, String transcript) {
        user.openPrivateChannel().flatMap(privateChannel ->
                privateChannel.sendMessage("One of your tickets was deleted, here is the transcript.")
                        .addFile(transcript.getBytes(StandardCharsets.UTF_8), "transcript.json"))
                .queue(null, new ErrorHandler().ignore(Arrays.asList(ErrorResponse.values())));
    }

    @Override
    public String componentId() {
        return "ticket_delete";
    }
}
