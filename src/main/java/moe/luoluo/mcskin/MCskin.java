package moe.luoluo.mcskin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import moe.luoluo.Api;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

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
    public static void mcskin(CommandSender context, String player) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();

        String uuid = Api.mojang(player, "uuid");
        if (Objects.equals(uuid, "NotFound")) {
            context.sendMessage("玩家不存在");
            return;
        }

        URI uri;
        byte[] data;
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
        } catch (IOException e) {
            chain.append(new PlainText("crafatar图片加载失败"));
            context.sendMessage(chain.build());
            throw new RuntimeException(e);
        }
        if (context.getSubject() != null) {
            Image img = context.getSubject().uploadImage(ExternalResource.create(data));
            chain.append(img);
        }

        URI mcs = new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        JsonObject json = new Gson().fromJson(request(mcs), JsonObject.class);
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
        } catch (IOException e) {
            chain.append(new PlainText("\n皮肤图片加载失败"));
            context.sendMessage(chain.build());
            throw new RuntimeException(e);
        }
        if (context.getSubject() != null) {
            Image img = context.getSubject().uploadImage(ExternalResource.create(data));
            chain.append(img);
        }

        chain.append("皮肤文件链接: ").append(String.valueOf(skinUri));

        context.sendMessage(chain.build());
    }
}
