package io.jking.tickster.button.impl.ticket;

import io.jking.tickster.button.ButtonContext;
import io.jking.tickster.button.IButton;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.Result;

public class CreateTicketButton implements IButton {

    /*
     *  Logic:
     *  - Retrieve/See if there is an already open ticket
     *  - If ticket is open, send error saying you cannot open a ticket until your current one is closed/deleted.
     *  - Else go through the logic of creating the ticket channel, depending on the category. If no category is set, create general.
     *  - Add necessary permission overrides & buttons.
     *
     */

    @Override
    public void onButtonPress(ButtonContext context) {
        final long guildId = context.getGuild().getIdLong();
        context.getTicketCache().retrieve(guildId, ticket -> {
            final TextChannel ticketChannel = context.getGuild().getTextChannelById(ticket.getChannelId());
            if (ticket.getOpen() && ticketChannel != null) {
                context.replyError(ErrorType.CUSTOM, "You already have a ticket open, please delete it or close it before creating another one.")
                        .setContent("**Channel:** " + ticketChannel.getAsMention())
                        .setEphemeral(true)

                        .queue();
                return;
            }

            startTicketProcess(context);
        }, error -> startTicketProcess(context));
    }

    private void startTicketProcess(ButtonContext context) {
        final Member member = context.getMember();
        context.reply("PLease wait as I create your ticket...")
                .flatMap(hook -> createTicketChannel(context.getGuild(), member))
                .queue(result -> {
                    result.onSuccess(textChannel -> {
                        textChannel.upsertPermissionOverride(member)
                                .setAllow(Permission.MESSAGE_WRITE, Permission.VIEW_CHANNEL)
                                .flatMap(success -> context.getChannel().sendMessageFormat("Your ticket was created here: %s", success.getChannel().getAsMention()))
                                .flatMap(Message::delete)
                                .queue();


                    });

                    result.onFailure(throwable -> context.replyError(ErrorType.CUSTOM, "An error occurred creating your ticket channel, please try again or contact support")
                            .setEphemeral(true)
                            .queue());
                });
    }

//    @Override
//    public void onButtonPress(ButtonContext context) {
//        context.getTicketCache().retrieve(context.getGuild().getIdLong(), record -> {
//            final TextChannel ticketChannel = context.getGuild().getTextChannelById(record.getChannelId());
//            if (ticketChannel != null && !ticketChannel.canTalk()) {
//                context.reply("%s, you already have a ticket created.\nPlease close/delete it before opening another.\n**See here:** %s",
//                        context.getMember().getAsMention(), ticketChannel.getAsMention())
//                        .setEphemeral(true)
//                        .queue();
//            }
//        }, error -> context.reply("Please wait as I create your ticket..")
//                .delay(3, TimeUnit.SECONDS)
//                .flatMap(InteractionHook::deleteOriginal)
//                .flatMap(success -> createTicketChannel(context.getGuild(), context.getMember()))
//                .onErrorFlatMap(throwable -> {
//                    context.getChannel().sendMessageEmbeds(EmbedFactory.getError(ErrorType.CUSTOM, "You have a ticket already open!").build())
//                            .delay(10, TimeUnit.SECONDS)
//                            .flatMap(Message::delete)
//                            .queue();
//                    return null;
//                })
//                .queue(result -> {
//
//                    result.onSuccess(textChannel -> textChannel.upsertPermissionOverride(context.getMember())
//                            .setAllow(Permission.MESSAGE_WRITE, Permission.VIEW_CHANNEL)
//                            .queue(success -> {
//                                        context.getChannel().sendMessageFormat("Your ticket was created here: %s", textChannel.getAsMention())
//                                                .delay(15, TimeUnit.SECONDS)
//                                                .flatMap(Message::delete)
//                                                .queue();
//
//                                        sendStartingMessage(textChannel, context.getMember());
//                                    }
//                            ));
//
//                    result.onFailure(throwable -> context.getChannel().sendMessage("Either you have a ticket currently open, or an error occurred.")
//                            .delay(8, TimeUnit.SECONDS)
//                            .flatMap(Message::delete)
//                            .queue());
//                }, new ErrorHandler().ignore(Arrays.asList(ErrorResponse.values()))));
//    }


    private void sendStartingMessage(TextChannel ticketChannel, Member member) {
        ticketChannel.sendMessage(member.getAsMention())
                .setEmbeds(EmbedFactory.getNewTicket(member.getUser()).build())
                .setActionRow(Button.of(ButtonStyle.DANGER, "close_ticket", "Close Ticket", Emoji.fromUnicode("\uD83D\uDD10")))
                .queue();
    }

    private RestAction<Result<TextChannel>> createTicketChannel(Guild guild, Member member) {
        final User user = member.getUser();
        final String ticketName = String.format("ticket-%s", user.getName());
        return guild.createTextChannel(ticketName).mapToResult();
    }

    @Override
    public String buttonId() {
        return "create_ticket";
    }
}
