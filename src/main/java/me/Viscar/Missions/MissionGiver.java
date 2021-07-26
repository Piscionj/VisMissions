package me.Viscar.Missions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class MissionGiver {

    private static ArrayList<Mission> missionsList = getMissionsFromConfig();
    private static Plugin plugin = Bukkit.getPluginManager().getPlugin("VisMissions");
    // Added so missions aren't stackable
    private static final NamespacedKey randomMissionKey = new NamespacedKey(plugin, "random");


    public static void giveRandomMission(Player player) {
        giveMission(player, missionsList.get((int) (missionsList.size() * Math.random())));
    }

    public static void giveNumberedMission(Player player, int index) {
        if(index >= missionsList.size())
            giveRandomMission(player);
        else
            // If it'll throw an index out of bounds exception, just give a random mission
            giveMission(player, missionsList.get(index));
    }

    private static void giveMission(Player player, Mission missionObj) {
        // Create and prepare mission item
        ItemStack mission = new ItemStack(Material.PAPER, 1);
        ItemMeta im = mission.getItemMeta();

        // Mark data containers for completion status and mission type
        PersistentDataContainer dataContainer = im.getPersistentDataContainer();
        dataContainer.set(MissionUpdater.getCompleteNameSpaceKey(), PersistentDataType.INTEGER, 0);
        dataContainer.set(MissionUpdater.getTypeNameSpaceKey(), PersistentDataType.STRING, missionObj.getMissionType().name());
        dataContainer.set(randomMissionKey, PersistentDataType.DOUBLE, Math.random());

        im.setDisplayName(ChatColor.AQUA + "" + ChatColor.ITALIC + "Mission");
        List<String> loreList = new ArrayList<String>();
        // Informational first line
        loreList.add(ChatColor.DARK_PURPLE + "Hold in your offhand and perform the task to collect your reward!");
        // Mission type line
        String missionType = missionObj.getMissionType().name();
        // Format mission type and add
        missionType = missionType.substring(0,1) + missionType.substring(1).toLowerCase();
        loreList.add(ChatColor.DARK_PURPLE + missionType + " " + missionObj.getMissionObjective() + ": 0/" + missionObj.getRandomAmountRequired());
        // Show difficulty
        loreList.add(ChatColor.DARK_PURPLE + "Difficulty: " + missionObj.getDifficultyColoredString());
        // Set lore and item meta and give to player
        im.setLore(loreList);
        mission.setItemMeta(im);
        player.getInventory().addItem(mission);
    }

    /**
     * Returns a list of all mission types from the config
     */
    public static ArrayList<Mission> getMissionsFromConfig() {
        ArrayList<Mission> missions = new ArrayList<>();
        FileConfiguration config = Bukkit.getPluginManager().getPlugin("VisMissions").getConfig();
        // Load in all mission types from config
        for(Mission.MissionType missionType : Mission.MissionType.values()) {
            for(String mission : config.getStringList(missionType.name().toLowerCase() + "Missions")) {
                // 0 = Difficulty, 1 = Amount, 2 = Mission Objective
                String[] mp = mission.split(",");
                String[] amounts = mp[1].split("-");
                missions.add(new Mission(missionType, Mission.Difficulty.valueOf(mp[0]), Integer.valueOf(amounts[0]),
                        Integer.valueOf(amounts[1]), mp[2]));
            }
        }
        return missions;
    }

    /**
     * Reloads missions from config
     */
    public static void reloadMissionsFromConfig() {
        Bukkit.getPluginManager().getPlugin("VisMissions").reloadConfig();
        missionsList = getMissionsFromConfig();
    }

}
