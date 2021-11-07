package io.jking.tickster.utility;

import io.jking.tickster.objects.command.ErrorType;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.Instant;

public final class EmbedFactory {
    private EmbedFactory() {
    }

    public static EmbedBuilder getError(ErrorType errorType, Object... objects) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(String.format("**An error occurred.**\n**Code:** %s\n**Response:** ```%s```",
                        errorType.getName(), errorType.getErrorResponse().formatted(objects)))
                .setTimestamp(Instant.now());
    }
}
