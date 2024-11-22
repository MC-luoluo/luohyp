package moe.luoluo.hypixel;

import com.google.gson.JsonObject;
import moe.luoluo.Api;
import moe.luoluo.ApiResult;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import static moe.luoluo.util.Format.*;

public class CAC {
    public static void mcgo(CommandSender context, String player, String type) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();

        type = type.toLowerCase();

        ApiResult result;
        JsonObject json;

        String uuid = Api.mojang(player, "uuid");
        if (Objects.equals(uuid, "NotFound")) {
            chain.append("玩家不存在");
            context.sendMessage(chain.build());
            return;
        } else {
            result = Api.hypixel("player", uuid);
            json = result.getJson();
        }

        if (result.getTime() != -1) {
            Instant instant = Instant.ofEpochMilli(result.getTime());
            LocalDateTime localDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            chain.append("\uD83D\uDFE5").append(localDate.toString()).append("\n");
        }

        if (!(json.get("player").isJsonObject() && json.get("player").getAsJsonObject().has("stats") && json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("MCGO"))) {
            chain.append(new PlainText("该玩家的警匪大战数据为空"));
            context.sendMessage(chain.build());
            return;
        }

        JsonObject cacJson = json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("MCGO").getAsJsonObject();

        int totalWins = 0;
        int totalKills = 0;
        int totalDeaths = 0;
        int totalAssists = 0;

        if (cacJson.has("game_wins"))
            totalWins += cacJson.get("game_wins").getAsInt();
        if (cacJson.has("game_wins_deathmatch"))
            totalWins += cacJson.get("game_wins_deathmatch").getAsInt();
        if (cacJson.has("game_wins_gungame"))
            totalWins += cacJson.get("game_wins_gungame").getAsInt();

        if (cacJson.has("kills"))
            totalKills += cacJson.get("kills").getAsInt();
        if (cacJson.has("kills_deathmatch"))
            totalKills += cacJson.get("kills_deathmatch").getAsInt();
        if (cacJson.has("kills_gungame"))
            totalKills += cacJson.get("kills_gungame").getAsInt();

        if (cacJson.has("deaths"))
            totalDeaths += cacJson.get("deaths").getAsInt();
        if (cacJson.has("deaths_deathmatch"))
            totalDeaths += cacJson.get("deaths_deathmatch").getAsInt();
        if (cacJson.has("deaths_gungame"))
            totalDeaths += cacJson.get("deaths_gungame").getAsInt();

        if (cacJson.has("assists"))
            totalAssists += cacJson.get("assists").getAsInt();
        if (cacJson.has("assists_deathmatch"))
            totalAssists += cacJson.get("assists_deathmatch").getAsInt();
        if (cacJson.has("assists_gungame"))
            totalAssists += cacJson.get("assists_gungame").getAsInt();


        chain.append(new PlainText(Rank.rank(json.get("player").getAsJsonObject()) + " ")); //玩家名称前缀
        chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
        chain.append(new PlainText(" | 警匪大战数据: "));

        //总模式
        chain.append("\n 硬币: ");
        if (cacJson.has("coins"))
            chain.append(largeNumFormat(cacJson.get("coins").getAsInt()));
        else chain.append("0");

        if (type.isEmpty() || type.equals("all")) {
            chain.append("\n 胜场: ").append(String.valueOf(totalWins));

            chain.append("\n 击杀: ").append(String.valueOf(totalKills));
            chain.append(" | 死亡: ").append(String.valueOf(totalDeaths));
            if (totalDeaths != 0)
                chain.append(" | K/D: ").append(decimalFormat.format((float)totalKills / totalDeaths));
            else chain.append(" | K/D: ").append(decimalFormat.format((float)totalKills));

            chain.append("\n 助攻: ").append(String.valueOf(totalAssists));
            if (totalDeaths != 0)
                chain.append(" | (K+A)/D: ").append(decimalFormat.format(((float)totalKills + totalAssists) / totalDeaths));
            else chain.append(" | (K+A)/D: ").append(decimalFormat.format((float)totalKills + totalAssists));

            if (cacJson.has("shots_fired"))
                chain.append("\n 射击次数: ").append(String.valueOf(cacJson.get("shots_fired").getAsInt()));
            else chain.append("\n 射击次数: 0");
            if (cacJson.has("headshot_kills"))
                chain.append(" | 爆头击杀数: ").append(String.valueOf(cacJson.get("headshot_kills").getAsInt()));
            else chain.append(" | 爆头击杀数: 0");
        }

        chain.append("\n");

        //爆破模式
        if (type.equals("boom") || type.equals("def") || type.equals("defusal") || type.equals("all")) {
            chain.append("\n|- 爆破模式:");
            chain.append("\n| 胜场: ").append(String.valueOf(cacJson.get("game_wins").getAsInt()));
            if (cacJson.has("round_wins"))
                chain.append(" | 回合胜利数: ").append(String.valueOf(cacJson.get("round_wins").getAsInt()));
            else chain.append(" | 回合胜利数: 0");
            chain.append("\n| 击杀: ").append(String.valueOf(cacJson.get("kills").getAsInt()));

            if (cacJson.has("deaths")) {
                chain.append(" | 死亡: ").append(String.valueOf(cacJson.get("deaths").getAsInt()));
                if (cacJson.get("deaths").getAsInt() != 0)
                    chain.append(" | K/D: ").append(decimalFormat.format((float)cacJson.get("kills").getAsInt() / cacJson.get("deaths").getAsInt()));
                else chain.append(" | K/D: ").append(decimalFormat.format((float)cacJson.get("kills").getAsInt()));
            }else chain.append(" | 死亡: 0").append(" | K/D: ").append(decimalFormat.format((float)cacJson.get("kills").getAsInt()));

            if (cacJson.has("assists")) {
                chain.append("\n| 助攻: ").append(String.valueOf(cacJson.get("assists").getAsInt()));
                if (cacJson.get("deaths").getAsInt() != 0)
                    chain.append(" | (K+A)/D: ").append(decimalFormat.format(
                            ((float)cacJson.get("kills").getAsInt() + cacJson.get("assists").getAsInt()) / cacJson.get("deaths").getAsInt()));
                else chain.append(" | (K+A)/D: ").append(decimalFormat.format(((float)cacJson.get("kills").getAsInt() + cacJson.get("assists").getAsInt())));
            }else {
                chain.append("\n| 助攻: 0");
                if (cacJson.has("deaths") && cacJson.get("deaths").getAsInt() != 0)
                    chain.append(" | (K+A)/D: ").append(decimalFormat.format((float)cacJson.get("kills").getAsInt() / cacJson.get("deaths").getAsInt()));
                else chain.append(" | (K+A)/D: ").append(decimalFormat.format((float)cacJson.get("kills").getAsInt()));
            }

            chain.append("\n| 炸弹安放: ").append(String.valueOf(cacJson.get("bombs_planted").getAsInt()));
            chain.append("  | 炸弹拆除: ").append(String.valueOf(cacJson.get("bombs_defused").getAsInt()));
        }

        //死斗模式
        if (type.equals("dm") || type.equals("deathmatch") || type.equals("all")) {
            chain.append("\n|- 死斗模式:");
            chain.append("\n| 胜场: ").append(String.valueOf(cacJson.get("game_wins_deathmatch").getAsInt()));
            chain.append("\n| 击杀: ").append(String.valueOf(cacJson.get("kills_deathmatch").getAsInt()));

            if (cacJson.has("deaths_deathmatch")) {
                chain.append(" | 死亡: ").append(String.valueOf(cacJson.get("deaths_deathmatch").getAsInt()));
                if (cacJson.get("deaths_deathmatch").getAsInt() != 0)
                    chain.append(" | K/D: ").append(decimalFormat.format(
                            (float)cacJson.get("kills_deathmatch").getAsInt() / cacJson.get("deaths_deathmatch").getAsInt()));
                else chain.append(" | K/D: ").append(decimalFormat.format((float)cacJson.get("kills_deathmatch").getAsInt()));
            }else chain.append(" | 死亡: 0").append(" | K/D: ").append(decimalFormat.format((float)cacJson.get("kills_deathmatch").getAsInt()));

            if (cacJson.has("assists_deathmatch")) {
                chain.append("\n| 助攻: ").append(String.valueOf(cacJson.get("assists_deathmatch").getAsInt()));
                if (cacJson.get("deaths_deathmatch").getAsInt() != 0)
                    chain.append(" | (K+A)/D: ").append(decimalFormat.format(
                            ((float)cacJson.get("kills_deathmatch").getAsInt() + cacJson.get("assists_deathmatch").getAsInt())
                                    / cacJson.get("deaths_deathmatch").getAsInt()));
                else chain.append(" | (K+A)/D: ").append(decimalFormat.format(((float)cacJson.get("kills_deathmatch").getAsInt())));
            }else {
                chain.append("\n| 助攻: 0");
                if (cacJson.has("deaths_deathmatch") && cacJson.get("deaths_deathmatch").getAsInt() != 0)
                    chain.append(" | (K+A)/D :").append(decimalFormat.format(
                            (float)cacJson.get("kills_deathmatch").getAsInt() / cacJson.get("deaths_deathmatch").getAsInt()));
                else chain.append(" | (K+A)/D: ").append(decimalFormat.format((float)cacJson.get("kills_deathmatch").getAsInt()));
            }

        }

        //gungame
        if (type.equals("gun") || type.equals("gungame") || type.equals("all")) {
            chain.append("\n|- gungame");
            chain.append("\n胜场: ").append(String.valueOf(cacJson.get("game_wins_gungame").getAsInt()));
            chain.append("\n| 击杀: ").append(String.valueOf(cacJson.get("kills_gungame").getAsInt()));

            if (cacJson.has("deaths_gungame")) {
                chain.append(" | 死亡: ").append(String.valueOf(cacJson.get("deaths_gungame").getAsInt()));
                if (cacJson.get("deaths_gungame").getAsInt() != 0)
                    chain.append(" | K/D: ").append(decimalFormat.format(
                            (float)cacJson.get("kills_gungame").getAsInt() / cacJson.get("deaths_gungame").getAsInt()));
                else chain.append(" | K/D: ").append(decimalFormat.format((float)cacJson.get("kills_gungame").getAsInt()));
            }else chain.append(" | 死亡: 0").append(" | K/D: ").append(decimalFormat.format((float)cacJson.get("kills_gungame").getAsInt()));

            if (cacJson.has("assists_gungame")) {
                chain.append("\n| 助攻: ").append(String.valueOf(cacJson.get("assists_gungame").getAsInt()));
                if (cacJson.get("deaths_gungame").getAsInt() != 0)
                    chain.append(" | (K+A/D): ").append(decimalFormat.format(
                            ((float)cacJson.get("kills_gungame").getAsInt() + cacJson.get("assists_gungame").getAsInt())
                                    / cacJson.get("deaths_gungame").getAsInt()));
                else chain.append(" | (K+A)/D: ").append(decimalFormat.format(
                        (float)cacJson.get("kills_gungame").getAsInt() + cacJson.get("assists_gungame").getAsInt()));
            }else {
                chain.append("\n| 助攻: 0");
                if (cacJson.has("deaths_gungame") && cacJson.get("deaths_gungame").getAsInt() != 0)
                    chain.append(" | (K+A)/D: ").append(decimalFormat.format(
                            (float)cacJson.get("kills_gungame").getAsInt() / cacJson.get("deaths_gungame").getAsInt()));
                else chain.append(" | (K+A)/D :").append(decimalFormat.format((float)cacJson.get("kills_gungame").getAsInt()));
            }

            if (cacJson.has("fastest_win_gungame")) {
                int secs = cacJson.get("fastest_win_gungame").getAsInt() / 1000;
                if (secs == 0)
                    chain.append("\n最快胜利: -");
                else if (secs >= 60) {
                    int mins = secs / 60;
                    secs -= mins * 60;
                    chain.append("\n最快胜利: ").append(String.valueOf(mins)).append("m,").append(String.valueOf(secs)).append("s");
                }else
                    chain.append("\n最快胜利: ").append(String.valueOf(secs)).append("s,").append(String.valueOf((cacJson.get("fastest_win_gungame").getAsInt() % 1000))).append("ms");
            }else chain.append("\n最快胜利: -");

        }

        if (type.isEmpty() || type.equals("all") || type.equals("boom") || type.equals("def") || type.equals("defusal")
                || type.equals("dm") || type.equals("deathmatch") ||type.equals("gun") || type.equals("gungame"))
            context.sendMessage(chain.build());
        else context.sendMessage("type有误，可用参数：all, def, defusal, dm, deathmatch, gun, gungame");

    }
}
