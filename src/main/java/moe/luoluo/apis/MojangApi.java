package moe.luoluo.apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MojangApi {
    public static String mojang(String arg1, String get) throws IOException, URISyntaxException {
        URI uri;
        URI uri2 = switch (arg1.length()) {
            case 32, 36 -> {
                uri = new URI("https://api.minecraftservices.com/minecraft/profile/lookup/" + arg1);
                yield new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + arg1);
            }
            default -> {

                uri = new URI("https://api.mojang.com/users/profiles/minecraft/" + arg1);
                yield new URI("https://api.minecraftservices.com/minecraft/profile/lookup/name/" + arg1);
            }
        };
        ;
        String result;
        try {
            result = Api.request(uri);
        } catch (IOException e) {
            result = Api.request(uri2);
        }

        if (result.equals("NotFound")) {
            return "NotFound";
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject json = gson.fromJson(result, JsonObject.class);
        if (get.equals("uuid")) {
            return json.get("id").getAsString();
        } else if (get.equals("name")) {
            return json.get("name").getAsString();
        } else throw new IOException();

    }
}
