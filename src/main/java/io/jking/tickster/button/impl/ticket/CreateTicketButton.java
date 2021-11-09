package io.jking.tickster.button.impl.ticket;

import io.jking.tickster.button.ButtonContext;
import io.jking.tickster.button.IButton;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.Result;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateTicketButton implements IButton {

    // TODO:
    //  - We still need to implement where in the event there is a specific category set, we need to create it under that first. Otherwise we default create.
    //  - Need to ignore FlatMap operand errors because they are annoying.

    @Override
    public void onButtonPress(ButtonContext context) {
        context.reply("Please wait as I create your ticket..")
                .delay(3, TimeUnit.SECONDS)
                .flatMap(InteractionHook::deleteOriginal)
                .flatMap(success -> createTicketChannel(context.getGuild(), context.getMember()))
                .onErrorFlatMap(throwable -> {
                    context.getChannel().sendMessageEmbeds(EmbedFactory.getError(ErrorType.CUSTOM, "You have a ticket already open!").build())
                            .delay(10, TimeUnit.SECONDS)
                            .flatMap(Message::delete)
                            .queue();
                    return null;
                })
                .queue(result -> {

                    result.onSuccess(textChannel -> textChannel.upsertPermissionOverride(context.getMember())
                            .setAllow(Permission.MESSAGE_WRITE, Permission.VIEW_CHANNEL)
                            .queue(success -> {
                                        context.getChannel().sendMessageFormat("Your ticket was created here: %s", textChannel.getAsMention())
                                                .delay(15, TimeUnit.SECONDS)
                                                .flatMap(Message::delete)
                                                .queue();

                                        sendStartingMessage(textChannel, context.getMember());
                                    }
                            ));

                    result.onFailure(throwable -> context.getChannel().sendMessage("Either you have a ticket currently open, or an error occurred.")
                            .delay(8, TimeUnit.SECONDS)
                            .flatMap(Message::delete)
                            .queue());
                }, new ErrorHandler().ignore(Arrays.asList(ErrorResponse.values())));
    }

    private void sendStartingMessage(TextChannel ticketChannel, Member member) {
        ticketChannel.sendMessage(member.getAsMention())
                .setEmbeds(EmbedFactory.getNewTicket(member.getUser()).build())
                .setActionRow(Button.of(ButtonStyle.DANGER, "close_ticket", "Close Ticket", Emoji.fromUnicode("\uD83D\uDD10")))
                .queue();
    }

    private RestAction<Result<TextChannel>> createTicketChannel(Guild guild, Member member) {
        final User user = member.getUser();
        final String ticketName = String.format("ticket-%s", user.getName());
        final List<TextChannel> temp = guild.getTextChannelsByName(ticketName, true);

        return temp.isEmpty() ? guild.createTextChannel(ticketName).mapToResult() : null;
    }

    @Override
    public String buttonId() {
        return "create_ticket";
    }
}
