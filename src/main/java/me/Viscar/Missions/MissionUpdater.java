package me.Viscar.Missions;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class MissionUpdater {

    private static Plugin plugin = Bukkit.getPluginManager().getPlugin("VisMissions");
    // Used on data containers to test if it is a completed mission or not, 0 = not complete, 1 = complete
    private static final NamespacedKey missionCompKey = new NamespacedKey(plugin, "missionComplete");
    // Used on data containers to store what kind of mission it is
    private static final NamespacedKey missionTypeKey = new NamespacedKey(plugin, "missionType");
    private static CoreProtectAPI coreProtectAPI = getCoreProtect();

    public static void onMine(Player player, Block block) {
        ItemStack offHand = player.getInventory().getItemInOffHand();
        // Check it's a catch mission
        if(!isUncompleteMissionType(offHand, "MINE"))
            return;
        String toMineLine = offHand.getItemMeta().getLore().get(1);
        if(!block.getType().name().equals(toMineLine.substring(7,toMineLine.indexOf(":")).toUpperCase().replace(" ", "_")))
            return;
        // Check block with CoreProtect (if loaded)
        if(coreProtectAPI != null)
            if(!coreProtectAPI.blockLookup(block, 86400).isEmpty())
                return;

        incrementNum(offHand, 1);
    }

    public static void onSmelt(Player player, ItemStack result) {
        onMissionEvent(player, "SMELT", result.getAmount(), result.getType().name(), true);
    }

    public static void onTame(Player player, Entity tamed) {
        onMissionEvent(player, "TAME", 1, tamed.getName(), false);
    }

    public static void onCraft(Player player, ItemStack crafted) {
        onMissionEvent(player, "CRAFT", crafted.getAmount(), crafted.getType().name(), true);
    }

    public static void onKill(Player player, Entity entityKilled) {
        onMissionEvent(player, "KILL", 1, entityKilled.getName(), false);
    }

    public static void onCatch(Player player, Entity itemCaught) {
        onMissionEvent(player, "CATCH", 1, itemCaught.getName(), false);
    }

    public static void onPlant(Player player, Block block) {
        onMissionEvent(player, "PLANT", 1, block.getType().name(), true);
    }

    /**
     * Mission event helper method
     */
    public static void onMissionEvent(Player player, String missionType, int incrementAmount, String missionObj, boolean upperCase) {
        ItemStack offHand = player.getInventory().getItemInOffHand();
        // Check it's a catch mission
        if(!isUncompleteMissionType(offHand, missionType))
            return;
        String missionStatusLine = offHand.getItemMeta().getLore().get(1);
        // Check the mission object on the paper
        int indexOne = missionType.length() + 3;
        String str = missionStatusLine.substring(indexOne,missionStatusLine.indexOf(":"));
        if(upperCase)
            str = str.toUpperCase().replace(" ", "_");
        if(!missionObj.equals(str))
            return;
        // Else, increment the number on the mission
        incrementNum(offHand, incrementAmount);
    }

    private static void incrementNum(ItemStack mission, int incAmount) {
        ItemMeta im = mission.getItemMeta();
        List<String> loreList = im.getLore();
        // Get line containing the progress of the mission
        String numClearedLine = loreList.get(1);
        // Parse line
        int colonIndex = numClearedLine.indexOf(":") + 4;
        String[] numbers = numClearedLine.substring(colonIndex).split("/");
        int numDone = Integer.valueOf(numbers[0]);
        int numRequired = Integer.valueOf(numbers[1]);
        numDone += incAmount;
        // Check if completed
        if(numDone >= numRequired) {
            im.getPersistentDataContainer().set(missionCompKey, PersistentDataType.INTEGER, 1);
            String completedLine = numClearedLine.substring(0, colonIndex) + ChatColor.GREEN + "RIGHT CLICK TO REDEEM";
            loreList.set(1, completedLine);
            im.setLore(loreList);
            mission.setItemMeta(im);
            return;
        }
        // If not completed - Increment number done and replace
        numClearedLine = numClearedLine.replaceFirst(numbers[0], "" + numDone);
        loreList.set(1, numClearedLine);
        im.setLore(loreList);
        mission.setItemMeta(im);
    }

    public static boolean isUncompleteMissionType(ItemStack itemStack, String missionType) {
        if(itemStack == null || itemStack.getType().equals(Material.AIR))
            return false;
        // Check it's a mission
        PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        if(!dataContainer.has(missionCompKey, PersistentDataType.INTEGER)
                || dataContainer.get(missionCompKey, PersistentDataType.INTEGER) != 0)
            return false;
        // Check the mission type
        if(!dataContainer.get(missionTypeKey, PersistentDataType.STRING).equals(missionType))
            return false;
        return true;
    }

    public static NamespacedKey getCompleteNameSpaceKey() {
        return missionCompKey;
    }

    public static NamespacedKey getTypeNameSpaceKey() {
        return missionTypeKey;
    }

    private static CoreProtectAPI getCoreProtect() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("CoreProtect");

        // Check that CoreProtect is loaded
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 6) {
            return null;
        }

        return CoreProtect;
    }
}
