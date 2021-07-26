package me.Viscar.Listeners;

import me.Viscar.Missions.MissionUpdater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemCraftEvent implements Listener {

    public ItemCraftEvent() {

    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player player = (Player) e.getInventory().getViewers().get(0);
        ItemStack result = e.getRecipe().getResult();
        MissionUpdater.onCraft(player, result);
    }
}
