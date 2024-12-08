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

import static moe.luoluo.util.Format.decimalFormat;
import static moe.luoluo.util.Format.largeNumFormat;

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
        int totalGamePlays = 0;
        int totalKills = 0;
        int totalDeaths = 0;
        int totalAssists = 0;

        if (cacJson.has("game_wins"))
            totalWins += cacJson.get("game_wins").getAsInt();
        if (cacJson.has("game_wins_deathmatch"))
            totalWins += cacJson.get("game_wins_deathmatch").getAsInt();
        if (cacJson.has("game_wins_gungame"))
            totalWins += cacJson.get("game_wins_gungame").getAsInt();

        if (cacJson.has("game_plays"))
            totalGamePlays += cacJson.get("game_plays").getAsInt();
        if (cacJson.has("game_plays_deathmatch"))
            totalGamePlays += cacJson.get("game_plays_deathmatch").getAsInt();
        if (cacJson.has("game_plays_gungame"))
            totalGamePlays += cacJson.get("game_plays_gungame").getAsInt();


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

        //总数据
        //等级 &经验
        if (cacJson.has("score")) {
            long score = cacJson.get("score").getAsLong();
            int[] expNeeded = {25, 50, 100, 150, 200, 250, 300, 350, 400, 500, 600, 700, 800, 900, 1000, 1250, 1500, 1750, 2000, 2200, 2400, 2600, 2800, 3000};

            chain.append(new PlainText("\n 等级: "));
            int level = 1;
            for (int j : expNeeded) {
                if (score >= j) {
                    score -= j;
                    level++;
                } else break;
            }
            while (score >= 3000) {
                level++;
                score -= 3000;
            }
            chain.append(new PlainText(String.valueOf(level)));

            //经验进度
            int xpLevel = Math.min(level - 1, 23);
            chain.append(new PlainText(" (" + score +
                    "/" + expNeeded[xpLevel] + " " +
                    decimalFormat.format((float) score / expNeeded[xpLevel] * 100) + "%)"
            ));
        }
        chain.append("\n 硬币: ");
        if (cacJson.has("coins"))
            chain.append(largeNumFormat(cacJson.get("coins").getAsInt()));
        else chain.append("0");
        if (cacJson.has("score"))
            chain.append(" | 分数").append(String.valueOf(cacJson.get("score").getAsLong()));

        if (type.isEmpty() || type.equals("all")) {
            chain.append("\n|- 总体数据");

            chain.append("\n| 胜场: ").append(String.valueOf(totalWins));
            chain.append(" | 完整游玩: ").append(String.valueOf(totalGamePlays));

            chain.append("\n| 击杀: ").append(String.valueOf(totalKills));
            chain.append(" | 死亡: ").append(String.valueOf(totalDeaths));
            if (totalDeaths != 0)
                chain.append(" | KDR: ").append(decimalFormat.format((float) totalKills / totalDeaths));
            else chain.append(" | KDR: ").append(decimalFormat.format((float) totalKills));

            chain.append("\n| 助攻: ").append(String.valueOf(totalAssists));
            if (totalDeaths != 0)
                chain.append(" | (K+A)/D: ").append(decimalFormat.format(((float) totalKills + totalAssists) / totalDeaths));
            else chain.append(" | (K+A)/D: ").append(decimalFormat.format((float) totalKills + totalAssists));

            if (cacJson.has("shots_fired"))
                chain.append("\n| 射击数: ").append(String.valueOf(cacJson.get("shots_fired").getAsInt()));
            else chain.append("\n| 射击数: 0");
            if (cacJson.has("headshot_kills"))
                chain.append(" | 爆头击杀: ").append(String.valueOf(cacJson.get("headshot_kills").getAsInt()));
            else chain.append(" | 爆头击杀: 0");
        }

        //爆破模式
        if (type.equals("boom") || type.equals("def") || type.equals("defusal") || type.equals("all")) {
            chain.append("\n|- 爆破模式:");

            chain.append("\n| 胜场: ").append(String.valueOf(cacJson.get("game_wins").getAsInt()));
            if (cacJson.has("game_plays"))
                chain.append(" | 完整游玩: ").append(String.valueOf(cacJson.get("game_plays").getAsInt()));

            chain.append("\n| 击杀: ").append(String.valueOf(cacJson.get("kills").getAsInt()));
            if (cacJson.has("deaths")) {
                chain.append(" | 死亡: ").append(String.valueOf(cacJson.get("deaths").getAsInt()));
                if (cacJson.get("deaths").getAsInt() != 0)
                    chain.append(" | KDR: ").append(decimalFormat.format((float) cacJson.get("kills").getAsInt() / cacJson.get("deaths").getAsInt()));
                else chain.append(" | KDR: ").append(decimalFormat.format((float) cacJson.get("kills").getAsInt()));
            } else
                chain.append(" | 死亡: 0").append(" | KDR: ").append(decimalFormat.format((float) cacJson.get("kills").getAsInt()));

            if (cacJson.has("assists")) {
                chain.append("\n| 助攻: ").append(String.valueOf(cacJson.get("assists").getAsInt()));
                if (cacJson.get("deaths").getAsInt() != 0)
                    chain.append(" | (K+A)/D: ").append(decimalFormat.format(
                            ((float) cacJson.get("kills").getAsInt() + cacJson.get("assists").getAsInt()) / cacJson.get("deaths").getAsInt()));
                else
                    chain.append(" | (K+A)/D: ").append(decimalFormat.format(((float) cacJson.get("kills").getAsInt() + cacJson.get("assists").getAsInt())));
            }

            chain.append("\n| 安放炸弹: ").append(String.valueOf(cacJson.get("bombs_planted").getAsInt()));
            chain.append("  | 拆除炸弹: ").append(String.valueOf(cacJson.get("bombs_defused").getAsInt()));
        }

        //死斗模式
        if (type.equals("dm") || type.equals("deathmatch") || type.equals("all")) {
            chain.append("\n|- 死斗模式:");
            chain.append("\n| 胜场: ").append(String.valueOf(cacJson.get("game_wins_deathmatch").getAsInt()));
            if (cacJson.has("game_plays_deathmatch"))
                chain.append(" | 完整游玩: ").append(String.valueOf(cacJson.get("game_plays_deathmatch").getAsInt()));
            chain.append("\n| 击杀: ").append(String.valueOf(cacJson.get("kills_deathmatch").getAsInt()));

            if (cacJson.has("deaths_deathmatch")) {
                chain.append(" | 死亡: ").append(String.valueOf(cacJson.get("deaths_deathmatch").getAsInt()));
                if (cacJson.get("deaths_deathmatch").getAsInt() != 0)
                    chain.append(" | KDR: ").append(decimalFormat.format(
                            (float) cacJson.get("kills_deathmatch").getAsInt() / cacJson.get("deaths_deathmatch").getAsInt()));
                else
                    chain.append(" | KDR: ").append(decimalFormat.format((float) cacJson.get("kills_deathmatch").getAsInt()));
            } else
                chain.append(" | 死亡: 0").append(" | KDR: ").append(decimalFormat.format((float) cacJson.get("kills_deathmatch").getAsInt()));

            if (cacJson.has("assists_deathmatch")) {
                chain.append("\n| 助攻: ").append(String.valueOf(cacJson.get("assists_deathmatch").getAsInt()));
                if (cacJson.get("deaths_deathmatch").getAsInt() != 0)
                    chain.append(" | (K+A)/D: ").append(decimalFormat.format(
                            ((float) cacJson.get("kills_deathmatch").getAsInt() + cacJson.get("assists_deathmatch").getAsInt())
                                    / cacJson.get("deaths_deathmatch").getAsInt()));
                else
                    chain.append(" | (K+A)/D: ").append(decimalFormat.format(((float) cacJson.get("kills_deathmatch").getAsInt())));
            } else {
                chain.append("\n| 助攻: 0");
                if (cacJson.has("deaths_deathmatch") && cacJson.get("deaths_deathmatch").getAsInt() != 0)
                    chain.append(" | (K+A)/D :").append(decimalFormat.format(
                            (float) cacJson.get("kills_deathmatch").getAsInt() / cacJson.get("deaths_deathmatch").getAsInt()));
                else
                    chain.append(" | (K+A)/D: ").append(decimalFormat.format((float) cacJson.get("kills_deathmatch").getAsInt()));
            }

        }

        //gungame
        if (type.equals("gun") || type.equals("gungame") || type.equals("all")) {
            chain.append("\n|- gungame");
            chain.append("\n| 胜场: ").append(String.valueOf(cacJson.get("game_wins_gungame").getAsInt()));
            if (cacJson.has("game_plays_gungame"))
                chain.append(" | 完整游玩: ").append(String.valueOf(cacJson.get("game_plays_gungame").getAsInt()));
            chain.append("\n| 击杀: ").append(String.valueOf(cacJson.get("kills_gungame").getAsInt()));

            if (cacJson.has("deaths_gungame")) {
                chain.append(" | 死亡: ").append(String.valueOf(cacJson.get("deaths_gungame").getAsInt()));
                if (cacJson.get("deaths_gungame").getAsInt() != 0)
                    chain.append(" | KDR: ").append(decimalFormat.format(
                            (float) cacJson.get("kills_gungame").getAsInt() / cacJson.get("deaths_gungame").getAsInt()));
                else
                    chain.append(" | KDR: ").append(decimalFormat.format((float) cacJson.get("kills_gungame").getAsInt()));
            } else
                chain.append(" | 死亡: 0").append(" | KDR: ").append(decimalFormat.format((float) cacJson.get("kills_gungame").getAsInt()));

            if (cacJson.has("assists_gungame")) {
                chain.append("\n| 助攻: ").append(String.valueOf(cacJson.get("assists_gungame").getAsInt()));
                if (cacJson.get("deaths_gungame").getAsInt() != 0)
                    chain.append(" | (K+A/D): ").append(decimalFormat.format(
                            ((float) cacJson.get("kills_gungame").getAsInt() + cacJson.get("assists_gungame").getAsInt())
                                    / cacJson.get("deaths_gungame").getAsInt()));
                else chain.append(" | (K+A)/D: ").append(decimalFormat.format(
                        (float) cacJson.get("kills_gungame").getAsInt() + cacJson.get("assists_gungame").getAsInt()));
            } else {
                chain.append("\n| 助攻: 0");
                if (cacJson.has("deaths_gungame") && cacJson.get("deaths_gungame").getAsInt() != 0)
                    chain.append(" | (K+A)/D: ").append(decimalFormat.format(
                            (float) cacJson.get("kills_gungame").getAsInt() / cacJson.get("deaths_gungame").getAsInt()));
                else
                    chain.append(" | (K+A)/D :").append(decimalFormat.format((float) cacJson.get("kills_gungame").getAsInt()));
            }

            if (cacJson.has("fastest_win_gungame") && cacJson.get("fastest_win_gungame").getAsInt() > 0) {
                chain.append(new PlainText("\n| 最快胜利: "));
                double fg = cacJson.get("fastest_win_gungame").getAsDouble() / 1000;
                chain.append(new PlainText(fg >= 60 ?
                        ((int) fg / 60) + ":" + String.format("%02d", (int) fg % 60) + ":" + String.format("%03d", Math.round((fg - (int) fg) * 1000)) :
                        String.format("%02d", (int) fg % 60) + ":" + String.format("%03d", Math.round((fg - (int) fg) * 1000))
                ));
            }

        }

        if (type.isEmpty() || type.equals("all") || type.equals("boom") || type.equals("def") || type.equals("defusal")
                || type.equals("dm") || type.equals("deathmatch") || type.equals("gun") || type.equals("gungame"))
            context.sendMessage(chain.build());
        else context.sendMessage("type有误，可用参数：all, def, defusal, dm, deathmatch, gun, gungame");

    }
}
