package io.jking.tickster.interaction.command.impl.info;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandFlag;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.concurrent.TimeUnit;

public class AboutCommand extends AbstractCommand {

    public AboutCommand() {
        super(
                "about",
                "Read about me!",
                CommandCategory.INFO,
                CommandFlag.ofEphemeral()
        );
    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final EmbedBuilder embedBuilder = EmbedUtil.getDefault()
                .setThumbnail(sender.getSelfUser().getEffectiveAvatarUrl())
                .setAuthor(sender.getSelfUser().getName())
                .setTitle("About Me")
                .setDescription("I am a bot tailored towards processing, managing and creating tickets to help fully moderate a server. I am equipped with the necessary commands and tools to provide the best ticketing system to date among discord bots. If you have any questions, feel free to join the support server!");

        sender.reply(embedBuilder).addActionRow(Button.link("https://www.discord.gg", "Support Server"))
                .delay(30, TimeUnit.SECONDS)
                .flatMap(InteractionHook::deleteOriginal)
                .queue();
    }
}
