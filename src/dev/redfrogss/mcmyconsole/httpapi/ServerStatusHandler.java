package dev.redfrogss.mcmyconsole.httpapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

public class ServerStatusHandler implements HttpHandler {
    double[] tps;
    double freeMemory = 0;
    double currentMemory = 0;
    double maxMemory = 0;

    private static Object minecraftServer;

    private static Field recentTps;

    private static double[] getRecentTpsRefl() throws Throwable {

        if (minecraftServer == null) {
            Server server = Bukkit.getServer();
            Field consoleField = server.getClass().getDeclaredField("console");
            consoleField.setAccessible(true);
            minecraftServer = consoleField.get(server);
        }
        if (recentTps == null) {
            recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
            recentTps.setAccessible(true);
        }
        return (double[]) recentTps.get(minecraftServer);
    }

    public void handle(HttpExchange t) throws IOException {
        try {
            tps = getRecentTpsRefl();
            freeMemory = (double)(Runtime.getRuntime().freeMemory() / 1048576);
            maxMemory = (double)(Runtime.getRuntime().maxMemory() / 1048576);

            currentMemory = (double)(maxMemory - freeMemory);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("tps", tps);
            jsonResponse.put("freeMemory", freeMemory);
            jsonResponse.put("currentMemory", currentMemory);
            jsonResponse.put("maxMemory", maxMemory);

            String response = jsonResponse.toString();
            Bukkit.getLogger().info("Responding: " + response);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Throwable error) {
            String response = error.toString();
            Bukkit.getLogger().info("Responding: " + response);
            t.sendResponseHeaders(500, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
