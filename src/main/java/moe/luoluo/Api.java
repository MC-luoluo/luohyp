package moe.luoluo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;


public class Api {
    private static final Logger logger = LoggerFactory.getLogger(Api.class);

    public static String mojang(String arg1, String get) throws IOException, URISyntaxException {
        URI uri = switch (arg1.length()) {
            case 32, 36 -> new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + arg1);
            default -> new URI("https://api.mojang.com/users/profiles/minecraft/" + arg1);
        };
        String result = request(uri);
        /*if (result.isEmpty()) {
            return "";
        } else if (result.startsWith("java.net.ConnectException:")) {
            return "TimedOut";
        } else if (result.startsWith("java.io.FileNotFoundException:")) {
            return "NotFound";
        } else if (result.startsWith("java.io.IOException:")) {
            return "IO";
        } else if (result.startsWith("java.net.SocketException:")) {
            return "reset";
        } else if (result.startsWith("javax.net.ssl.SSLHandshakeException:")) {
            return "sslEr";
        }*/
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject json = gson.fromJson(result, JsonObject.class);
        if (get.equals("uuid")) {
            return json.get("id").getAsString();
        } else if (get.equals("name")) {
            return json.get("name").getAsString();
        } else return "null";

    }

    public static ApiResult hypixel(String type) throws URISyntaxException, IOException {
        return hypixel(type, "", "");
    }

    public static ApiResult hypixel(String type, String uuid) throws URISyntaxException, IOException {
        return hypixel(type, "uuid", uuid);
    }

    public static ApiResult hypixel(String type, String parameter, String value) throws URISyntaxException, IOException {
        URI uri = new URI("https://api.hypixel.net/" + type + "?key=" + config.INSTANCE.getHypixelAPIkey() + "&" + parameter + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8));
        JsonObject result;
        String s = "";
        try {
            s = request(uri);
            result = new Gson().fromJson(s, JsonObject.class);
        } catch (JsonSyntaxException e) {
            logger.warn("请求结果: {}", s);
            if (!Data.getHypixelData(type, value).equals("null")) {
                logger.error("请求失败，返回本地缓存", e);
                return new ApiResult(new Gson().fromJson(Data.getHypixelData(type, value), JsonObject.class), Long.parseLong(Data.getHypixelDataTime(type, value)));
            }
            if (config.INSTANCE.getHypixelAPIkey().isEmpty()) {
                throw new JsonSyntaxException("HypixelAPIkey为空，请前往配置文件填写HypixelAPIkey");
            }
            throw new JsonSyntaxException(e);
        }
        Data.setHypixelData(type, value, result);
        return new ApiResult(result, -1);
    }

    public static String request(URI uri) throws MalformedURLException {
        System.setProperty("java.net.preferIPv6Addresses", "true");

        Proxy proxy = null;
        if (!config.INSTANCE.getProxy().isEmpty()) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.INSTANCE.getProxy(), Integer.parseInt(config.INSTANCE.getProxyPort())));
        }
        URL url = uri.toURL();
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URLConnection connection;
            if (proxy != null) {
                connection = url.openConnection(proxy);
            } else {
                connection = url.openConnection();
            }
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:131.0) Gecko/20100101 Firefox/131.0");
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

    //格式化经验值
    static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public static String formatExp(float ex) {
        if (ex >= 100000 & ex < 1000000) {
            return decimalFormat.format(ex / 1000) + "K";
        } else if (ex > 1000000 & ex < 1000000000) {
            return decimalFormat.format(ex / 1000000) + "M";
        } else if (ex > 1000000000) {
            return decimalFormat.format(ex / 1000000000) + "B";
        } else {
            return String.valueOf((int) ex);
        }
    }
}
