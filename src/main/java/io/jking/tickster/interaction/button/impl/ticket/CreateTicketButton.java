package io.jking.tickster.interaction.button.impl.ticket;

import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.core.Error;
import io.jking.tickster.interaction.core.impl.ButtonContext;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;

import java.util.List;

public class CreateTicketButton extends AbstractButton {

    private final List<Permission> permissionList = List.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);

    public CreateTicketButton() {
        super("button:create_ticket");
    }

    @Override
    public void onButtonPress(ButtonContext context) {
        final Member self = context.getSelfMember();
        if (!self.hasPermission(Permission.MANAGE_CHANNEL)) {
            context.replyErrorEphemeral(
                    Error.PERMISSION,
                    self.getUser().getAsTag(),
                    Permission.MANAGE_CHANNEL
            );
            return;
        }

        final long categoryId = context.getRecord().getCategoryId();
        final Category category = context.getGuild().getCategoryById(categoryId);
        final Member member = context.getMember();

        createTicketChannel(member, category).queue(created -> {
            setupSupportRole(created, context.getRecord());



        }, error -> {
            context.replyErrorEphemeral(Error.CUSTOM, "Could not create your ticket, sorry!");
        });


    }

    private ChannelAction<TextChannel> createTicketChannel(Member member, Category category) {
        final Guild guild = member.getGuild();
        final String channelName = String.format("ticket-%s", member.getUser().getName());
        return category == null
                ? guild.createTextChannel(channelName)
                : guild.createTextChannel(channelName, category);
    }

    private void setupSupportRole(TextChannel channel, GuildDataRecord record) {
        final Guild guild = channel.getGuild();
        final long supportId = record.getSupportId();
        final Role supportRole = guild.getRoleByBot(supportId);

        if (supportRole == null)
            return;

        channel.putPermissionOverride(supportRole).setAllow(permissionList)
                .flatMap(__ -> channel.sendMessage(supportRole.getAsMention()))
                .queue();
    }

    private PermissionOverrideAction setupMemberPermissions(TextChannel channel, Member member) {
        return channel.putPermissionOverride(member).setAllow(permissionList);
    }
}

