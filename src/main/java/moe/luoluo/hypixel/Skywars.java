package moe.luoluo.hypixel;

import com.google.gson.JsonObject;
import moe.luoluo.Api;
import moe.luoluo.ApiResult;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import static moe.luoluo.util.Format.decimalFormat;
import static moe.luoluo.util.Format.largeNumFormat;

public class Skywars {
    public static MessageChain skywars(String player) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();

        ApiResult result;
        JsonObject json;
        String uuid = Api.mojang(player, "uuid");
        if (Objects.equals(uuid, "NotFound")) {
            chain.append("玩家不存在");
            return chain.build();
        } else {
            result = Api.hypixel("player", uuid);
            json = result.getJson();
        }

        if (result.getTime() != -1) {
            Instant instant = Instant.ofEpochMilli(result.getTime());
            LocalDateTime localDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            chain.append("\uD83D\uDFE5").append(localDate.toString()).append("\n");
        }

        JsonObject swJson = json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject();

        if (!(json.get("player").isJsonObject() && json.get("player").getAsJsonObject().has("stats") && json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("SkyWars"))) {
            chain.append(new PlainText("该玩家的空岛战争数据为空"));
            return chain.build();
        }
        chain.append(new PlainText(Rank.rank(json.get("player").getAsJsonObject()) + " ")); //玩家名称前缀

        chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
        chain.append(new PlainText(" | 空岛战争数据:"));

        chain.append("\n游戏次数: ");
        int temp = 0;
        if (swJson.has("wins")) {
            temp += swJson.get("wins").getAsInt();
        }
        if (swJson.has("losses")) {
            temp += swJson.get("losses").getAsInt();
        }
        chain.append(new PlainText(String.valueOf(temp)));
        if (swJson.has("win_streak")) {
            chain.append(new PlainText(" | 连胜: "));
            chain.append(new PlainText(String.valueOf(swJson.get("win_streak").getAsInt())));
        }

        chain.append(new PlainText("\n硬币: "));
        if (swJson.has("coins")) {
            chain.append(largeNumFormat(swJson.get("coins").getAsInt()));
        } else chain.append("null");
        if (swJson.has("cosmetic_tokens"))
            chain.append(" | 代币: ").append(largeNumFormat(swJson.get("cosmetic_tokens").getAsInt()));

        chain.append(new PlainText("\n等级: "));
        if (swJson.has("skywars_experience")) {
            long exp = swJson.get("skywars_experience").getAsLong();
            int[] expNeeded = {10, 25, 50, 75, 100, 250, 500, 750, 1000, 1250, 1500, 1750, 2000, 2500, 3000, 3500, 4000, 4500, 5000};
            int level = 1;
            for (int j : expNeeded) {
                if (exp >= j) {
                    exp -= j;
                    level++;
                } else break;
            }
            while (exp >= 5000) {
                level++;
                exp -= 5000;
            }
            //max sw level is 500
            if (level > 500)
                level = 500;
            chain.append(new PlainText(String.valueOf(level)));
            //经验进度
            if (level != 500) { // same as "if (level < 500)"
                int xpLevel = Math.min(level - 1, 18);
                chain.append(new PlainText(" (" + exp +
                        "/" + exp(xpLevel) + " " +
                        decimalFormat.format((float) exp / expNeeded[xpLevel] * 100) + "%)"
                ));
            } else chain.append("(MAX)");
        } else chain.append("null");

        chain.append("\n灵魂: ");
        if (swJson.has("souls"))
            chain.append(String.valueOf(swJson.get("souls").getAsInt()));
        else chain.append("null");
        if (swJson.has("heads"))
            chain.append(" | 头颅: ").append(String.valueOf(swJson.get("heads").getAsInt()));
        if (swJson.has("opals"))
            chain.append(" | 欧泊: ").append(String.valueOf(swJson.get("opals").getAsInt()));

        if (swJson.has("wins") && swJson.has("losses")) {
            chain.append(new PlainText("\n胜场: "));
            chain.append(new PlainText(String.valueOf(swJson.get("wins").getAsInt())));
            chain.append(new PlainText(" | 败场: "));
            chain.append(new PlainText(String.valueOf(swJson.get("losses").getAsInt())));
            chain.append(new PlainText(" | WLR: "));
            chain.append(new PlainText(String.valueOf(decimalFormat.format(
                    (float) swJson.get("wins").getAsInt() /
                            (float) swJson.get("losses").getAsInt()))));
        }

        if (swJson.has("kills") && swJson.has("assists") && swJson.has("deaths")) {
            chain.append(new PlainText("\n击杀: "));
            chain.append(new PlainText(String.valueOf(swJson.get("kills").getAsInt())));
            chain.append(new PlainText(" | 死亡: "));
            chain.append(new PlainText(String.valueOf(swJson.get("deaths").getAsInt())));
            chain.append(new PlainText(" | KDR: "));
            chain.append(new PlainText(String.valueOf(decimalFormat.format(
                    (float) swJson.get("kills").getAsInt() /
                            (float) swJson.get("deaths").getAsInt()))));
            chain.append(new PlainText("\n助攻: "));
            chain.append(new PlainText(String.valueOf(swJson.get("assists").getAsInt())));
            chain.append(new PlainText(" | (K+A)/D: "));
            chain.append(new PlainText(String.valueOf(decimalFormat.format(
                    ((float) swJson.get("kills").getAsInt() +
                            (float) swJson.get("assists").getAsInt()) /
                            (float) swJson.get("deaths").getAsInt()))));
        }

        //lab
        if (swJson.has("games_lab")) {
            chain.append(new PlainText("\n\n实验模式: "));
            chain.append(new PlainText("\n游戏次数: "));
            temp = 0;
            if (swJson.has("wins_lab")) {
                temp += swJson.get("wins_lab").getAsInt();
            }
            if (swJson.has("losses_lab")) {
                temp += swJson.get("losses_lab").getAsInt();
            }
            chain.append(new PlainText(String.valueOf(temp)));
            if (swJson.has("quits_lab")) {
                chain.append(new PlainText(" | 退出次数: "));
                chain.append(new PlainText(String.valueOf(swJson.get("quits_lab").getAsInt())));
            }
            if (swJson.has("win_streak_lab")) {
                chain.append(new PlainText(" | 连胜: "));
                chain.append(new PlainText(String.valueOf(swJson.get("win_streak_lab").getAsInt())));
            }
            chain.append(new PlainText("\n胜场: "));
            if (swJson.has("wins_lab")) {
                chain.append(new PlainText(String.valueOf(swJson.get("wins_lab").getAsInt())));
            } else chain.append(new PlainText("0"));
            if (swJson.has("losses_lab")) {
                chain.append(new PlainText(" | 败场: "));
                chain.append(new PlainText(String.valueOf(swJson.get("losses_lab").getAsInt())));
            }
            if (swJson.has("wins_lab") && swJson.has("losses_lab")) {
                chain.append(new PlainText(" | WLR:"));
                chain.append(new PlainText(decimalFormat.format(
                        (float) swJson.get("wins_lab").getAsInt() /
                                (float) swJson.get("losses_lab").getAsInt())));
            }
            if (swJson.has("kills_lab")) {
                chain.append(new PlainText("\n击杀: "));
                chain.append(new PlainText(String.valueOf(swJson.get("kills_lab").getAsInt())));
                chain.append(new PlainText(" | 死亡: "));
                chain.append(new PlainText(String.valueOf(swJson.get("deaths_lab").getAsInt())));
                chain.append(new PlainText(" | KDR: "));
                chain.append(new PlainText(decimalFormat.format(
                        (float) swJson.get("kills_lab").getAsInt() /
                                (float) swJson.get("deaths_lab").getAsInt())));
            }
            if (swJson.has("assists_lab")) {
                chain.append(new PlainText("\n助攻: "));
                chain.append(new PlainText(String.valueOf(swJson.get("assists_lab").getAsInt())));
                if (swJson.has("kills_lab")) {
                    chain.append(new PlainText(" | (K+A)/D: "));
                    chain.append(new PlainText(decimalFormat.format(
                            ((float) swJson.get("kills_lab").getAsInt() +
                                    (float) swJson.get("assists_lab").getAsInt()) /
                                    (float) swJson.get("deaths_lab").getAsInt())));
                }
            }
        }

        return chain.build();
    }

    public static String exp(int exp) {
        String[] map = {"10", "25", "50", "75", "100", "250", "500", "750", "1k", "1.25k", "1.5k", "1.75k", "2k", "2.5k", "3k", "3.5k", "4k", "4.5k", "5k"};
        return map[exp];
    }
}
