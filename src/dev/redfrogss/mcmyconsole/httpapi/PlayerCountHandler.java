package dev.redfrogss.mcmyconsole.httpapi;

import org.json.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PlayerCountHandler implements HttpHandler {

    int playerCount = 0;
    int maxPlayerCount = 0;

    public void handle(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();
        is.read();

        playerCount = Bukkit.getServer().getOnlinePlayers().size();
        maxPlayerCount = Bukkit.getServer().getMaxPlayers();

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("playerCount", playerCount);
        jsonResponse.put("maxPlayerCount", maxPlayerCount);

        String response = jsonResponse.toString();
        Bukkit.getLogger().info("Responding: " + response);
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
