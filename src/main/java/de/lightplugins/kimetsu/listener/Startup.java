package de.lightplugins.kimetsu.listener;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Startup extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("[KIMETSU] The bot is now ready to use.");
    }
}
