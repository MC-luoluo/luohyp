package moe.luoluo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class api {
    public static String mojang(String arg1, String type) throws IOException, URISyntaxException {
        URI uri = switch (type) {
            case "name" -> new URI("https://api.mojang.com/users/profiles/minecraft/" + arg1);
            case "uuid" -> new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + arg1);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject json = gson.fromJson(request(uri), JsonObject.class);
        if (type.equals("name")){
            return json.get("id").getAsString();
        } else {
            return json.get("name").getAsString();
        }

    }

    public static String hypixel(String type, String uuid) throws URISyntaxException, IOException {
        URI uri = switch (type) {
            case "guild" ->
                    new URI("https://api.hypixel.net/" + type + "?key=" + config.INSTANCE.getHypixelAPI() + "&player=" + uuid);
            default ->
                    new URI("https://api.hypixel.net/" + type + "?key=" + config.INSTANCE.getHypixelAPI() + "&uuid=" + uuid);
        };

        return request(uri);
    }

    public static String request(URI uri) throws IOException {
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
        }catch (IOException e) {
            return String.valueOf(e);
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }
}
