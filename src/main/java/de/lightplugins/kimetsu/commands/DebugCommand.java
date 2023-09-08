package de.lightplugins.kimetsu.commands;

import de.lightplugins.kimetsu.database.AccountSQL;
import de.lightplugins.kimetsu.database.PlayerSQL;
import de.lightplugins.kimetsu.enums.OptionDataPath;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DebugCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        Dotenv config = Dotenv.configure().load();
        long channelID = Long.parseLong(config.get("COMMAND_CHANNEL_ID"));


        String triggerCommand = event.getName();
        if(!triggerCommand.equalsIgnoreCase("debug"))  {
            return;
        }


        OptionMapping subCommand0 = event.getOption(OptionDataPath.COINS_SET_LOGIN_NAME.getName());
        if(subCommand0 == null) {
            event.reply(":no_entry: " +
                    "Missing requirements - login name").setEphemeral(true).queue();
            return;
        }

        String charName = subCommand0.getAsString();

        PlayerSQL playerSQL = new PlayerSQL();
        CompletableFuture<Boolean> accountFuture = playerSQL.debugChar(charName);

        try {

            if(!accountFuture.get()) {
                event.reply(":no_entry: " +
                        "Something went wrong while debug player " + charName).setEphemeral(true).queue();
                return;
            }

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            event.reply(":white_check_mark: " +
                            "Successfully unbanned user " + charName)
                    .setEphemeral(true).queue();

            Objects.requireNonNull(event.getJDA().getTextChannelById(channelID))
                    .sendMessage(":white_check_mark: " +
                            "Successfully debug user " + charName + " from " + event.getUser().getAsMention() + " at " + formattedDate)
                    .queue();


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
