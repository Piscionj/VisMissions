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
    private static ChatColor nameColor = getNameColor();
    private static ChatColor loreColor = getLoreColor();
    private static ChatColor countColor = getCountColor();
    private static Plugin plugin = Bukkit.getPluginManager().getPlugin("VisMissions");
    // Added so missions aren't stackable
    private static final NamespacedKey randomMissionKey = new NamespacedKey(plugin, "random");


    public static void giveRandomMission(Player player) {
        giveMission(player, missionsList.get((int) (missionsList.size() * Math.random())));
    }

    public static void giveNumberedMission(Player player, int index) {
        giveMission(player, missionsList.get(index % missionsList.size()));
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

        im.setDisplayName(nameColor + "" + ChatColor.ITALIC + "Mission");
        List<String> loreList = new ArrayList<String>();
        // Informational first line
        loreList.add(loreColor + "Hold in your offhand and perform the task to collect your reward!");
        // Mission type line
        String missionType = missionObj.getMissionType().name();
        // Format mission type and add
        missionType = missionType.substring(0,1) + missionType.substring(1).toLowerCase();
        loreList.add(loreColor + missionType + " " + missionObj.getMissionObjective() + ":" + countColor + " 0/" + missionObj.getRandomAmountRequired());
        // Show difficulty
        loreList.add(loreColor + "Difficulty: " + missionObj.getDifficultyColoredString());
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
        // Set missions colors
        nameColor = getNameColor();
        loreColor = getLoreColor();
        countColor = getCountColor();

        // Load in all mission types from config
        for(MissionType missionType : MissionType.values()) {
            for(String mission : config.getStringList(missionType.name().toLowerCase() + "Missions")) {
                // 0 = Difficulty, 1 = Amount, 2 = Mission Objective
                String[] mp = mission.split(",");
                String[] amounts = mp[1].split("-");
                missions.add(new Mission(missionType, MissionDifficulty.valueOf(mp[0]), Integer.valueOf(amounts[0]),
                        Integer.valueOf(amounts[1]), mp[2]));
            }
        }
        return missions;
    }

    public static ChatColor getNameColor() {
        FileConfiguration config = Bukkit.getPluginManager().getPlugin("VisMissions").getConfig();
        ChatColor nC = ChatColor.valueOf(config.getString("nameColor"));
        return nC == null ? ChatColor.AQUA : nC;
    }
    public static ChatColor getLoreColor() {
        FileConfiguration config = Bukkit.getPluginManager().getPlugin("VisMissions").getConfig();
        ChatColor lC = ChatColor.valueOf(config.getString("loreColor"));
        return lC == null ? ChatColor.AQUA : lC;
    }
    public static ChatColor getCountColor() {
        FileConfiguration config = Bukkit.getPluginManager().getPlugin("VisMissions").getConfig();
        ChatColor cC = ChatColor.valueOf(config.getString("countColor"));
        return cC == null ? ChatColor.AQUA : cC;
    }

    /**
     * Reloads missions from config
     */
    public static void reloadMissionsFromConfig() {
        Bukkit.getPluginManager().getPlugin("VisMissions").reloadConfig();
        missionsList = getMissionsFromConfig();
    }

}
