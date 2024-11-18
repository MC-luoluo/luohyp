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

public class TurboKartRacers {
    public static MessageChain GingerBread(String player) throws IOException, URISyntaxException {
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

        if (!(json.get("player").isJsonObject() && json.get("player").getAsJsonObject().has("stats") && json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("GingerBread"))) {
            chain.append(new PlainText("该玩家的方块赛车数据为空"));
            return chain.build();
        }

        JsonObject tkrJson = json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("GingerBread").getAsJsonObject();

        int games = 0;
        String goldTrophyRate = null;
        String silverTrophyRate = null;
        String bronzeTrophyRate = null;

        if (tkrJson.has("junglerush_plays"))
            games += tkrJson.get("junglerush_plays").getAsInt();
        if (tkrJson.has("canyon_plays"))
            games += tkrJson.get("canyon_plays").getAsInt();
        if (tkrJson.has("retro_plays"))
            games += tkrJson.get("retro_plays").getAsInt();
        if (tkrJson.has("olympus_plays"))
            games += tkrJson.get("olympus_plays").getAsInt();
        if (tkrJson.has("hypixelgp_plays"))
            games += tkrJson.get("hypixelgp_plays").getAsInt();

        if (games != 0) {
            if (tkrJson.has("gold_trophy"))
                goldTrophyRate = decimalFormat.format(100.0 * tkrJson.get("gold_trophy").getAsInt() / games) + "%";
            if (tkrJson.has("silver_trophy"))
                silverTrophyRate = decimalFormat.format(100.0 * tkrJson.get("silver_trophy").getAsInt() / games) + "%";
            if (tkrJson.has("bronze_trophy"))
                bronzeTrophyRate = decimalFormat.format(100.0 * tkrJson.get("bronze_trophy").getAsInt() / games) + "%";
        }

        chain.append(new PlainText(Rank.rank(json.get("player").getAsJsonObject()) + " "));
        chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
        chain.append(new PlainText(" | 方块赛车数据："));

        chain.append("\n硬币: ");
        if (tkrJson.has("coins"))
            chain.append(largeNumFormat(tkrJson.get("coins").getAsInt()));
        else chain.append("0");
        if (tkrJson.has("coins_picked_up"))
            chain.append(" | 捡起硬币: ").append(largeNumFormat(tkrJson.get("coins_picked_up").getAsInt()));
        if (tkrJson.has("box_pickups"))
            chain.append(" | 捡起盒子: ").append(largeNumFormat(tkrJson.get("box_pickups").getAsInt()));

        chain.append("\n游戏次数: ").append(String.valueOf(games));
        if (tkrJson.has("wins"))
            chain.append(" | 胜利数: ").append(String.valueOf(tkrJson.get("wins").getAsInt()));
        if (tkrJson.has("laps_completed"))
            chain.append(" | 完成圈: ").append(largeNumFormat(tkrJson.get("laps_completed").getAsInt()));

        if (tkrJson.has("gold_trophy")) {
            chain.append("\n金奖杯: ");
            chain.append(String.valueOf(tkrJson.get("gold_trophy").getAsInt()));
            chain.append(" | 金奖杯占比: ").append(goldTrophyRate);
        }
        if (tkrJson.has("silver_trophy")) {
            chain.append("\n银奖杯: ");
            chain.append(String.valueOf(tkrJson.get("silver_trophy").getAsInt()));
            chain.append(" | 银奖杯占比: ").append(silverTrophyRate);
        }
        if (tkrJson.has("bronze_trophy")) {
            chain.append("\n铜奖杯: ");
            chain.append(String.valueOf(tkrJson.get("bronze_trophy").getAsInt()));
            chain.append(" | 铜奖杯占比: ").append(bronzeTrophyRate);
        }

        return chain.build();
    }
}
