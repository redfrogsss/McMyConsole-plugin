package dev.redfrogss.mcmyconsole.httpapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import java.io.*;

public class CommandHandler implements HttpHandler {

    private Plugin plugin;

    public CommandHandler (Plugin plugin) {
        this.plugin = plugin;
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
        String requestBody = "";
        String response = "";

        InputStream is = t.getRequestBody();

//        convert is to string
        StringBuilder stringBuilder = new StringBuilder();

        new BufferedReader(new InputStreamReader(is))
                .lines()
                .forEach( (String s) -> stringBuilder.append(s + "\n") );

        requestBody = stringBuilder.toString();

//        Check HTTP Method
        String method = t.getRequestMethod();   // value: GET, POST, etc.

        if (!method.equals("POST")) {
            t.sendResponseHeaders(405, 0);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        JSONObject requestBodyJSON = new JSONObject(requestBody);

        String command = requestBodyJSON.getString("command");

        executeCommand(command);

        t.sendResponseHeaders(200, 0);
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
