package dev.redfrogss.mcmyconsole;

import com.sun.net.httpserver.HttpServer;
import dev.redfrogss.mcmyconsole.classes.ServerInfo;
import dev.redfrogss.mcmyconsole.httpapi.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;

public class McMyConsole extends JavaPlugin {

    private long serverStartTimestamp;
    @Override
    public void onEnable() {

        this.getCommand("mcmyconsole").setExecutor(new CommandMcmyconsole());

        serverStartTimestamp = System.currentTimeMillis();

        // http server
        HttpServer server = null;
        int port = 8003;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext("/test", new MyHandler());
            server.createContext("/playerCount", new PlayerCountHandler());
            server.createContext("/player", new PlayerHandler(this));
            server.createContext("/shutdown", new ShutdownHandler(this));
            server.createContext("/serverStatus", new ServerStatusHandler());
            server.createContext("/serverInfo", new ServerInfoHandler(serverStartTimestamp));

            server.setExecutor(null); // creates a default executor
            server.start();

            // success msg
            Bukkit.getLogger().info("[McMyConsole] API Server is listening to port " + port);
        } catch (IOException e) {
            Bukkit.getLogger().info("[McMyConsole] API Server failed to start.");
            throw new RuntimeException(e);
        }

        Bukkit.getLogger().info("[McMyConsole] Enabled " + this.getName());
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + " Disabled " + this.getName());
    }
}
