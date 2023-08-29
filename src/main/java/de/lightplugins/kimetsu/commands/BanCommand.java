package de.lightplugins.kimetsu.commands;

import de.lightplugins.kimetsu.database.AccountSQL;
import de.lightplugins.kimetsu.enums.OptionDataPath;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BanCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {


        Dotenv config = Dotenv.configure().load();
        long channelID = Long.parseLong(config.get("COMMAND_CHANNEL_ID"));

        String triggerCommand = event.getName();
        if(!triggerCommand.equalsIgnoreCase("ban"))  {
            return;
        }

        OptionMapping subCommand0 = event.getOption(OptionDataPath.BAN_LOGIN_NAME.getName());
        if(subCommand0 == null) {
            event.reply(":no_entry: Missing requirements - login name").setEphemeral(true).queue();
            return;
        }

        OptionMapping subCommand1 = event.getOption(OptionDataPath.BAN_REASON.getName());
        if(subCommand1 == null) {
            event.reply(":no_entry:  Missing requirements - ban reason").setEphemeral(true).queue();
            return;
        }

        String loginName = subCommand0.getAsString();
        String banReason = subCommand1.getAsString();

        if(banReason.length() > 240) {
            event.reply(":no_entry: The ban reason is to long - max 240 characters").setEphemeral(true).queue();
            return;
        }

        AccountSQL accountSQL = new AccountSQL();
        CompletableFuture<Boolean> completableFuture = accountSQL.userExist(loginName);

        try {
            if(!completableFuture.get()) {
                event.reply(":no_entry: " +
                        "The user name was not found in the database").setEphemeral(true).queue();
                return;
            }

            CompletableFuture<String> futureGetHwID = accountSQL.getHwID(loginName);
            if(futureGetHwID.get() == null) {
                event.reply(":no_entry: " +
                        "No HwID found for this user").setEphemeral(true).queue();
                return;
            }

            String hwid = futureGetHwID.get();




        } catch (ExecutionException | InterruptedException e) {

        }


    }
}
