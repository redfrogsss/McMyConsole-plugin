package dev.redfrogss.mcmyconsole.httpapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.OutputStream;

public class ShutdownHandler implements HttpHandler {
    protected Plugin plugin;

//    constructor
    public ShutdownHandler (Plugin p) {
        plugin = p;
    }


    private void shutdownServer () {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run () {
                    Bukkit.getLogger().info("[MyMcConsole] Shutting Down Server...");
                    Bukkit.getServer().shutdown();
                }
            });

        } catch (Exception e) {
            Bukkit.getLogger().info(e.toString());
        }
    }

    public void handle(HttpExchange t) throws IOException {
        String response = "";
        shutdownServer();
        t.sendResponseHeaders(200, 0);
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
