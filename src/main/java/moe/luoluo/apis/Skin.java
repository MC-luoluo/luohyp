package moe.luoluo.apis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import static moe.luoluo.apis.Api.request;

public class Skin {
    public static byte[] render(String uuid) throws URISyntaxException, IOException {
        URI uri = new URI("https://nmsr.nickac.dev/fullbody/" + uuid);
        return skin(uri);
    }

    public static JsonObject getProfile(String uuid) throws IOException, URISyntaxException {
        URI mcs = new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        JsonObject json = new Gson().fromJson(request(mcs), JsonObject.class);
        String value = json.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();

        // 获取一个Base64解码器
        Base64.Decoder decoder = Base64.getDecoder();

        // 将Base64编码的字符串解码回字节数组
        byte[] decodedBytes = decoder.decode(value);

        // 将字节数组转换回字符串
        return new Gson().fromJson(new String(decodedBytes), JsonObject.class);
    }

    public static URI getSkinUri(JsonObject profile) throws URISyntaxException {
        return new URI(profile.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString());
    }

    public static URI getCapeUri(JsonObject profile) throws URISyntaxException {
        if (profile.get("textures").getAsJsonObject().has("CAPE"))
            return new URI(profile.get("textures").getAsJsonObject().get("CAPE").getAsJsonObject().get("url").getAsString());
        else return null;
    }

    public static byte[] texture(String type, String uuid) throws URISyntaxException, IOException {
        if (type.equals("skin")) {
            return skin(getSkinUri(getProfile(uuid)));
        } else if (type.equals("cape")) {
            return skin(getCapeUri(getProfile(uuid)));
        } else return null;
    }

    public static byte[] skin(URI uri) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        InputStream is = conn.getInputStream();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[6024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        is.close();

        return outStream.toByteArray();
    }
}
