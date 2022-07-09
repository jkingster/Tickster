package io.jking.tickster.utility;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.Instant;

public final class EmbedFactory {

    private EmbedFactory() {}

    public static EmbedBuilder getDefault() {
        return new EmbedBuilder().setColor(Color.decode("#FF7300"));
    }

    public static EmbedBuilder getTicket(Member member, String subject, String information) {
        final String description = String.format("**Information:** ```%s```", information);
        return new EmbedBuilder().setDescription(description).setColor(Color.ORANGE.darker())
                .setTitle("Subject: " + subject)
                .setFooter("Ticket created by " + member.getUser().getName())
                .setTimestamp(Instant.now());
    }

}
