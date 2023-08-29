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

        System.out.println("test1");
        Dotenv config = Dotenv.configure().load();
        long channelID = Long.parseLong(config.get("COMMAND_CHANNEL_ID"));

        String triggerCommand = event.getName();
        if(!triggerCommand.equalsIgnoreCase("coins"))  {
            return;
        }

        try {
            OptionMapping subCommand1 = event.getOption(OptionDataPath.COINS_SET_LOGIN_NAME.getName());
            System.out.println("test2");
            if(subCommand1 == null) {
                Objects.requireNonNull(event.getJDA().getTextChannelById(channelID))
                        .sendMessage("Missing requirements - login name")
                        .queue();
                return;
            }

            OptionMapping subCommand2 = event.getOption(OptionDataPath.COINS_SET_AMOUNT.getName());

            if(subCommand2 == null) {
                Objects.requireNonNull(event.getJDA().getTextChannelById(channelID))
                        .sendMessage("Missing requirements - amount")
                        .queue();
                return;
            }
            System.out.println("test3");

            String loginName = subCommand1.getAsString();
            int coinAmount = subCommand2.getAsInt();

            AccountSQL accountSQL = new AccountSQL();
            CompletableFuture<Boolean> completableFuture = accountSQL.userExist(loginName);

            try {

                if(!completableFuture.get()) {
                    Objects.requireNonNull(event.getJDA().getTextChannelById(channelID))
                            .sendMessage("The user name was not found in the database")
                            .queue();
                    return;
                }

                CompletableFuture<Integer> completableFuture2 = accountSQL.getCoins(loginName);
                int currentCoins = completableFuture2.get();

                CompletableFuture<Boolean> completableFuture1 = accountSQL.updateCoins(currentCoins + coinAmount, loginName);

                if(completableFuture1.get()) {
                    Objects.requireNonNull(event.getJDA().getTextChannelById(channelID))
                            .sendMessage("Successfully added " + coinAmount + " coins for user " + loginName)
                            .queue();

                    return;

                }

                Objects.requireNonNull(event.getJDA().getTextChannelById(channelID))
                        .sendMessage("Something went wrong while executing this command. Inspect the log for more information's")
                        .queue();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        } catch (NumberFormatException ex) {
            Objects.requireNonNull(event.getJDA().getTextChannelById(channelID))
                    .sendMessage("The coin amount must be an integer")
                    .queue();
        }
    }
}
