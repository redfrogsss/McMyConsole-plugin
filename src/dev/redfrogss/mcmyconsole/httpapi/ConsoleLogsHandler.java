package dev.redfrogss.mcmyconsole.httpapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.redfrogss.mcmyconsole.classes.ServerInfo;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class ConsoleLogsHandler implements HttpHandler {

    ArrayList<String> consoleLogs;

    public ConsoleLogsHandler (ArrayList<String> consoleLogs) {
        this.consoleLogs = consoleLogs;
    }

    public JSONObject toJSON () {
        JSONObject json = new JSONObject();

        json.put("consoleLogs", consoleLogs);

        return json;
    }

    public void handle(HttpExchange t) throws IOException {
        String response = this.toJSON().toString();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
