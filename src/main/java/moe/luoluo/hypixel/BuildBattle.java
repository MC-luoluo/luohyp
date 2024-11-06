package moe.luoluo.hypixel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import moe.luoluo.Api;
import moe.luoluo.ApiResult;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuildBattle {
    public static MessageChain buildBattle(String player) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

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

        JsonObject leader = Api.hypixel("leaderboards").getJson();

        if (result.getTime() != -1) {
            Instant instant = Instant.ofEpochMilli(result.getTime());
            LocalDateTime localDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            chain.append("\uD83D\uDFE5").append(localDate.toString()).append("\n");
        }

        if (!(json.get("player").isJsonObject() && json.get("player").getAsJsonObject().has("stats") && json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("BuildBattle"))) {
            chain.append(new PlainText("该玩家的建筑大师数据为空"));
            return chain.build();
        }
        JsonObject bbJson = json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("BuildBattle").getAsJsonObject();
        chain.append(new PlainText(Rank.rank(json.get("player").getAsJsonObject()) + " ")); //玩家名称前缀
        chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
        chain.append(new PlainText(" | 建筑大师数据: "));

        if (bbJson.has("score")) {
            int score = bbJson.get("score").getAsInt();
            chain.append(new PlainText("\n积分: "));
            chain.append(new PlainText(String.valueOf(score)));
            chain.append(new PlainText(" | 称号: "));
            JsonArray l1 = leader.get("leaderboards").getAsJsonObject().get("BUILD_BATTLE").getAsJsonArray().get(0).getAsJsonObject().get("leaders").getAsJsonArray();
            List<String> l2 = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                l2.add(l1.get(i).toString());
            }
            String[] leaders = l2.toArray(new String[10]);
            int lead = 1;
            for (String l : leaders) {
                if (Objects.equals(json.get("player").getAsJsonObject().get("uuid").getAsString(), l.replaceAll("\"", "").replaceAll("-", ""))) {
                    chain.append(new PlainText("#" + lead + "建筑师"));
                    break;
                } else {
                    lead++;
                }
                if (lead > 10) {
                    int[] scoreNeeded = {100, 250, 500, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 200000, 350000, 500000};
                    int level = 1;
                    for (int j : scoreNeeded) {
                        if (score >= j) {
                            level++;
                        } else break;
                    }
                    chain.append(switch (level) {
                        case 1 -> "Prospect";
                        case 2 -> "Rookie (初来乍到)";
                        case 3 -> "Amateur (初窥门径)";
                        case 4 -> "Apprentice (学有所成)";
                        case 5 -> "Trained (技艺精湛)";
                        case 6 -> "Experienced (驾轻就熟)";
                        case 7 -> "Seasoned (历练老成)";
                        case 8 -> "Skilled (炉火纯青)";
                        case 9 -> "Talented (技惊四座)";
                        case 10 -> "Professional (巧夺天工)";
                        case 11 -> "Artisan";
                        case 12 -> "Expert (闻名于世)";
                        case 13 -> "Master (建筑大师)";
                        case 14 -> "Grandmaster (最强王者)";
                        default -> "null";
                    });
                    break;
                }
            }
        }

        chain.append(new PlainText("\n硬币: "));
        if (bbJson.has("coins")) {
            chain.append(new PlainText(String.valueOf(bbJson.get("coins").getAsInt())));
        } else {
            chain.append(new PlainText("0"));
        }
        if (bbJson.has("score")) {
            chain.append(new PlainText(" | 平均每场得分: "));
            chain.append(new PlainText(decimalFormat.format((double) bbJson.get("score").getAsInt() / bbJson.get("games_played").getAsInt())));
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
            chain.append(new PlainText(decimalFormat.format((double) bbJson.get("wins").getAsInt() / bbJson.get("games_played").getAsInt() * 100)));
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
        return chain.build();
    }
}
