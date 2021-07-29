package me.Viscar.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class TabComplete implements TabCompleter {
    public static final List<String> adminCommands = Arrays.asList("give", "reload");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1 && sender.hasPermission("vismissions.admin"))
            return adminCommands;

        return null;
    }
}
