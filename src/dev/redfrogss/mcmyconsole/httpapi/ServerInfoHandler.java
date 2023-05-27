package dev.redfrogss.mcmyconsole.httpapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.redfrogss.mcmyconsole.classes.ServerInfo;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ServerInfoHandler implements HttpHandler {

    private long serverStartTimestamp;

    public ServerInfoHandler (long startTimestamp) {
        serverStartTimestamp = startTimestamp;
    }


    public void handle(HttpExchange t) throws IOException {
        ServerInfo info = new ServerInfo(serverStartTimestamp);

        String response = info.toJSON().toString();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
