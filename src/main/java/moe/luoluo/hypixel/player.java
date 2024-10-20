package moe.luoluo.hypixel;

import com.google.gson.JsonObject;
import moe.luoluo.Api;
import moe.luoluo.ApiResult;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Player {
    public static void player(CommandSender context, String player, String type) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();
        JsonObject playerJson;
        JsonObject giftingMeta;
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日-HH时mm分ss秒", Locale.CHINA);

        ApiResult result;
        JsonObject json;
        String uuid = Api.mojang(player, "uuid");
        if (Objects.equals(uuid, "NotFound")) {
            context.sendMessage("玩家不存在");
            return;
        } else {
            result = Api.hypixel("player", uuid);
            json = result.getJson();
        }

        if (result.getTime() != -1) {
            Instant instant = Instant.ofEpochMilli(result.getTime());
            LocalDateTime localDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            chain.append("\uD83D\uDFE5").append(localDate.toString()).append("\n");
        }

        JsonObject guild = Api.hypixel("guild", "player", Api.mojang(player, "uuid")).getJson();
        if (!json.get("player").isJsonObject()) {
            chain.append("该玩家的Hypixel数据为空");
            context.sendMessage(chain.build());
            return;
        }
        playerJson = json.get("player").getAsJsonObject();

        JsonObject online;
        if (playerJson.has("lastLogout")) {
            online = Api.hypixel("status", Api.mojang(player, "uuid")).getJson();
        } else {
            online = null;
        }
        chain.append(Rank.rank(playerJson)).append(" "); //玩家名称前缀
        chain.append(playerJson.get("displayname").getAsString());

        if (online != null && online.has("session")) {
            JsonObject session = online.get("session").getAsJsonObject();
            if (session.get("online").getAsBoolean()) {
                chain.append("  在线\uD83D\uDFE2").append("\n").append(session.get("gameType").getAsString());
                if (session.has("mode")) {
                    chain.append(" | ").append(session.get("mode").getAsString());
                }
                if (session.has("map")) {
                    chain.append(" | ").append(session.get("map").getAsString());
                }
            } else if (playerJson.get("lastLogin").getAsInt() > playerJson.get("lastLogout").getAsInt()) {
                chain.append("  离线\ud83d\udfe8");
            } else {
                if (playerJson.has("lastLogin"))
                    chain.append("  离线\uD83D\uDD34");
                else chain.append("  离线\ud83d\udfe5");
            }
        } else chain.append("  \uD83D\uDD34");

        chain.append("\n成就点数: ");
        if (playerJson.has("achievementPoints"))
            chain.append(String.valueOf(playerJson.get("achievementPoints").getAsInt()));
        else chain.append("null");


        chain.append(" | 人品值: ");
        if (playerJson.has("karma"))
            chain.append(String.valueOf(playerJson.get("karma").getAsInt()));
        else chain.append("null");


        chain.append("\nHypixel等级: ");
        if (playerJson.has("networkExp")) {
            String ex = playerJson.get("networkExp").toString();
            long l = (long) Double.parseDouble(ex);
            double xp = Math.sqrt((0.0008 * l) + 12.25) - 2.5;
            chain.append(decimalFormat.format(xp));
        } else chain.append("null");

        chain.append("\n所属公会: ");
        if (!guild.get("guild").isJsonNull())
            chain.append(guild.get("guild").getAsJsonObject().get("name").getAsString());
        else chain.append("无");


        chain.append("\n使用的语言: ");
        if (playerJson.has("userLanguage"))
            chain.append(playerJson.get("userLanguage").getAsString());
        else chain.append("English");

        chain.append("\nRANK赠送数: ");
        if (playerJson.has("giftingMeta")) {
            giftingMeta = playerJson.get("giftingMeta").getAsJsonObject();
            if (giftingMeta.has("ranksGiven"))
                chain.append(String.valueOf(giftingMeta.get("ranksGiven").getAsInt()));
            else chain.append("0");
        } else chain.append("0");

        chain.append("\n首次登录: ");
        if (playerJson.has("firstLogin"))
            chain.append(simpleDateFormat.format(new Date(playerJson.get("firstLogin").getAsLong())));
        else
            chain.append("null");

        if (playerJson.has("lastLogin"))
            chain.append("\n最后登录: ").append(simpleDateFormat.format(new Date(playerJson.get("lastLogin").getAsLong())));

        if (playerJson.has("lastLogout"))
            chain.append("\n最后退出: ").append(simpleDateFormat.format(new Date(playerJson.get("lastLogout").getAsLong())));
        else if (playerJson.has("lastClaimedReward"))
            chain.append("\n最后领取每日奖励: ").append(simpleDateFormat.format(new Date(playerJson.get("lastClaimedReward").getAsLong())));

        if (playerJson.has("mostRecentGameType"))
            chain.append("\n最近游戏: ").append(playerJson.get("mostRecentGameType").getAsString());


        if (Objects.equals(type, "all") && playerJson.has("socialMedia") && playerJson.get("socialMedia").getAsJsonObject().has("links")) {
            JsonObject links = playerJson.get("socialMedia").getAsJsonObject().get("links").getAsJsonObject().getAsJsonObject();
            chain.append("\n链接的社交媒体账号: ");
            if (links.has("DISCORD"))
                chain.append("\n| Discord: ").append(links.get("DISCORD").getAsString());
            if (links.has("HYPIXEL"))
                chain.append("\n| Hypixel论坛: ").append(links.get("HYPIXEL").getAsString());
            if (links.has("YOUTUBE"))
                chain.append("\n| YouTube: ").append(links.get("YOUTUBE").getAsString());
            if (links.has("TWITTER"))
                chain.append("\n| Twitter: ").append(links.get("TWITTER").getAsString());
            if (links.has("TWITCH"))
                chain.append("\n| Twitch: ").append(links.get("TWITCH").getAsString());
            if (links.has("INSTAGRAM"))
                chain.append("\n| Instagram: ").append(links.get("INSTAGRAM").getAsString());
            if (links.has("TIKTOK"))
                chain.append("\n| TikTok: ").append(links.get("TIKTOK").getAsString());
        }

        URI uri;
        byte[] data;
        try {
            uri = new URI("https://crafatar.com/renders/body/" + playerJson.get("uuid").getAsString() + "?scale=10" + "&overlay");
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
            chain.append("\n\n皮肤图片加载失败");
            context.sendMessage(chain.build());
            System.out.println("以对下方报错进行处理, 若 crafatar.com 正常访问请联系作者");
            throw new RuntimeException(e);
        }

        if (context.getSubject() != null) {
            Image img = context.getSubject().uploadImage(ExternalResource.create(data));
            chain.append(img);
        }
        if (type.equals("all") && context.getSubject() != null) {
            ForwardMessageBuilder builder = new ForwardMessageBuilder(context.getSubject());
            builder.add(Objects.requireNonNull(context.getBot()).getId(), context.getBot().getNick(), chain.build());
            context.sendMessage(builder.build());
        } else context.sendMessage(chain.build());
    }
}
