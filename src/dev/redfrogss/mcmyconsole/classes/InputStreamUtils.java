package dev.redfrogss.mcmyconsole.classes;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamUtils {
    InputStream is;
    public InputStreamUtils (InputStream is) {
        this.is = is;
    }

    public JSONObject toJSON() {
        StringBuilder stringBuilder = new StringBuilder();

        new BufferedReader(new InputStreamReader(is))
                .lines()
                .forEach( (String s) -> stringBuilder.append(s + "\n") );

        String requestBody = stringBuilder.toString();

        return new JSONObject(requestBody);
    }
}
