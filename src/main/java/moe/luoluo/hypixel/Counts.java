package moe.luoluo.hypixel;

import com.google.gson.JsonObject;
import moe.luoluo.Api;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class Counts {
    private static final Logger logger = LoggerFactory.getLogger(Counts.class);

    public static void playerList(CommandSender context, String type) {

        JsonObject json;
        try {
            json = Api.hypixel("counts");
        } catch (URISyntaxException | IOException e) {
            context.sendMessage(new MessageChainBuilder().append("请求失败, 请检查控制台错误日志").build());
            logger.error("请求失败", e);
            return;
        }
        if ((!json.isJsonObject() || !json.has("success") || !json.get("success").getAsBoolean())) {
            context.sendMessage(new MessageChainBuilder().append("请求失败").build());
            return;
        }

        MessageChainBuilder chain = new MessageChainBuilder();
        JsonObject games = json.get("games").getAsJsonObject();
        chain.append("总人数: ");
        chain.append(new PlainText(String.valueOf(json.get("playerCount").getAsInt())));

        String[][] gameList = {
                {"MAIN_LOBBY","主大厅"},
                {"IDLE","其他大厅"},
                {"SKYBLOCK","SkyBlock","dark_auction","Dark auction"},
                {"BEDWARS","起床战争"},
                {"SKYWARS","空岛战争"},
                {"DUELS","决斗游戏"},
                {"BUILD_BATTLE","建筑大师","BUILD_BATTLE_GUESS_THE_BUILD","建筑猜猜乐"},
                {"ARCADE","街机游戏","DROPPER","心跳水立方","ZOMBIES_DEAD_END","僵尸末日:穷途末路","ZOMBIES_BAD_BLOOD","僵尸末日:坏血之宫","ZOMBIES_ALIEN_ARCADIUM","僵尸末日:外星游乐园","ZOMBIES_PRISON","僵尸末日:监狱风云","PARTY","派对游戏","DAYONE","行尸走肉","HALLOWEEN_SIMULATOR","万圣节模拟器","PIXEL_PARTY","像素派对","SIMON_SAYS","我说你做"},
                {"MURDER_MYSTERY","密室杀手","MURDER_CLASSIC","经典","MURDER_DOUBLE_UP","双倍","MURDER_INFECTION","感染","MURDER_ASSASSINS","刺客"},
                {"WOOL_GAMES","羊毛游戏","sheep_wars_two_six","绵羊战争","wool_wars_two_four","羊毛战争","capture_the_wool_two_twenty","捕捉羊毛大作战"},
                {"TNTGAMES","TNT Games","TNTAG","TNT Tag","TNTRUN","TNT Run","CAPTURE","法师掘战","BOWSPLEEF","掘一死箭"},
                {"WALLS3","超级战墙"},
                {"HOUSING","家园"},
                {"PIT","天坑乱斗"},
                {"SURVIVAL_GAMES","闪电饥饿游戏"},
                {"LEGACY","经典游戏","QUAKECRAFT","未来射击","PAINTBALL","彩弹射击","VAMPIREZ","吸血鬼","WALLS","战墙"},
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
                chain.append(String.valueOf(games.get(x[0]).getAsJsonObject().get("players").getAsInt()));
                if (x.length > 2 && type.equals("all") && games.get(x[0]).getAsJsonObject().has("modes")) {
                    JsonObject modes = games.get(x[0]).getAsJsonObject().get("modes").getAsJsonObject();
                    for (int i = 2; i < x.length; i+=2) {
                        if (modes.has(x[i])) {
                            chain.append(("\n|- ")).append(x[i+1]).append(": ");
                            chain.append(String.valueOf(modes.get(x[i]).getAsInt()));
                        }
                    }
                }
            }
        }

        if (context.getSubject() != null) {
            ForwardMessageBuilder builder = new ForwardMessageBuilder(context.getSubject());
            builder.add(Objects.requireNonNull(context.getBot()).getId(), context.getBot().getNick(), chain.build());
            context.sendMessage(builder.build());
        } else context.sendMessage(chain.build());
    }
}
