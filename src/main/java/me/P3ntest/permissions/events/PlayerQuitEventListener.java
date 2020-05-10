package me.P3ntest.permissions.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventListener implements Listener {

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (PlayerLoginEventListener.playerTeams.containsKey(event.getPlayer())) {
            PlayerLoginEventListener.playerTeams.get(event.getPlayer()).unregister();
            PlayerLoginEventListener.playerTeams.remove(event.getPlayer());
        }
    }

}
