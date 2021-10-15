package io.jking.untitled.utility;

import io.jking.untitled.command.error.CommandError;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.Instant;

public final class EmbedUtil {
    private EmbedUtil()  {
    }

    private static final Color DEFAULT = Color.decode("#cc6666");

    public static EmbedBuilder getDefault() {
        return new EmbedBuilder()
                .setColor(DEFAULT)
                .setTimestamp(Instant.now());
    }

    public static EmbedBuilder getError(CommandError error, Object... objects) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setAuthor("Error")
                .setDescription(String.format("**Error Thrown:** `%s`\n**Response:** `%s`\n",
                        error.getName(), error.getErrorResponse(objects)))
                .setTimestamp(Instant.now());
    }


}
