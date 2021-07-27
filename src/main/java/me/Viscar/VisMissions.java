package me.Viscar;

import me.Viscar.Commands.Commands;
import me.Viscar.Commands.TabComplete;
import me.Viscar.Listeners.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class VisMissions extends JavaPlugin {

    private Economy econ;
    private Logger log;

    public void onEnable() {
        log = getLogger();
        log.info("[VisMissions] Booting up ... ");
        saveDefaultConfig();
        if (!setupEconomy())
            log.severe(String.format("[%s] - Disabled mission redeeming due to no Vault dependency found!", getDescription().getName()));

        registerListeners();

        this.getCommand("vm").setExecutor((CommandExecutor) new Commands());
        this.getCommand("vm").setTabCompleter((TabCompleter) new TabComplete());
        log.info("[VisMissions] Finished loading!");
    }

    public void onDisable() {
        log.info("[VisMissions] Shutting down ... ");
    }

    public void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new FishCatchListener(), this);
        pluginManager.registerEvents(new EntityKillListener(), this);
        pluginManager.registerEvents(new ItemCraftEvent(), this);
        pluginManager.registerEvents(new MiningListener(), this);
        pluginManager.registerEvents(new SmeltingListener(), this);
        pluginManager.registerEvents(new TamingListener(), this);
        pluginManager.registerEvents(new PlantListener(), this);
        if(econ != null)
        pluginManager.registerEvents(new MissionRedeemListener(econ), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
