package dev.redfrogss.mcmyconsole;

import com.sun.net.httpserver.HttpServer;
import dev.redfrogss.mcmyconsole.classes.LogAppender;
import dev.redfrogss.mcmyconsole.classes.ServerInfo;
import dev.redfrogss.mcmyconsole.httpapi.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

public class McMyConsole extends JavaPlugin {

    private long serverStartTimestamp;

    private static final org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();

    public ArrayList<String> consoleLogs = new ArrayList<String>();
    @Override
    public void onEnable() {

        LogAppender appender = new LogAppender(consoleLogs);
        logger.addAppender(appender);

        serverStartTimestamp = System.currentTimeMillis();

        this.getCommand("mcmyconsole").setExecutor(new CommandMcmyconsole());

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
            server.createContext("/consoleLogs", new ConsoleLogsHandler(consoleLogs));
            server.createContext("/command", new CommandHandler(this));

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
