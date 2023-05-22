package dev.redfrogss.mcmyconsole.httpapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import static java.lang.module.ModuleDescriptor.read;

public class MyHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException{
        InputStream is = t.getRequestBody();
        is.read();

        String response = "This is the response";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
