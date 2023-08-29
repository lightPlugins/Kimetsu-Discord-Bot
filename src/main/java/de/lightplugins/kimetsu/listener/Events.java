package de.lightplugins.kimetsu.listener;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Events extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        Emoji emoji = event.getReaction().getEmoji();
        String channelMention = event.getChannel().getAsMention();
        String jumpLink = event.getJumpUrl();

        if(user == null) {
            return;
        }

        String message = user.getAsMention() + " reacted to a message with " + emoji.getAsReactionCode() + " in the " + channelMention + " channel";
        event.getChannel().sendMessage(message).queue();

    }

    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {

        User user = event.getUser();
        String message = user.getAsMention() + " updated their online status to " + event.getNewOnlineStatus().getKey() + "!";
        long id = 1087099618396999802L;
        TextChannel channel = event.getJDA().getTextChannelById(id);

        if(channel == null) {
            return;
        }

        channel.sendMessage(message).queue();

    }
}
