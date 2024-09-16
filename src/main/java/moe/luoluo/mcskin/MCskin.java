package moe.luoluo.mcskin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import moe.luoluo.Api;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Objects;

import static moe.luoluo.Api.request;

public class MCskin {
    private static final Logger log = LoggerFactory.getLogger(MCskin.class);

    public static void mcskin(CommandSender context, String player) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();

        String uuid = Api.mojang(player, "uuid");
        if (Objects.equals(uuid, "NotFound")) {
            context.sendMessage("玩家不存在");
            return;
        }

        URI mcs = new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        JsonObject json = new Gson().fromJson(request(mcs), JsonObject.class);

        chain.append("玩家: ");
        chain.append(json.get("name").getAsString());

        chain.append("\nuuid: ");
        chain.append(json.get("id").getAsString());

        URI uri;
        byte[] data = new byte[0];
        try {
            uri = new URI("https://crafatar.com/renders/body/" + uuid + "?scale=10" + "&overlay");
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
            data = outStream.toByteArray();

            if (context.getSubject() != null) {
                Image img = context.getSubject().uploadImage(ExternalResource.create(data));
                chain.append(img);
            }
        } catch (IOException e) {
            chain.append(new PlainText("\ncrafatar图片加载失败"));
            log.error(String.valueOf(new RuntimeException(e)));
        }

        String value = json.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();

        // 获取一个Base64解码器
        Base64.Decoder decoder = Base64.getDecoder();

        // 将Base64编码的字符串解码回字节数组
        byte[] decodedBytes = decoder.decode(value);

        // 将字节数组转换回字符串
        JsonObject profile = new Gson().fromJson(new String(decodedBytes), JsonObject.class);
        URI skinUri = new URI(profile.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString());
        try {
            HttpURLConnection conn = (HttpURLConnection) skinUri.toURL().openConnection();
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
            data = outStream.toByteArray();

            if (context.getSubject() != null) {
                Image img = context.getSubject().uploadImage(ExternalResource.create(data));
                chain.append(img);
            }
        } catch (IOException e) {
            chain.append(new PlainText("\n皮肤图片加载失败"));
            log.error(String.valueOf(new RuntimeException(e)));
        }

        chain.append("\n皮肤链接: ").append(String.valueOf(skinUri));

        if (profile.get("textures").getAsJsonObject().has("CAPE")) {
            URI capeUri = new URI(profile.get("textures").getAsJsonObject().get("CAPE").getAsJsonObject().get("url").getAsString());
            try {
                HttpURLConnection conn = (HttpURLConnection) capeUri.toURL().openConnection();
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
                data = outStream.toByteArray();

                if (context.getSubject() != null) {
                    Image img = context.getSubject().uploadImage(ExternalResource.create(data));
                    chain.append(img);
                }
            } catch (IOException e) {
                chain.append(new PlainText("\n披风图片加载失败"));
                log.error(String.valueOf(new RuntimeException(e)));
            }

            chain.append("\n披风链接: ").append(String.valueOf(capeUri));
        }

        URI ofCape = new URI("http://s.optifine.net/capes/" + json.get("name").getAsString() + ".png");
        byte[] ofData;
        try {
            HttpURLConnection conn = (HttpURLConnection) ofCape.toURL().openConnection();
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
            ofData = outStream.toByteArray();

            if (context.getSubject() != null) {
                Image img = context.getSubject().uploadImage(ExternalResource.create(ofData));
                chain.append(img);
            }

            chain.append("\noptifine披风链接: ").append(String.valueOf(ofCape));
        } catch (IOException ignored) {
        }

        context.sendMessage(chain.build());
    }
}
