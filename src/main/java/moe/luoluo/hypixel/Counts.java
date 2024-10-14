package moe.luoluo.hypixel;

import com.google.gson.JsonObject;
import moe.luoluo.Api;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

public class Counts {
    private static final Logger logger = LoggerFactory.getLogger(Counts.class);

    public static MessageChain playerList() {

        JsonObject json;
        try {
            json = Api.hypixel("counts");
        } catch (URISyntaxException | IOException e) {
            logger.error("请求失败", e);
            return new MessageChainBuilder().append("请求失败, 请检查控制台错误日志").build();
        }
        if ((!json.isJsonObject() || !json.has("success") || !json.get("success").getAsBoolean())) {
            return new MessageChainBuilder().append("请求失败").build();
        }

        MessageChainBuilder chain = new MessageChainBuilder();
        JsonObject games = json.get("games").getAsJsonObject();
        chain.append("总人数: ");
        chain.append(new PlainText(String.valueOf(json.get("playerCount").getAsInt())));

        if (games.has("MAIN_LOBBY") && games.get("MAIN_LOBBY").getAsJsonObject().has("players")) {
            chain.append("\n| 主大厅: ");
            chain.append(new PlainText(String.valueOf(games.get("MAIN_LOBBY").getAsJsonObject().get("players").getAsInt())));
            if (games.has("TOURNAMENT_LOBBY") && games.get("TOURNAMENT_LOBBY").getAsJsonObject().has("players")) {
                chain.append(" | 锦标赛大厅: ");
                chain.append(new PlainText(String.valueOf(games.get("TOURNAMENT_LOBBY").getAsJsonObject().get("players").getAsInt())));
            }
        }
        if (games.has("IDLE") && games.get("IDLE").getAsJsonObject().has("players")) {
            chain.append("\n| 大厅闲置: ");
            chain.append(new PlainText(String.valueOf(games.get("IDLE").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("SKYBLOCK") && games.get("SKYBLOCK").getAsJsonObject().has("players")) {
            chain.append("\n| SkyBlock: ");
            chain.append(new PlainText(String.valueOf(games.get("SKYBLOCK").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("BEDWARS") && games.get("BEDWARS").getAsJsonObject().has("players")) {
            chain.append("\n| 起床战争: ");
            chain.append(new PlainText(String.valueOf(games.get("BEDWARS").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("SKYWARS") && games.get("SKYWARS").getAsJsonObject().has("players")) {
            chain.append("\n| 空岛战争: ");
            chain.append(new PlainText(String.valueOf(games.get("SKYWARS").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("DUELS") && games.get("DUELS").getAsJsonObject().has("players")) {
            chain.append("\n| 决斗游戏: ");
            chain.append(new PlainText(String.valueOf(games.get("DUELS").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("ARCADE") && games.get("ARCADE").getAsJsonObject().has("players")) {
            chain.append("\n| 街机游戏: ");
            chain.append(new PlainText(String.valueOf(games.get("ARCADE").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("BUILD_BATTLE") && games.get("BUILD_BATTLE").getAsJsonObject().has("players")) {
            chain.append("\n| 建筑大师: ");
            chain.append(new PlainText(String.valueOf(games.get("BUILD_BATTLE").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("MURDER_MYSTERY") && games.get("MURDER_MYSTERY").getAsJsonObject().has("players")) {
            chain.append("\n| 密室杀手: ");
            chain.append(new PlainText(String.valueOf(games.get("MURDER_MYSTERY").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("WOOL_GAMES") && games.get("WOOL_GAMES").getAsJsonObject().has("players")) {
            chain.append("\n| 羊毛游戏: ");
            chain.append(new PlainText(String.valueOf(games.get("WOOL_GAMES").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("TNTGAMES") && games.get("TNTGAMES").getAsJsonObject().has("players")) {
            chain.append("\n| TNT Games: ");
            chain.append(new PlainText(String.valueOf(games.get("TNTGAMES").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("WALLS3") && games.get("WALLS3").getAsJsonObject().has("players")) {
            chain.append("\n| 超级战墙: ");
            chain.append(new PlainText(String.valueOf(games.get("WALLS3").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("HOUSING") && games.get("HOUSING").getAsJsonObject().has("players")) {
            chain.append("\n| 家园: ");
            chain.append(new PlainText(String.valueOf(games.get("HOUSING").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("PIT") && games.get("PIT").getAsJsonObject().has("players")) {
            chain.append("\n| 天坑乱斗: ");
            chain.append(new PlainText(String.valueOf(games.get("PIT").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("SURVIVAL_GAMES") && games.get("SURVIVAL_GAMES").getAsJsonObject().has("players")) {
            chain.append("\n| 闪电饥饿游戏: ");
            chain.append(new PlainText(String.valueOf(games.get("SURVIVAL_GAMES").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("LEGACY") && games.get("LEGACY").getAsJsonObject().has("players")) {
            chain.append("\n| 经典游戏: ");
            chain.append(new PlainText(String.valueOf(games.get("LEGACY").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("PROTOTYPE") && games.get("PROTOTYPE").getAsJsonObject().has("players")) {
            chain.append("\n| 实验游戏: ");
            chain.append(new PlainText(String.valueOf(games.get("PROTOTYPE").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("SUPER_SMASH") && games.get("SUPER_SMASH").getAsJsonObject().has("players")) {
            chain.append("\n| 星碎英雄: ");
            chain.append(new PlainText(String.valueOf(games.get("SUPER_SMASH").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("SMP") && games.get("SMP").getAsJsonObject().has("players")) {
            chain.append("\n| SMP: ");
            chain.append(new PlainText(String.valueOf(games.get("SMP").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("UHC") && games.get("UHC").getAsJsonObject().has("players")) {
            chain.append("\n| UHC: ");
            chain.append(new PlainText(String.valueOf(games.get("UHC").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("REPLAY") && games.get("REPLAY").getAsJsonObject().has("players")) {
            chain.append("\n| replay: ");
            chain.append(new PlainText(String.valueOf(games.get("REPLAY").getAsJsonObject().get("players").getAsInt())));
        }
        if (games.has("LIMBO") && games.get("LIMBO").getAsJsonObject().has("players")) {
            chain.append("\n| Limbo: ");
            chain.append(new PlainText(String.valueOf(games.get("LIMBO").getAsJsonObject().get("players").getAsInt())));
        }
        return chain.build();
    }
}
