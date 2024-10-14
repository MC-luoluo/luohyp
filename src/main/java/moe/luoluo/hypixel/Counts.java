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

        String[][] gameList = {
                {"MAIN_LOBBY","主大厅"},
                {"IDLE","其他大厅"},
                {"SKYBLOCK","SkyBlock"},
                {"BEDWARS","起床战争"},
                {"SKYWARS","空岛战争"},
                {"DUELS","决斗游戏"},
                {"ARCADE","街机游戏"},
                {"BUILD_BATTLE","建筑大师"},
                {"MURDER_MYSTERY","密室杀手"},
                {"WOOL_GAMES","羊毛游戏"},
                {"TNTGAMES","TNT Games"},
                {"WALLS3","超级战墙"},
                {"HOUSING","家园"},
                {"PIT","天坑乱斗"},
                {"SURVIVAL_GAMES","闪电饥饿游戏"},
                {"LEGACY","经典游戏"},
                {"PROTOTYPE","实验游戏"},
                {"SUPER_SMASH","星碎英雄"},
                {"SMP","SMP"},
                {"UHC","UHC"},
                {"REPLAY","replay"},
                {"LIMBO","Limbo"}
        };

        for (String[] x : gameList) {
            if (games.has(x[0]) && games.get(x[0]).getAsJsonObject().has("players")) {
                chain.append(("\n| ")).append(x[1]).append(": ");
                chain.append(new PlainText(String.valueOf(games.get(x[0]).getAsJsonObject().get("players").getAsInt())));
            }
        }

        return chain.build();
    }
}
