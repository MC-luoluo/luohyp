package moe.luoluo.hypixel;

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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Player {
    public static void player(CommandSender context, String player) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();
        JsonObject playerJson;
        JsonObject giftingMeta;
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日-HH时mm分ss秒", Locale.CHINA);

        JsonObject json = Api.hypixel("player", Api.mojang(player, "uuid"));
        JsonObject guild = Api.hypixel("guild", "player",Api.mojang(player, "uuid"));
        if (json.get("player").isJsonObject()) {
            playerJson = json.get("player").getAsJsonObject();
//            achievements = playerJson.get("achievements").getAsJsonObject();  V0.4.2版本更新注释

            JsonObject online;
            if (playerJson.has("lastLogin")) {
                online = Api.hypixel("status", Api.mojang(player, "uuid"));
            } else {
                online = null;
            }
            chain.append(new PlainText(Rank.rank(playerJson) + " ")); //玩家名称前缀
            chain.append(new PlainText(playerJson.get("displayname").getAsString()));

            chain.append(new PlainText("\n在线状态: "));
            if (online != null && online.has("session")) {
                JsonObject session = online.get("session").getAsJsonObject();
                if (session.get("online").getAsBoolean()) {
                    chain.append(new PlainText("ONLINE\uD83D\uDFE2"));
                    chain.append(new PlainText("\n" + session.get("gameType").getAsString()));
                    {
                        if (session.has("mode")) {
                            chain.append(new PlainText(" | "));
                            chain.append(new PlainText(session.get("mode").getAsString()));
                        }
                        if (session.has("map")) {
                            chain.append(new PlainText(" | "));
                            chain.append(new PlainText(session.get("map").getAsString()));
                        }
                    }
                } else chain.append(new PlainText("OFFLINE\uD83D\uDD34"));
            } else {
                chain.append(new PlainText("未开启在线状态API"));
            }

            chain.append(new PlainText("\nRANK赠送数: "));
            if (playerJson.has("giftingMeta")) {
                giftingMeta = playerJson.get("giftingMeta").getAsJsonObject();
                if (giftingMeta.has("ranksGiven")) {
                    chain.append(new PlainText(String.valueOf(giftingMeta.get("ranksGiven").getAsInt())));
                } else chain.append(new PlainText("0"));
            } else chain.append(new PlainText("0"));

            chain.append(new PlainText("\n首次登录: "));
            if (playerJson.has("firstLogin")) {
                chain.append(new PlainText(simpleDateFormat.format(new Date(playerJson.get("firstLogin").getAsLong()))));
            } else {
                chain.append(new PlainText("null"));
            }

            if (playerJson.has("lastLogin")) {
                chain.append(new PlainText("\n最后登录: "));
                chain.append(new PlainText(simpleDateFormat.format(new Date(playerJson.get("lastLogin").getAsLong()))));
            }

            if (playerJson.has("lastLogout")) {
                chain.append(new PlainText("\n最后退出: "));
                chain.append(new PlainText(simpleDateFormat.format(new Date(playerJson.get("lastLogout").getAsLong()))));
            } else if (playerJson.has("lastClaimedReward")) {
                chain.append(new PlainText("\n最后领取每日奖励: "));
                chain.append(new PlainText(simpleDateFormat.format(new Date(playerJson.get("lastClaimedReward").getAsLong()))));
            }

            chain.append(new PlainText("\n使用的语言: "));
            if (playerJson.has("userLanguage")) {
                chain.append(new PlainText(playerJson.get("userLanguage").getAsString()));
            } else {
                chain.append(new PlainText("English"));
            }

            chain.append(new PlainText("\n所属公会: "));
            if (!guild.get("guild").isJsonNull()) {
                chain.append(new PlainText(guild.get("guild").getAsJsonObject().get("name").getAsString()));
            } else {
                chain.append(new PlainText("无"));
            }

            chain.append(new PlainText("\nHypixel等级: "));
            if (playerJson.has("networkExp")) {
                String ex = playerJson.get("networkExp").toString();
                long l = (long) Double.parseDouble(ex);
                double xp = Math.sqrt((0.0008 * l) + 12.25) - 2.5;

                chain.append(new PlainText(decimalFormat.format(xp)));
            } else {
                chain.append(new PlainText("null"));
            }

            if (playerJson.has("mostRecentGameType")) {
                chain.append(new PlainText("\n最近游玩的模式: "));
                chain.append(new PlainText(playerJson.get("mostRecentGameType").getAsString()));
            }

            chain.append(new PlainText("\n成就点数: "));
            if (playerJson.has("achievementPoints")) {
                chain.append(new PlainText(String.valueOf(playerJson.get("achievementPoints").getAsInt())));
            } else {
                chain.append(new PlainText("null"));
            }

            chain.append(new PlainText(" | 人品值: "));
            if (playerJson.has("karma")) {
                chain.append(new PlainText(String.valueOf(playerJson.get("karma").getAsInt())));
            } else {
                chain.append(new PlainText("null"));
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
                chain.append(new PlainText("\n\n皮肤图片加载失败, 可能是API出现问题"));
                chain.append(new PlainText("\n可查看 crafatar.com 确认是否无法加载"));
                context.sendMessage(chain.build());
                System.out.println("以对下方报错进行处理, 若 crafatar.com 正常访问请联系作者");
                throw new RuntimeException(e);
            }

            if (context.getSubject() != null) {
                Image img = context.getSubject().uploadImage(ExternalResource.create(data));
                chain.append(img);
            }

        } else {
            chain.append(new PlainText("该玩家存在, 但是玩家数据不存在, 可能是因为玩家没有进入过hyp服务器, 也可能是数据丢失"));
        }
        context.sendMessage(chain.build());
    }
}
