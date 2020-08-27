package VMCommands;

import VMMissions.MissionGiver;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        // Permissions check
        if(!sender.hasPermission("vismissions.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to use this command!");
            return false;
        }
        // Reload command: /vm reload
        if(args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.AQUA + "Reloading Mission config ...");
            MissionGiver.reloadMissionsFromConfig();
            sender.sendMessage(ChatColor.AQUA + "Reloading complete!");
        }
        // Give player command: /vm give <player>
        else if(args.length >= 2 && args[0].equalsIgnoreCase("give")) {
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null) {
                sender.sendMessage(ChatColor.RED + "Player '" + args[1] + "' does not exist");
                return false;
            }
            if(args.length >= 3 && StringUtils.isNumeric(args[2]))
                MissionGiver.giveNumberedMission(player, Integer.valueOf(args[2]));
            else
                MissionGiver.giveRandomMission(player);
            sender.sendMessage(ChatColor.AQUA + "Mission given to " + player.getName());
        }
        return true;
    }
}
