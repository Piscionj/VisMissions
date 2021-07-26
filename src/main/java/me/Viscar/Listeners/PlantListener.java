package me.Viscar.Listeners;

import me.Viscar.Missions.MissionUpdater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlantListener implements Listener {

    public PlantListener() {

    }

    @EventHandler
    public void onPlant(BlockPlaceEvent e) {
        MissionUpdater.onPlant(e.getPlayer(), e.getBlock());
    }
}
