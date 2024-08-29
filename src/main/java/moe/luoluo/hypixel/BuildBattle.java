package moe.luoluo.hypixel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import moe.luoluo.Api;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuildBattle {
    public static void buildBattle(CommandSender context, String player) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        JsonObject json = new Gson().fromJson(Api.hypixel("player", Api.mojang(player, "uuid")), JsonObject.class);
        JsonObject leader =new Gson().fromJson(Api.hypixel("leaderboards"), JsonObject.class);

        if (json.get("player").isJsonObject()) {
            JsonObject playerJson = json.get("player").getAsJsonObject();

            if (playerJson.has("stats") && playerJson.get("stats").getAsJsonObject().has("BuildBattle")) {
                JsonObject bbJson = playerJson.get("stats").getAsJsonObject().get("BuildBattle").getAsJsonObject();
                chain.append(new PlainText("\n" + Rank.rank(playerJson) + " ")); //玩家名称前缀
                chain.append(new PlainText(playerJson.get("displayname").getAsString()));
                chain.append(new PlainText(" | 建筑大师数据: "));

                if (bbJson.has("score")) {
                    int score = bbJson.get("score").getAsInt();
                    chain.append(new PlainText("\n称号: "));
                    JsonArray l1 = leader.get("leaderboards").getAsJsonObject().get("BUILD_BATTLE").getAsJsonArray().get(0).getAsJsonObject().get("leaders").getAsJsonArray();
                    List<String> l2 = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        l2.add(l1.get(i).toString());
                    }
                    String[] leaders = l2.toArray(new String[10]);
                    int lead =1;
                    for (String l : leaders){
                        if (Objects.equals(playerJson.get("uuid").getAsString(), l.replaceAll("\"", "").replaceAll("-", ""))){
                            chain.append(new PlainText("#" + lead + "建筑师"));
                            break;
                        }else {lead++;}
                        if (lead>10){
                            int[] scoreNeeded = {100, 250, 500, 1000, 2000, 3500, 5000, 7500, 10000, 15000, 20000};
                            int level = 1;
                            for (int j : scoreNeeded) {
                                if (score >= j) {
                                    level++;
                                } else break;
                            }
                            switch (level) {
                                case 1:
                                    chain.append(new PlainText("初来乍到"));
                                    break;
                                case 2:
                                    chain.append(new PlainText("未经雕琢"));
                                    break;
                                case 3:
                                    chain.append(new PlainText("初窥门径"));
                                    break;
                                case 4:
                                    chain.append(new PlainText("学有所成"));
                                    break;
                                case 5:
                                    chain.append(new PlainText("驾轻就熟"));
                                    break;
                                case 6:
                                    chain.append(new PlainText("历练老成"));
                                    break;
                                case 7:
                                    chain.append(new PlainText("技艺精湛"));
                                    break;
                                case 8:
                                    chain.append(new PlainText("炉火纯青"));
                                    break;
                                case 9:
                                    chain.append(new PlainText("技惊四座"));
                                    break;
                                case 10:
                                    chain.append(new PlainText("巧夺天工"));
                                    break;
                                case 11:
                                    chain.append(new PlainText("闻名于世"));
                                    break;
                                case 12:
                                    chain.append(new PlainText("建筑大师"));
                                    break;
                            }
                            break;
                        }
                    }


                    chain.append(new PlainText(" | 积分: "));
                    chain.append(new PlainText(String.valueOf(score)));
                }

                chain.append(new PlainText("\n硬币: "));
                if (bbJson.has("coins")) {
                    chain.append(new PlainText(String.valueOf(bbJson.get("coins").getAsInt())));
                } else {
                    chain.append(new PlainText("0"));
                }
                if (bbJson.has("score")) {
                    chain.append(new PlainText(" | 平均每场得分: "));
                    chain.append(new PlainText(decimalFormat.format(
                            (double) bbJson.get("score").getAsInt() /
                                    bbJson.get("games_played").getAsInt()
                    )));
                }

                chain.append(new PlainText("\n游戏场次: "));
                if (bbJson.has("games_played")) {
                    chain.append(new PlainText(String.valueOf(bbJson.get("games_played").getAsInt())));
                } else {
                    chain.append(new PlainText("0"));
                }
                chain.append(new PlainText(" | 胜场数: "));
                if (bbJson.has("wins")) {
                    chain.append(new PlainText(String.valueOf(bbJson.get("wins").getAsInt())));
                    chain.append(new PlainText(" | 胜率: "));
                    chain.append(new PlainText(decimalFormat.format(
                            (double) bbJson.get("wins").getAsInt() /
                                    bbJson.get("games_played").getAsInt() * 100
                    )));
                    chain.append(new PlainText("%"));
                } else {
                    chain.append(new PlainText("0"));
                }

                chain.append(new PlainText("\n投票数: "));
                if (bbJson.has("total_votes")) {
                    chain.append(new PlainText(String.valueOf(bbJson.get("total_votes").getAsInt())));
                } else {
                    chain.append(new PlainText("0"));
                }
                chain.append(new PlainText(" | 超级选票: "));
                if (bbJson.has("super_votes")) {
                    chain.append(new PlainText(String.valueOf(bbJson.get("super_votes").getAsInt())));
                } else {
                    chain.append(new PlainText("0"));
                }

                chain.append(new PlainText("\n猜猜乐胜场: "));
                if (bbJson.has("wins_guess_the_build")) {
                    chain.append(new PlainText(String.valueOf(bbJson.get("wins_guess_the_build").getAsInt())));
                } else {
                    chain.append(new PlainText("0"));
                }
                chain.append(new PlainText(" | 猜对次数: "));
                if (bbJson.has("correct_guesses")) {
                    chain.append(new PlainText(String.valueOf(bbJson.get("correct_guesses").getAsInt())));
                } else {
                    chain.append(new PlainText("0"));
                }

                chain.append(new PlainText("\n单人1.8胜场: "));
                if (bbJson.has("wins_solo_normal")) {
                    chain.append(new PlainText(String.valueOf(bbJson.get("wins_solo_normal").getAsInt())));
                } else {
                    chain.append(new PlainText("0"));
                }
                chain.append(new PlainText(" | 单人1.14胜场: "));
                if (bbJson.has("wins_solo_normal_latest")) {
                    chain.append(new PlainText(String.valueOf(bbJson.get("wins_solo_normal_latest").getAsInt())));
                } else {
                    chain.append(new PlainText("0"));
                }

                chain.append(new PlainText("\n团队模式胜场: "));
                if (bbJson.has("wins_teams_normal")) {
                    chain.append(new PlainText(String.valueOf(bbJson.get("wins_teams_normal").getAsInt())));
                } else {
                    chain.append(new PlainText("0"));
                }
                chain.append(new PlainText(" | 高手模式胜场: "));
                if (bbJson.has("wins_solo_pro")) {
                    chain.append(new PlainText(String.valueOf(bbJson.get("wins_solo_pro").getAsInt())));
                } else {
                    chain.append(new PlainText("0"));
                }

            } else chain.append(new PlainText("该玩家的建筑大师数据为空"));
            context.sendMessage(chain.build());
        }

    }
}
