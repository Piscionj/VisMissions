package VMListeners;

import VMMissions.MissionUpdater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.inventory.ItemStack;

public class SmeltingListener implements Listener {

    public SmeltingListener() {

    }

    @EventHandler
    public void onSmelt(FurnaceExtractEvent e) {
        MissionUpdater.onSmelt(e.getPlayer(), new ItemStack(e.getItemType(), e.getItemAmount()));
    }
}
