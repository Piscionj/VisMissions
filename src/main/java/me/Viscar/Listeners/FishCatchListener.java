package me.Viscar.Listeners;

import me.Viscar.Missions.MissionUpdater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishCatchListener implements Listener {

    public FishCatchListener() {

    }

    @EventHandler
    public void onCatch(PlayerFishEvent e) {
        if(e.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;
        MissionUpdater.onCatch(e.getPlayer(), e.getCaught());
    }
}
