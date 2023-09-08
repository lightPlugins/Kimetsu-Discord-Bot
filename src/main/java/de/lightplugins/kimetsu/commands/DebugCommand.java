package de.lightplugins.kimetsu.commands;

import de.lightplugins.kimetsu.enums.OptionDataPath;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class DebugCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        Dotenv config = Dotenv.configure().load();
        long channelID = Long.parseLong(config.get("COMMAND_CHANNEL_ID"));


        String triggerCommand = event.getName();
        if(!triggerCommand.equalsIgnoreCase("coins"))  {
            return;
        }


        OptionMapping subCommand0 = event.getOption(OptionDataPath.COINS_ADD.getName());
        if(subCommand0 == null) {
            event.reply("Missing requirements - login name").setEphemeral(true).queue();
            return;
        }


    }
}
