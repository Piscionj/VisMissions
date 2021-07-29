package me.Viscar.Listeners;

import me.Viscar.Missions.MissionDifficulty;
import me.Viscar.Missions.MissionUpdater;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MissionRedeemListener implements Listener {

    private Economy economy;
    private static double easyReward;
    private static double mediumReward;
    private static double hardReward;

    public MissionRedeemListener(Economy economy) {
        this.economy = economy;
        reloadRewardValues();
    }

    public static void reloadRewardValues() {
        FileConfiguration config = Bukkit.getPluginManager().getPlugin("VisMissions").getConfig();
        easyReward = config.getDouble("easyReward");
        mediumReward = config.getDouble("mediumReward");
        hardReward = config.getDouble("hardReward");
    }

    @EventHandler
    public void onRedeem(PlayerInteractEvent e) {
        Action action = e.getAction();
        if(action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return;
        Player player = e.getPlayer();
        ItemStack mission = player.getInventory().getItemInHand();
        if(mission == null || mission.getType().equals(Material.AIR))
            return;
        ItemMeta im = mission.getItemMeta();
        PersistentDataContainer dataContainer = im.getPersistentDataContainer();
        if(!dataContainer.has(MissionUpdater.getCompleteNameSpaceKey(), PersistentDataType.INTEGER)
                || dataContainer.get(MissionUpdater.getCompleteNameSpaceKey(), PersistentDataType.INTEGER) != 1)
            return;
        MissionDifficulty difficulty = MissionDifficulty.valueOf(im.getLore().get(2).substring(16));
        double amountToGive = 0;
        switch(difficulty) {
            case EASY:
                amountToGive = easyReward;
                break;
            case MEDIUM:
                amountToGive = mediumReward;
                break;
            case HARD:
                amountToGive = hardReward;
                break;
        }
        if(amountToGive != 0) {
            economy.depositPlayer(player, amountToGive);
            player.sendMessage(ChatColor.AQUA + "You have redeemed $" + amountToGive + " for completing this mission!");
            mission.setAmount(0);
        }
    }
}
