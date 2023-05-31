package dev.redfrogss.mcmyconsole.httpapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.redfrogss.mcmyconsole.classes.InputStreamUtils;
import dev.redfrogss.mcmyconsole.classes.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ShutdownHandler implements HttpHandler {
    protected Plugin plugin;
    protected FileConfiguration config;

//    constructor
    public ShutdownHandler (Plugin p, FileConfiguration c) {
        plugin = p;
        config = c;
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

        String method = t.getRequestMethod();   // value: GET, POST, etc.
        if (!method.equals("POST")) {
            t.sendResponseHeaders(405, 0);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

//        check auth
        InputStream is = t.getRequestBody();
        JSONObject requestBodyJSON = new InputStreamUtils(is).toJSON();
        String username = requestBodyJSON.getString("username");
        String password = requestBodyJSON.getString("password");

        boolean isValidLogin = new User(config).checkUser(username, password);
        int rCode;

        if (isValidLogin) {
            shutdownServer();
            rCode = 200;
        } else {
            rCode = 401;
        }

        t.sendResponseHeaders(rCode, 0);
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
