package moe.luoluo.hypixel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import moe.luoluo.Api;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

public class Skywars {
    public static MessageChain skywars(String player) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        JsonObject json = new Gson().fromJson(Api.hypixel("player", Api.mojang(player, "uuid")), JsonObject.class);
        JsonObject playerJson = json.get("player").getAsJsonObject();
        JsonObject swJson = playerJson.get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject();

        if (json.get("player").isJsonObject()) {

            chain.append(new PlainText( Rank.rank(playerJson) + " ")); //玩家名称前缀

            chain.append(new PlainText(playerJson.get("displayname").getAsString()));
            chain.append(new PlainText(" | 空岛战争数据:"));

            if (playerJson.has("stats") && playerJson.get("stats").getAsJsonObject().has("SkyWars")) {


                if (swJson.has("games_played_skywars")) {
                    chain.append(new PlainText("\n总游戏场次: "));
                    chain.append(new PlainText(String.valueOf(swJson.get("games_played_skywars").getAsInt())));
                }
                if (swJson.has("coins")) {
                    chain.append(new PlainText(" | 硬币: "));
                    chain.append(new PlainText(String.valueOf(swJson.get("coins").getAsInt())));
                }

                if (swJson.has("games")) {
                    chain.append(new PlainText("\n游戏场次: "));
                    int temp = 0;
                    if (swJson.has("wins")) {
                        temp += swJson.get("wins").getAsInt();
                    }
                    if (swJson.has("losses")) {
                        temp += swJson.get("losses").getAsInt();
                    }
                    chain.append(new PlainText(String.valueOf(temp)));
                    chain.append(new PlainText(" | 等级: "));
                    chain.append(new PlainText(swJson.get("levelFormatted").getAsString().replace(swJson.get("levelFormatted").getAsString().substring(0, 2), "").replace("⋆", "✨")));
                }
                if (swJson.has("win_streak")) {
                    chain.append(new PlainText(" | 连胜: "));
                    chain.append(new PlainText(String.valueOf(swJson.get("win_streak").getAsInt())));
                }

                /*if (skywars_experience(swJson)){

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
                    chain.append(new PlainText("\n\n实验模式数据: "));
                    chain.append(new PlainText("\n游戏场次: "));
                    int temp = 0;
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
                }
                */
            } else {
                chain.append(new PlainText("该玩家的空岛战争数据为空"));
            }

        }
        return chain.build();
    }
}
