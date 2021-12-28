package io.jking.tickster.interaction.impl.selection;

import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.interaction.context.SelectionContext;
import io.jking.tickster.interaction.type.ISelection;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import org.jooq.tools.json.JSONObject;

import java.nio.charset.StandardCharsets;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class ViewTicketsSelection implements ISelection {

    @Override
    public void onInteraction(SelectionContext context) {
        context.deferEdit().queue(deferred -> {
            final SelectOption option = context.getSelectedOption();
            if (option == null) {
                context.replyError(ErrorType.SELECTION);
                return;
            }

            final long guildId = context.getGuild().getIdLong();
            final long channelId = Long.parseLong(option.getValue());

            context.getDatabase().getDSL().selectFrom(GUILD_TICKETS)
                    .where(GUILD_TICKETS.GUILD_ID.eq(guildId))
                    .and(GUILD_TICKETS.CHANNEL_ID.eq(channelId))
                    .fetchAsync()
                    .thenAcceptAsync(record -> {
                        if (record.isEmpty()) {
                            context.replyError(ErrorType.RETRIEVING, "that ticket.");
                            return;
                        }

                        sendTicket(context, record.get(0)).queue();
                    })
                    .exceptionallyAsync(throwable -> {
                        context.replyError(ErrorType.CUSTOM, "Could not retrieve that selection!");
                        return null;
                    });
        });
    }

    private WebhookMessageAction<Message> sendTicket(SelectionContext context, GuildTicketsRecord record) {
        final EmbedBuilder embed = EmbedFactory.getTicket(record);
        final WebhookMessageAction<Message> message = context.getHook().sendMessageEmbeds(embed.build())
                .setEphemeral(true);

        if (record.getTranscript() != null) {
            try {
                String json = JSONObject.toJSONString(record.intoMap());
                return message.addFile(json.getBytes(StandardCharsets.UTF_8), "transcript.json");
            } catch (Exception ignored) {
            }
        }

        return message;
    }

    @Override
    public String componentId() {
        return "view_tickets";
    }
}
