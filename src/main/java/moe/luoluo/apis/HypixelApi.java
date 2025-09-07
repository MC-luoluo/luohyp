package moe.luoluo.apis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import moe.luoluo.Config;
import moe.luoluo.Data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HypixelApi {
    public static ApiResult general(String type) throws URISyntaxException, IOException {
        return general(type, "", "");
    }

    public static ApiResult general(String type, String uuid) throws URISyntaxException, IOException {
        return general(type, "uuid", uuid);
    }

    public static ApiResult playerStatus (String uuid) throws URISyntaxException, IOException {
        return general("player", "uuid", uuid);
    }

    public static ApiResult general(String type, String parameter, String value) throws URISyntaxException, IOException {
        URI uri = new URI("https://api.hypixel.net/" + type + "?key=" + Config.INSTANCE.getHypixelAPIkey() + "&" + parameter + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8));
        JsonObject result;
        String s = "";
        try {
            s = Api.request(uri);
            result = new Gson().fromJson(s, JsonObject.class);
        } catch (JsonSyntaxException | IOException e) {
            Api.logger.warn("请求结果: {}", s);
            if (!Data.getHypixelData(type, value).equals("null")) {
                Api.logger.error("请求失败，返回本地缓存", e);
                return new ApiResult(new Gson().fromJson(Data.getHypixelData(type, value), JsonObject.class), Long.parseLong(Data.getHypixelDataTime(type, value)));
            }
            if (Config.INSTANCE.getHypixelAPIkey().isEmpty()) {
                throw new JsonSyntaxException("HypixelAPIkey为空，请前往配置文件填写HypixelAPIkey");
            }
            throw new JsonSyntaxException(e);
        }
        Data.setHypixelData(type, value, result);
        return new ApiResult(result, -1);
    }
}
