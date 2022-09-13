package io.jking.tickster.utility;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.Instant;

public final class EmbedFactory {

    private EmbedFactory() {
    }

    public static EmbedBuilder getDefault() {
        return new EmbedBuilder().setColor(Color.decode("#FF7300"));
    }

    public static EmbedBuilder getUpdated(String content, Object... objects) {
        return new EmbedBuilder().setColor(Color.GREEN.darker())
                .setDescription(String.format("**SUCCESS:** %s", content.formatted(objects)))
                .setTimestamp(Instant.now());
    }


}
