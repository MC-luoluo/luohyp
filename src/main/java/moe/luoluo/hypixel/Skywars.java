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

        if (swJson.has("games_played_skywars")) {
            chain.append(new PlainText("\n总游戏场次: "));
            chain.append(new PlainText(String.valueOf(swJson.get("games_played_skywars").getAsInt())));
        }
        if (swJson.has("coins")) {
            chain.append(new PlainText(" | 硬币: "));
            chain.append(new PlainText(String.valueOf(swJson.get("coins").getAsInt())));
        }

        chain.append(new PlainText("\n等级: "));
        if (swJson.has("skywars_experience")) {
            long exp = swJson.get("skywars_experience").getAsLong();
            int[] expNeeded = {20, 50, 80, 100, 250, 500, 1000, 1500, 2500, 4000, 5000, 10000};
            int level = 1;
            for (int j : expNeeded) {
                if (exp >= j) {
                    exp -= j;
                    level++;
                } else break;
            }
            while (exp >= 10000) {
                level++;
                exp -= 10000;
            }
            chain.append(new PlainText(String.valueOf(level)));
            //经验进度
            int xpLevel = Math.min(level - 1, 11);
            chain.append(new PlainText(" (" + exp +
                    "/" + exp(xpLevel) + " " +
                    decimalFormat.format((float) exp / expNeeded[xpLevel] * 100) + "%)"
            ));
        } else chain.append("null");

        chain.append(new PlainText("\n游戏场次: "));
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

        /*if (skywars_experience(swJson)) {
            chain.append(new PlainText("\n当前经验值: "));
            chain.append(new PlainText(String.valueOf(swJson.get("skywars_experience").getAsDouble())));
            chain.append(new PlainText(" | 硬币: "));
            chain.append(new PlainText(String.valueOf(swJson.get("coins").getAsInt())));
        }*/

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

        chain.append(new PlainText("\n头颅: "));
        if (swJson.has("heads")) {
            chain.append(new PlainText(String.valueOf(swJson.get("heads").getAsInt())));
        } else {
            chain.append(new PlainText("null"));
        }
        chain.append(new PlainText(" | 灵魂: "));
        if (swJson.has("souls")) {
            chain.append(new PlainText(String.valueOf(swJson.get("souls").getAsInt())));
        } else {
            chain.append(new PlainText("null"));
        }

        if (swJson.has("games_lab")) {
            chain.append(new PlainText("\n\n实验模式: "));
            chain.append(new PlainText("\n游戏场次: "));
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

        /*
        if (melee_kills(swJson)) {
            chain.append(new PlainText("\n近战杀敌数: "));
            chain.append(new PlainText(String.valueOf(swJson.get("melee_kills").getAsInt())));
            chain.append(new PlainText(" | 弓箭击杀: "));
            chain.append(new PlainText(String.valueOf(swJson.get("bow_kills").getAsInt())));
            chain.append(new PlainText(" | 推入虚空: "));
            chain.append(new PlainText(String.valueOf(swJson.get("void_kills").getAsInt())));
        }

        if (arrows_shot(swJson)) {
            chain.append(new PlainText("\n箭矢射击数: "));
            chain.append(new PlainText(String.valueOf(swJson.get("arrows_shot").getAsInt())));
            chain.append(new PlainText(" | 箭矢命中数: "));
            chain.append(new PlainText(String.valueOf(swJson.get("arrows_hit").getAsInt())));
            chain.append(new PlainText(" | 箭矢命中率: "));
            chain.append(new PlainText(decimalFormat.format(
                    (float) swJson.get("arrows_hit").getAsInt() /
                            (float) swJson.get("arrows_shot").getAsInt())));
        }

        if (chests_opened(swJson)) {
            chain.append(new PlainText("\n打开箱子个数: "));
            chain.append(new PlainText(String.valueOf(swJson.get("chests_opened").getAsInt())));
        }

        if (enderpearls_thrown(swJson)) {
            chain.append(new PlainText(" | 扔出末影珍珠数: "));
            chain.append(new PlainText(String.valueOf(swJson.get("enderpearls_thrown").getAsInt())));
        }

        if (souls_gathered(swJson)) {
            chain.append(new PlainText(" | 获得的灵魂: "));
            chain.append(new PlainText(String.valueOf(swJson.get("souls_gathered").getAsInt())));
        }*/

        return chain.build();
    }

    public static String exp(int exp) {
        String[] map = {"20", "50", "80", "100", "250", "500", "1k", "1.5k", "2.5k", "4k", "5k", "10k"};
        return map[exp];
    }
}
