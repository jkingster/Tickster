package io.jking.untitled.utility;

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


}
