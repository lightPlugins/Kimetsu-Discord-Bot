package de.lightplugins.kimetsu.commands;

import de.lightplugins.kimetsu.enums.OptionDataPath;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Objects;

public class CoinsCommand extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        Dotenv config = Dotenv.configure().load();


        String triggerCommand = event.getName();
        if(!triggerCommand.equalsIgnoreCase("coins"))  {
            return;
        }

        OptionMapping subCommand = event.getOption(OptionDataPath.COINS_SET_LOGIN_NAME.getName());

        if(subCommand == null) {
            Objects.requireNonNull(
                    event.getJDA().getTextChannelById(config.get("COMMAND_CHANNEL_ID" + "L")))
                    .sendMessage("Missing requirements").queue();
            return;
        }



        String sub1 = subCommand.getAsString();

    }
}
