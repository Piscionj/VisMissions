package VMListeners;

import VMMissions.MissionUpdater;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashSet;

public class MiningListener implements Listener {

    private HashSet<Block> playerPlaced = new HashSet<>();

    public MiningListener() {

    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
//        Bukkit.broadcastMessage("Block type string : " + block.getType().toString());
//        Bukkit.broadcastMessage("Block type string : " + block.getType().name());
    }

    @EventHandler
    public void onMine(BlockBreakEvent e) {
        Player player = e.getPlayer();
        MissionUpdater.onMine(player, e.getBlock());
    }
}
