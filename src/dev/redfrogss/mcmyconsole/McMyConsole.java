package dev.redfrogss.mcmyconsole;

import com.sun.net.httpserver.HttpServer;
import dev.redfrogss.mcmyconsole.httpapi.MyHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;

public class McMyConsole extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getLogger().info(" Enabled " + this.getName());

        this.getCommand("mcmyconsole").setExecutor(new CommandMcmyconsole());

        // http server
        HttpServer server = null;
        int port = 8003;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
//            server.createContext("/applications/myapp", new MyHandler());
            server.createContext("/test", new MyHandler());
            server.setExecutor(null); // creates a default executor
            server.start();

            // success msg
            Bukkit.getLogger().info("API Server is listening to port " + port);
        } catch (IOException e) {
            Bukkit.getLogger().info("API Server failed to start.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + " Disabled " + this.getName());
    }
}
