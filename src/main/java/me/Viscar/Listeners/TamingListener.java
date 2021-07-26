package me.Viscar.Listeners;

import me.Viscar.Missions.MissionUpdater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

public class TamingListener implements Listener {

    public TamingListener() {

    }

    @EventHandler
    public void onTame(EntityTameEvent e) {
        // Not sure when this might be true, but just in case
        if(!(e.getOwner() instanceof Player))
            return;
        MissionUpdater.onTame((Player) e.getOwner(), e.getEntity());
    }
}
