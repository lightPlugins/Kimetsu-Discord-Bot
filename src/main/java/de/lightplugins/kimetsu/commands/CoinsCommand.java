package de.lightplugins.kimetsu.commands;

import de.lightplugins.kimetsu.database.AccountSQL;
import de.lightplugins.kimetsu.enums.OptionDataPath;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CoinsCommand extends ListenerAdapter {


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

        if(!subCommand0.getAsString().equalsIgnoreCase("add")) {

            event.reply("Subcommand add/remove is wrong!").setEphemeral(true).queue();
            return;
        }

        try {
            OptionMapping subCommand1 = event.getOption(OptionDataPath.COINS_SET_LOGIN_NAME.getName());
            if(subCommand1 == null) {
                event.reply("Missing requirements - login name").setEphemeral(true).queue();
                return;
            }

            OptionMapping subCommand2 = event.getOption(OptionDataPath.COINS_SET_AMOUNT.getName());

            if(subCommand2 == null) {
                event.reply("Missing requirements - amount").setEphemeral(true).queue();
                return;
            }

            String loginName = subCommand1.getAsString();
            int coinAmount = subCommand2.getAsInt();

            if(coinAmount < 0) {
                event.reply(":no_entry: " +
                        "You cannot use negative numbers!").setEphemeral(true).queue();
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

                CompletableFuture<Integer> completableFuture2 = accountSQL.getCoins(loginName);
                int currentCoins = completableFuture2.get();

                CompletableFuture<Boolean> completableFuture1 = accountSQL.updateCoins(currentCoins + coinAmount, loginName);

                if(completableFuture1.get()) {
                    Objects.requireNonNull(event.getJDA().getTextChannelById(channelID))
                            .sendMessage(":white_check_mark: " +
                                    "Successfully added **" + coinAmount + "** coins for user **" + loginName + "**. "
                                    + "Now he have **" + (currentCoins + coinAmount) + "** Coins.")
                            .queue();
                    event.reply("Command successfully executed").setEphemeral(true).queue();

                    return;

                }
                event.reply("Something went wrong while executing this command. Inspect the log for more information's").setEphemeral(true).queue();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        } catch (NumberFormatException ex) {
            event.reply("The coin amount must be an integer").setEphemeral(true).queue();
        }
    }
}
