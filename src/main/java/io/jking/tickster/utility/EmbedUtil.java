package io.jking.tickster.utility;

import io.jking.tickster.interaction.core.Error;
import io.jking.tickster.interaction.core.Success;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.Instant;


public final class EmbedUtil {
    public static Color PRIMARY = Color.decode("#FF7300").brighter();
    public static Color SECONDARY = Color.decode("#008DF5").darker();
    private EmbedUtil() {

    }

    public static EmbedBuilder getDefault() {
        return new EmbedBuilder().setColor(PRIMARY);
    }

    public static EmbedBuilder getError(Error error, Object... objects) {
        return new EmbedBuilder().setColor(Color.RED)
                .setTimestamp(Instant.now())
                .setDescription(error.getDesc(objects))
                .setAuthor("Error");
    }

    public static EmbedBuilder getSuccess(Success success, Object... objects) {
        return new EmbedBuilder().setColor(Color.GREEN.darker())
                .setTimestamp(Instant.now())
                .setDescription(success.getDesc(objects))
                .setAuthor("Success");
    }





}
