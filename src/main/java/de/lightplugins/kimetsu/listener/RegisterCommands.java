package de.lightplugins.kimetsu.listener;

import de.lightplugins.kimetsu.enums.OptionDataPath;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RegisterCommands extends ListenerAdapter {


    public void onGuildReady(@NotNull GuildReadyEvent event) {

        List<CommandData> commandData = new ArrayList<>();

        /* Coins command */

        OptionData optionData1 = new OptionData(OptionType.STRING, OptionDataPath.COINS_ADD.getName(),
                "Add coins to a specify user", true);
        OptionData optionData2 = new OptionData(OptionType.STRING, OptionDataPath.COINS_SET_LOGIN_NAME.getName(),
                "The account name from the user", true);
        OptionData optionData3 = new OptionData(OptionType.INTEGER, OptionDataPath.COINS_SET_AMOUNT.getName(),
                "The amount of coins the user get", true);
        commandData.add(Commands.slash("coins",
                "Update the coins from a specify user").addOptions(optionData1, optionData2, optionData3));

        /* ban command */

        OptionData optionData4 = new OptionData(OptionType.STRING, OptionDataPath.BAN_LOGIN_NAME.getName(),
                "The account name from the user", true);
        OptionData optionData5 = new OptionData(OptionType.STRING, OptionDataPath.BAN_REASON.getName(),
                "The account name from the user", true);
        commandData.add(Commands.slash("ban",
                "ban an HWID from a specify user").addOptions(optionData4, optionData5));

        /* unban command */

        OptionData optionData6 = new OptionData(OptionType.STRING, OptionDataPath.BAN_LOGIN_NAME.getName(),
                "The account name from the user", true);
        commandData.add(Commands.slash("unban",
                "Unban an HWID from a specify user").addOptions(optionData6));

        /* debug command */

        OptionData optionData7 = new OptionData(OptionType.STRING, OptionDataPath.BAN_LOGIN_NAME.getName(),
                "The account name from the user", true);
        commandData.add(Commands.slash("debug",
                "Debug a player").addOptions(optionData7));



        event.getGuild().updateCommands().addCommands(commandData).queue(
                callback -> System.out.println("[KIMETSU] Successfully registered commands"));


    }

}
