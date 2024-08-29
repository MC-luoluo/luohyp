package moe.luoluo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class Api {
    private static final Logger logger = LoggerFactory.getLogger(Api.class);

    public static String mojang(String arg1, String get) throws IOException, URISyntaxException {
        URI uri = switch (arg1.length()) {
            case 32, 36 -> new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + arg1);
            default -> new URI("https://api.mojang.com/users/profiles/minecraft/" + arg1);
        };
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject json = gson.fromJson(request(uri), JsonObject.class);
        if (get.equals("uuid")) {
            return json.get("id").getAsString();
        } else if (get.equals("name")) {
            return json.get("name").getAsString();
        } else return null;

    }

    public static String hypixel(String type) throws URISyntaxException, IOException {
        URI uri = new URI("https://api.hypixel.net/" + type + "?key=" + config.INSTANCE.getHypixelAPI());
        return request(uri);
    }

    public static String hypixel(String type, String uuid) throws URISyntaxException, IOException {
        URI uri = new URI("https://api.hypixel.net/" + type + "?key=" + config.INSTANCE.getHypixelAPI() + "&uuid=" + uuid);
        return request(uri);
    }

    public static String hypixel(String type, String value, String parameter) throws URISyntaxException, IOException {
        URI uri = new URI("https://api.hypixel.net/" + type + "?key=" + config.INSTANCE.getHypixelAPI() + "&" + parameter + "=" + value);
        return request(uri);
    }

    public static String guild(String type, String arg2) throws URISyntaxException, IOException {
        URI uri = new URI("https://api.hypixel.net/guild?key=" + config.INSTANCE.getHypixelAPI() + "&" + type + "=" + URLEncoder.encode(arg2, StandardCharsets.UTF_8));
        return request(uri);
    }

    public static String request(URI uri) throws MalformedURLException {
        URL url = uri.toURL();
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Linux x86_64; rv:129.0)");
            connection.connect();

            String line;
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            return String.valueOf(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("An error occurred", e2);
            }
        }
        return result.toString();
    }
}
