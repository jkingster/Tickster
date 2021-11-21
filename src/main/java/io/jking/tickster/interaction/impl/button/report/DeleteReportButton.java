package io.jking.tickster.interaction.impl.button.report;

import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static io.jking.tickster.jooq.tables.GuildReports.GUILD_REPORTS;

public class DeleteReportButton implements IButton {
    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit().flatMap(InteractionHook::retrieveOriginal).map(Message::getContentDisplay).queue(uuid -> {
            context.getDatabase().getDSL().deleteFrom(GUILD_REPORTS)
                    .where(GUILD_REPORTS.UUID.eq(uuid))
                    .executeAsync()
                    .thenAcceptAsync(action -> {
                        context.getHook().editOriginal("This report is now deleted. Thank you.")
                                .setEmbeds(Collections.emptyList())
                                .setActionRows(Collections.emptyList())
                                .delay(5, TimeUnit.SECONDS)
                                .flatMap(Message::delete)
                                .queue();
                        context.getReportCache().delete(uuid);
                    })
                    .exceptionallyAsync(throwable -> {
                        context.getHook().editOriginal("There was an error deleting this report.").queue();
                        return null;
                    });
        });
    }

    @Override
    public String componentId() {
        return "delete_report";
    }
}
