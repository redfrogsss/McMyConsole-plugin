package dev.redfrogss.mcmyconsole.httpapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerHandler implements HttpHandler {

    protected Plugin plugin;

    public PlayerHandler(Plugin p) {
        plugin = p;
    }

    private void kickPlayerInGame (String playerName, String reason) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equals(playerName)) {
                Bukkit.getLogger().info("Kicked Player " + playerName);
                try {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run () {
                            p.kickPlayer(reason);
                        }
                    });

                } catch (Exception e) {
                    Bukkit.getLogger().info(e.toString());
                }
            }
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

        if (!(method.equals("POST") || method.equals("GET"))) {
            t.sendResponseHeaders(405, 0);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

//        Handle GET Request
        if (method.equals("GET")){
//            return a list of player name
            List<String> playerList = new ArrayList<String>();
            JSONObject responseJSON = new JSONObject();

            final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (Player player : players) {
                // If a player is hidden from the sender don't show them in the list
                playerList.add(player.getDisplayName());
            }

            responseJSON.put("playerList", playerList);

            response = responseJSON.toString();

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

//        Handle POST Request
        if (method.equals("POST")) {
            JSONObject requestBodyJSON = new JSONObject(requestBody);

//            Player player = Bukkit.getPlayer(requestBodyJSON.getString("player"));
            String playerName = requestBodyJSON.getString("player");
            String action = requestBodyJSON.getString("action");

            if (!(action.equals("kick") || action.equals("ban") || action.equals("ban-ip"))) {
                response = "Invalid `action` in request body.";

                t.sendResponseHeaders(400, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            Bukkit.getLogger().info("Player: " + playerName);
            Bukkit.getLogger().info("Action: " + action);

            if (action.equals("kick")) {
                Bukkit.getLogger().info("Kicking Player " + playerName);
                String reason = "You have been kicked.";

                kickPlayerInGame(playerName, reason);

//                for (Player p : Bukkit.getOnlinePlayers()) {
//                    if (p.getName().equals(playerName)) {
//                        Bukkit.getLogger().info("Kicked Player " + playerName);
//                        try {
////                            p.kickPlayer(reason);
//                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//                                @Override
//                                public void run () {
//                                    p.kickPlayer(reason);
//                                }
//                            });
//
//                        } catch (Exception e) {
//                            Bukkit.getLogger().info(e.toString());
//                        }
//                    }
//                }
//

                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            if (action.equals("ban")) {
                String reason = "You have been banned.";
                Bukkit.getBanList(BanList.Type.NAME).addBan(playerName, reason, null, null);
                kickPlayerInGame(playerName, reason);

                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            if (action.equals("ban-ip")) {
                String reason = "Your IP has been banned.";
                Bukkit.getBanList(BanList.Type.IP).addBan(playerName, reason, null, null);
                kickPlayerInGame(playerName, reason);

                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }
        }
    }
}
