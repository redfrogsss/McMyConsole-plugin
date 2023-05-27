package dev.redfrogss.mcmyconsole.classes;

import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ServerInfo {
    public String activePlayers, totalPlayers, version, icon, uptime;

    public ServerInfo(long serverStartTimestamp) throws IOException {
        activePlayers = String.valueOf(Bukkit.getServer().getOnlinePlayers().size());
        totalPlayers = String.valueOf(Bukkit.getServer().getMaxPlayers());
        version = Bukkit.getServer().getBukkitVersion().split("-")[0];
        uptime = getUptime(serverStartTimestamp);

        try {
            File fileObject = new File("server-icon.png");
            icon = Base64.getEncoder().encodeToString(Files.readAllBytes(fileObject.toPath()));
        } catch (Exception e) {
//            Bukkit.getLogger().info("Error getting server icon: " + e.toString());
//            If not found, use the default server icon
        }

    }

    private String getUptime(long earlierTimestamp) {
        long laterTimestamp = System.currentTimeMillis();
        long differenceInMilliseconds = laterTimestamp - earlierTimestamp;
        long differenceInSeconds = (differenceInMilliseconds / 1000) % 60;
        long differenceInMinutes = ((differenceInMilliseconds / 1000) / 60) % 60;
        long differenceInHours = ((differenceInMilliseconds / 1000) / 60) / 60;

        String uptime = "" + differenceInHours + "h " + differenceInMinutes + "m " + differenceInSeconds + "s";
        return uptime;
    }

    public JSONObject toJSON () {
        JSONObject json = new JSONObject();
        json.put("activePlayers", activePlayers);
        json.put("totalPlayers", totalPlayers);
        json.put("version", version);
        json.put("icon", icon);
        json.put("uptime", uptime);

        return json;
    }
}
