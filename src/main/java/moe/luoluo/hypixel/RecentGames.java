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
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class RecentGames {
    public static MessageChain recentGames(String player) throws IOException, URISyntaxException, SQLException {
        MessageChainBuilder chain = new MessageChainBuilder();

        ApiResult result;
        JsonObject json;
        String uuid = Api.mojang(player, "uuid");
        if (Objects.equals(uuid, "NotFound")) {
            chain.append("玩家不存在");
            return chain.build();
        } else {
            result = Api.hypixel("recentgames", uuid);
            json = result.getJson();
        }

        if (result.getTime() != -1) {
            Instant instant = Instant.ofEpochMilli(result.getTime());
            LocalDateTime localDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            chain.append("\uD83D\uDFE5").append(localDate.toString()).append("\n");
        }

        if (!(json.get("games").isJsonArray() && !json.get("games").getAsJsonArray().isEmpty())) {
            chain.append(new PlainText("该玩家的最近游玩为空"));
            return chain.build();
        }

        chain.append(Api.mojang(uuid, "name"));
        chain.append(" | 最近游玩: ");

        JsonArray games = json.get("games").getAsJsonArray();
        for (int i = 0; i < games.size(); i++) {
            JsonObject game = games.get(i).getAsJsonObject();
            if (game.has("date")) {
                chain.append("\n|-时间: ");
                long date = game.get("date").getAsLong();
                Instant instant = Instant.ofEpochMilli(date);
                LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                chain.append(localDateTime.toString());
            }
            if (game.has("gameType") && !game.get("gameType").isJsonNull())
                chain.append("\n| 游戏: ").append(game.get("gameType").getAsString());
            if (game.has("mode") && !game.get("mode").isJsonNull())
                chain.append("\n| 模式: ").append(game.get("mode").getAsString());
            if (game.has("map") && !game.get("map").isJsonNull())
                chain.append("\n| 地图: ").append(game.get("map").getAsString());
            if (game.has("date") && game.has("ended")) {
                chain.append("\n| 游戏时长: ");
                long time = game.get("ended").getAsLong() - game.get("date").getAsLong();
                if (time > 3600000) {
                    chain.append(String.valueOf(time / 3600000)).append(":")
                            .append(String.valueOf(time % 3600000 / 60000)).append(":")
                            .append(String.valueOf(time % 60000 / 1000)).append(".")
                            .append(String.valueOf(time % 1000));
                } else if (time > 60000) {
                    chain.append(String.valueOf(time / 60000)).append(":")
                            .append(String.valueOf(time % 60000 / 1000)).append(".")
                            .append(String.valueOf(time % 1000));
                } else {
                    chain.append(String.valueOf(time % 60000 / 1000)).append(".")
                            .append(String.valueOf(time % 1000));
                }
            }
        }
        return chain.build();
    }
}
