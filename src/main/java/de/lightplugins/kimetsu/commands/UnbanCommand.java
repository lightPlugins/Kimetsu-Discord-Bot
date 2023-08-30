package de.lightplugins.kimetsu.commands;

import de.lightplugins.kimetsu.database.AccountSQL;
import de.lightplugins.kimetsu.enums.OptionDataPath;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UnbanCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        Dotenv config = Dotenv.configure().load();
        long channelID = Long.parseLong(config.get("COMMAND_CHANNEL_ID"));

        String triggerCommand = event.getName();
        if(!triggerCommand.equalsIgnoreCase("unban"))  {
            return;
        }


        OptionMapping subCommand0 = event.getOption(OptionDataPath.BAN_LOGIN_NAME.getName());
        if(subCommand0 == null) {
            event.reply(":no_entry: Missing requirements - login name").setEphemeral(true).queue();
            return;
        }

        String loginName = subCommand0.getAsString();

        AccountSQL accountSQL = new AccountSQL();
        CompletableFuture<Boolean> completableFuture = accountSQL.userExist(loginName);

        try {

            if(!completableFuture.get()) {
                event.reply(":no_entry: " +
                        "The user name was not found in the database").setEphemeral(true).queue();
                return;
            }

            CompletableFuture<String> futureGetHwID = accountSQL.getUserHwid(loginName);
            if(futureGetHwID.get() == null) {
                event.reply(":no_entry: " +
                        "No hwid found for this user. That should not happen").setEphemeral(true).queue();
                return;
            }

            String hwid = futureGetHwID.get();

            CompletableFuture<Boolean> futureAlreadyBanned = accountSQL.hwidAlreadyExist(hwid);

            if(!futureAlreadyBanned.get()) {
                event.reply(":no_entry: " +
                        "Users hwid is not banned").setEphemeral(true).queue();
                return;
            }

            CompletableFuture<Boolean> futureUnban = accountSQL.removeHwidBan(hwid);

            if(!futureUnban.get()) {
                event.reply(":no_entry: " +
                        "Something went wrong while deleting database entry").setEphemeral(true).queue();
                return;
            }


            CompletableFuture<List<String>> futureMultipleHwid = accountSQL.multipleAccountsWithSameHwid(hwid);

            for(String singleLogin : futureMultipleHwid.get()) {

                CompletableFuture<Boolean> futureUpdateStatus = accountSQL.updateStatus("OK", singleLogin);
                if(!futureUpdateStatus.get()) {
                    event.reply(":no_entry: " +
                                    "Something went wrong while updating status from " + singleLogin + " to BLOCK")
                            .setEphemeral(true).queue();
                    return;
                }
            }

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            event.reply(":white_check_mark: " +
                            "Successfully unbanned user " + loginName)
                    .setEphemeral(true).queue();

            Objects.requireNonNull(event.getJDA().getTextChannelById(channelID))
                    .sendMessage(":white_check_mark: " +
                            "Successfully unbanned user " + loginName + " from " + event.getUser().getAsMention() + " at " + formattedDate)
                    .queue();


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
