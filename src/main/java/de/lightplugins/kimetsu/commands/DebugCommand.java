package de.lightplugins.kimetsu.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DebugCommand extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {


        Dotenv config = Dotenv.configure().load();
        long channelID = Long.parseLong(config.get("COMMAND_CHANNEL_ID"));

        String triggerCommand = event.getName();
        if(!triggerCommand.equalsIgnoreCase("debug"))  {
            return;
        }


    }
}
