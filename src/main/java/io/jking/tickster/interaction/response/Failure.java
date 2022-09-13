package io.jking.tickster.interaction.response;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.Instant;

public enum Failure {

    UPDATED("Data could not be **updated.**"),
    RETRIEVE("Data could not be **retrieved.**"),
    PERMISSION("Missing permission(s): `%s`"),
    CUSTOM("%s"),
    MATCHING("The data attempting to be updated is already the current."); // This might be too vague?

    private final String description;

    Failure(String description) {
        this.description = description;
    }

    public String getDescription(Object... objects) {
        return description.formatted(objects);
    }

    public EmbedBuilder getEmbed(Object... objects) {
        return new EmbedBuilder().setColor(Color.RED)
                .setDescription(String.format("**FAILURE:** %s", this.getDescription(objects)))
                .setTimestamp(Instant.now());
    }
}
