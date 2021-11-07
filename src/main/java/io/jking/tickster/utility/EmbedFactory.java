package io.jking.tickster.utility;

import io.jking.tickster.objects.command.ErrorType;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.Instant;
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
}
