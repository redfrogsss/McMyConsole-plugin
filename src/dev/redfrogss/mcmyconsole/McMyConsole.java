package dev.redfrogss.mcmyconsole;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class McMyConsole extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getLogger().info(ChatColor.RED + " Enabled " + this.getName());
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + " Disabled " + this.getName());
    }
}
