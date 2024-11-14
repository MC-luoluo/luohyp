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


public class Warlords {
    public static MessageChain Battleground(String player) throws IOException, URISyntaxException {
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

        if (!(json.get("player").isJsonObject() && json.get("player").getAsJsonObject().has("stats") && json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("Battleground"))) {
            chain.append(new PlainText("该玩家的战争领主数据为空"));
            return chain.build();
        }

        JsonObject wlJson = json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("Battleground").getAsJsonObject();
        chain.append(new PlainText(Rank.rank(json.get("player").getAsJsonObject()) + " ")); //玩家名称前缀
        chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
        chain.append(new PlainText(" | 战争领主数据: "));

        chain.append("\n硬币: ");
        if (wlJson.has("coins"))
            chain.append(largeNumFormat(wlJson.get("coins").getAsInt()));
        else chain.append("0");
        if (wlJson.has("magic_dust"))
            chain.append(" | 魔法粉尘: ").append(String.valueOf(wlJson.get("magic_dust").getAsInt()));
        if (wlJson.has("void_shards"))
            chain.append(" | 虚空结晶: ").append(String.valueOf(wlJson.get("void_shards").getAsInt()));

        chain.append("\nMVP次数: ");
        if (wlJson.has("mvp_count"))
            chain.append(String.valueOf(wlJson.get("mvp_count").getAsInt()));
        else chain.append("0");
        if (wlJson.has("win_streak"))
            chain.append(" | 连胜: ").append(String.valueOf(wlJson.get("win_streak").getAsInt()));
        if (wlJson.has("afk_warned"))
            chain.append(" | AFK场次: ").append(String.valueOf(wlJson.get("afk_warned").getAsInt()));

        if (wlJson.has("chosen_class")) {
            chain.append("\n职业: ").append(wlJson.get("chosen_class").getAsString());
            if (wlJson.has(wlJson.get("chosen_class").getAsString() + "_spec"))
                chain.append("_").append(wlJson.get(
                        wlJson.get("chosen_class").getAsString() + "_spec"
                ).getAsString());
            if (wlJson.has("play_streak"))
                chain.append(" | 游戏连续度: ").append(String.valueOf(wlJson.get("play_streak").getAsInt() * 100 / 5)).append("%");
        }

        chain.append("\n胜场: ");
        if (wlJson.has("wins"))
            chain.append(String.valueOf(wlJson.get("wins").getAsInt()));
        else chain.append("null");
        chain.append(" | 败场: ");
        if (wlJson.has("losses"))
            chain.append(String.valueOf(wlJson.get("losses").getAsInt()));
        else chain.append("null");
        if (wlJson.has("wins")) {
            chain.append(" | WLR: ");
            if (wlJson.has("losses"))
                chain.append(decimalFormat.format(
                        (float) wlJson.get("wins").getAsInt() /
                                wlJson.get("losses").getAsInt()
                ));
            else chain.append(String.valueOf(wlJson.get("wins").getAsInt()));
        }

        chain.append("\n击杀: ");
        if (wlJson.has("kills"))
            chain.append(String.valueOf(wlJson.get("kills").getAsInt()));
        else chain.append("null");
        chain.append(" | 死亡: ");
        if (wlJson.has("deaths"))
            chain.append(String.valueOf(wlJson.get("deaths").getAsInt()));
        if (wlJson.has("kills")) {
            chain.append(" | KDR: ");
            if (wlJson.has("deaths"))
                chain.append(decimalFormat.format(
                        (float) wlJson.get("kills").getAsInt() /
                                wlJson.get("deaths").getAsInt()
                ));
            else chain.append(String.valueOf(wlJson.get("deaths").getAsInt()));
        }

        if (wlJson.has("assists")) {
            chain.append("\n助攻: ").append(String.valueOf(wlJson.get("assists").getAsInt()));
            chain.append(" | (K+A)/D: ");
            float ka = wlJson.get("assists").getAsInt();
            if (wlJson.has("kills"))
                ka += wlJson.get("kills").getAsInt();
            if (wlJson.has("deaths"))
                chain.append(decimalFormat.format(
                        ka / wlJson.get("deaths").getAsInt()
                ));
        }

        if (wlJson.has("damage")) {
            chain.append("\n造成伤害: ").append(largeNumFormat(wlJson.get("damage").getAsFloat()));
            if (wlJson.has("heal"))
                chain.append(" | 治疗: ").append(largeNumFormat(wlJson.get("heal").getAsFloat()));
            if (wlJson.has("life_leeched"))
                chain.append(" | 吸血: ").append(largeNumFormat(wlJson.get("life_leeched").getAsFloat()));
        }

        return chain.build();
    }
}
