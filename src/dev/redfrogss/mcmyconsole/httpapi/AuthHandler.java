package dev.redfrogss.mcmyconsole.httpapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.redfrogss.mcmyconsole.classes.InputStreamUtils;
import dev.redfrogss.mcmyconsole.classes.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.JSONObject;

import java.io.*;

public class AuthHandler implements HttpHandler {

    FileConfiguration config;

    public AuthHandler (FileConfiguration config) {
        this.config = config;
    }

    public void handle(HttpExchange t) throws IOException {
        String username, password;
        String response = "";

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
        username = requestBodyJSON.getString("username");
        password = requestBodyJSON.getString("password");

        boolean isValidLogin = new User(config).checkUser(username, password);
        int rCode;

        if (isValidLogin) {
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