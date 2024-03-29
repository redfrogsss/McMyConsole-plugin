package dev.redfrogss.mcmyconsole.httpapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.redfrogss.mcmyconsole.classes.InputStreamUtils;
import dev.redfrogss.mcmyconsole.classes.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import java.io.*;

public class CommandHandler implements HttpHandler {

    private Plugin plugin;
    private FileConfiguration config;

    public CommandHandler (Plugin plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
    }

    private void executeCommand (String command) {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run () {
                    Bukkit.getLogger().info("[McMyConsole] Executing Command: " + command);

            //        send command
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            });

        } catch (Exception e) {
            Bukkit.getLogger().info(e.toString());
        }
    }
    public void handle(HttpExchange t) throws IOException {
        String response = "";

//        Check HTTP Method
        String method = t.getRequestMethod();   // value: GET, POST, etc.

        if (!method.equals("POST")) {
            t.sendResponseHeaders(405, 0);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        InputStream is = t.getRequestBody();
        JSONObject requestBodyJSON = new InputStreamUtils(is).toJSON();
        String command = requestBodyJSON.getString("command");
        String username = requestBodyJSON.getString("username");
        String password = requestBodyJSON.getString("password");

        boolean isValidLogin = new User(config).checkUser(username, password);
        int rCode;

        if (isValidLogin) {
            executeCommand(command);
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
