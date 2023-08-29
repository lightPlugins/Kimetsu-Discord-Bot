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

        /* PRE - Check if ban reason is not too long, caused by the database limit of 250 chars (VARCHAR 250) */

        if(banReason.length() > 240) {
            event.reply(":no_entry: The ban reason is to long - max 240 characters").setEphemeral(true).queue();
            return;
        }

        AccountSQL accountSQL = new AccountSQL();
        CompletableFuture<Boolean> completableFuture = accountSQL.userExist(loginName);

        /* STEP 1 - If user exist */

        try {
            if(!completableFuture.get()) {
                event.reply(":no_entry: " +
                        "The user name was not found in the database").setEphemeral(true).queue();
                return;
            }

            /* STEP 2 - Hwid may not be found */

            CompletableFuture<String> futureGetHwID = accountSQL.getUserHwid(loginName);
            if(futureGetHwID.get() == null) {
                event.reply(":no_entry: " +
                        "No hwid found for this user. That should not happen").setEphemeral(true).queue();
                return;
            }

            String hwid = futureGetHwID.get();

            /* STEP 3 - Check, if user already banned */

            CompletableFuture<Boolean> futureAlreadyBanned = accountSQL.hwidAlreadyExist(hwid);

            if(futureAlreadyBanned.get()) {
                event.reply(":no_entry: " +
                        "Users hwid is already banned").setEphemeral(true).queue();
                return;
            }

            /* STEP 4 - Insert the hwid in the new table */

            CompletableFuture<Boolean> futureBanHwid = accountSQL.createHwidBan(hwid);

            if(!futureBanHwid.get()) {
                event.reply(":no_entry: " +
                        "Something went wrong while inserting hwid in database.").setEphemeral(true).queue();
                return;
            }






        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
