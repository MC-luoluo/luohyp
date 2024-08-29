package moe.luoluo.hypixel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import moe.luoluo.Api;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

public class TNT {
    public static MessageChain tnt(String player, String type) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();
        JsonObject json = new Gson().fromJson(Api.hypixel("player", Api.mojang(player, "uuid")), JsonObject.class);
        JsonObject playerJson = json.get("player").getAsJsonObject();

        if (playerJson.has("stats") && playerJson.get("stats").getAsJsonObject().has("TNTGames")) {
            JsonObject tntJson = playerJson.get("stats").getAsJsonObject().get("TNTGames").getAsJsonObject();
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            chain.append(new PlainText(Rank.rank(playerJson) + " "));
            chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
            chain.append(new PlainText(" | TNT Games数据:"));

            chain.append(new PlainText("\n胜场: "));
            if (tntJson.has("wins")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("wins").getAsInt())));
            } else {
                chain.append(new PlainText("null"));
            }
            chain.append(new PlainText(" | 连胜: "));
            if (tntJson.has("winstreak")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("winstreak").getAsInt())));
            } else {
                chain.append(new PlainText("null"));
            }
            if (tntJson.has("coins")) {
                chain.append(new PlainText("\n硬币: "));
                chain.append(new PlainText(String.valueOf(tntJson.get("coins").getAsInt())));
            }


            chain.append(new PlainText("\nTNT Tag: "));

            chain.append(new PlainText("\n  胜场: "));
            if (tntJson.has("wins_tntag")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("wins_tntag").getAsInt())));
                if (tntJson.has("deaths_tntag")) {
                    chain.append(new PlainText(" | WLR: "));
                    chain.append(new PlainText(decimalFormat.format(
                            (double) tntJson.get("wins_tntag").getAsInt() /
                                    (double) tntJson.get("deaths_tntag").getAsInt())));
                }
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n  击杀: "));
            if (tntJson.has("kills_tntag")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("kills_tntag").getAsInt())));
            } else chain.append(new PlainText("null"));
            chain.append(new PlainText(" | 死亡:"));
            if (tntJson.has("deaths_tntag")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("deaths_tntag").getAsInt())));
            } else chain.append(new PlainText("null"));
            if (tntJson.has("kills_tntag") && tntJson.has("deaths_tntag")) {
                chain.append(new PlainText(" | KDR:"));
                chain.append(new PlainText(decimalFormat.format(
                        (double) tntJson.get("kills_tntag").getAsInt() /
                                (double) tntJson.get("deaths_tntag").getAsInt())));
            }


            chain.append(new PlainText("\nTNT Run: "));

            if (tntJson.has("record_tntrun")) {
                chain.append(new PlainText("\n  最长生存时间: "));
                int record = tntJson.get("record_tntrun").getAsInt();
                chain.append(new PlainText((record / 60) + ":" + String.format("%02d", record % 60)));
            }

            chain.append(new PlainText("\n  胜场: "));
            if (tntJson.has("wins_tntrun")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("wins_tntrun").getAsInt())));
            } else chain.append(new PlainText("null"));
            chain.append(new PlainText(" | 死亡: "));
            if (tntJson.has("deaths_tntrun")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("deaths_tntrun").getAsInt())));
            } else chain.append(new PlainText("null"));
            if (tntJson.has("wins_tntrun") && tntJson.has("deaths_tntrun")) {
                chain.append(new PlainText(" | WLR: "));
                chain.append(new PlainText(decimalFormat.format(
                        (double) tntJson.get("wins_tntrun").getAsInt() /
                                (double) tntJson.get("deaths_tntrun").getAsInt())));
            }


            chain.append(new PlainText("\nPVP TNT Run: "));

            if (tntJson.has("record_pvprun")) {
                chain.append(new PlainText("\n  最长生存时间: "));
                int record = tntJson.get("record_pvprun").getAsInt();
                chain.append(new PlainText(record >= 60 ?
                        (record / 60) + ":" + String.format("%02d", record % 60) :
                        "0:" + String.format("%02d", record % 60)
                ));
            }

            chain.append(new PlainText("\n  胜场: "));
            if (tntJson.has("wins_pvprun")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("wins_pvprun").getAsInt())));
                if (tntJson.has("deaths_pvprun")) {
                    chain.append(new PlainText(" | WLR: "));
                    chain.append(new PlainText(decimalFormat.format(
                            (double) tntJson.get("wins_pvprun").getAsInt() /
                                    (double) tntJson.get("deaths_pvprun").getAsInt())));
                }
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n  击杀: "));
            if (tntJson.has("kills_pvprun")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("kills_pvprun").getAsInt())));
            } else chain.append(new PlainText("null"));
            chain.append(new PlainText(" | 死亡: "));
            if (tntJson.has("deaths_pvprun")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("deaths_pvprun").getAsInt())));
            } else chain.append(new PlainText("null"));
            if (tntJson.has("kills_pvprun") && tntJson.has("deaths_pvprun")) {
                chain.append(new PlainText(" | KDR: "));
                chain.append(new PlainText(decimalFormat.format(
                        (double) tntJson.get("kills_pvprun").getAsInt() /
                                (double) tntJson.get("deaths_pvprun").getAsInt())));
            }


            chain.append(new PlainText("\nBow Spleef: "));
            chain.append(new PlainText("\n  胜场: "));
            if (tntJson.has("wins_bowspleef")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("wins_bowspleef").getAsInt())));
            } else chain.append(new PlainText("null"));
            chain.append(new PlainText(" | 死亡: "));
            if (tntJson.has("deaths_bowspleef")) {
                chain.append(new PlainText(String.valueOf(tntJson.get("deaths_bowspleef").getAsInt())));
            } else chain.append(new PlainText("null"));
            if (tntJson.has("wins_bowspleef") && tntJson.has("deaths_bowspleef")) {
                chain.append(new PlainText(" | WLR: "));
                chain.append(new PlainText(decimalFormat.format(
                        (double) tntJson.get("wins_bowspleef").getAsInt() /
                                (double) tntJson.get("deaths_bowspleef").getAsInt())));
            }
            if (tntJson.has("tags_bowspleef")) {
                chain.append(new PlainText("\n  射箭数: "));
                chain.append(new PlainText(String.valueOf(tntJson.get("tags_bowspleef").getAsInt())));
            }
        } else {
            chain.append(new PlainText(" 该玩家的TNT游戏数据为空"));
        }
        return chain.build();
    }
}
