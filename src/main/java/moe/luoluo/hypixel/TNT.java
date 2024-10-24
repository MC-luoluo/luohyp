package moe.luoluo.hypixel;

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
import java.util.Objects;

public class TNT {
    public static MessageChain tntgames(String player) throws IOException, URISyntaxException {
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

        if (!(json.get("player").isJsonObject() && json.get("player").getAsJsonObject().has("stats") && json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("TNTGames"))) {
            chain.append(new PlainText(" 该玩家的TNT游戏数据为空"));
            return chain.build();
        }
        JsonObject tntJson = json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("TNTGames").getAsJsonObject();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        chain.append(new PlainText(Rank.rank(json.get("player").getAsJsonObject()) + " "));
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
                        (float) tntJson.get("wins_tntag").getAsInt() /
                                tntJson.get("deaths_tntag").getAsInt())));
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
                    (float) tntJson.get("kills_tntag").getAsInt() /
                            tntJson.get("deaths_tntag").getAsInt())));
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
                    (float) tntJson.get("wins_tntrun").getAsInt() /
                            tntJson.get("deaths_tntrun").getAsInt())));
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
                        (float) tntJson.get("wins_pvprun").getAsInt() /
                                tntJson.get("deaths_pvprun").getAsInt())));
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
                    (float) tntJson.get("kills_pvprun").getAsInt() /
                            tntJson.get("deaths_pvprun").getAsInt())));
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
                    (float) tntJson.get("wins_bowspleef").getAsInt() /
                            tntJson.get("deaths_bowspleef").getAsInt())));
        }
        if (tntJson.has("tags_bowspleef")) {
            chain.append(new PlainText("\n  射箭数: "));
            chain.append(new PlainText(String.valueOf(tntJson.get("tags_bowspleef").getAsInt())));
        }


        chain.append("\nWizards: ");
        chain.append("\n  胜场: ");
        if (tntJson.has("wins_capture")) {
            chain.append(new PlainText(String.valueOf(tntJson.get("wins_capture").getAsInt())));
        } else chain.append("null");
        if (tntJson.has("points_capture")) {
            chain.append(" | 占点: ");
            chain.append(new PlainText(String.valueOf(tntJson.get("points_capture").getAsInt())));
        }
        chain.append("\n  击杀: ");
        if (tntJson.has("kills_capture")) {
            chain.append(new PlainText(String.valueOf(tntJson.get("kills_capture").getAsInt())));
        } else chain.append("null");
        chain.append(" | 死亡: ");
        if (tntJson.has("deaths_capture")) {
            chain.append(new PlainText(String.valueOf(tntJson.get("deaths_capture").getAsInt())));
        } else chain.append("null");
        if (tntJson.has("kills_capture") && tntJson.has("deaths_capture")) {
            chain.append(" | KDR: ");
            chain.append(new PlainText(decimalFormat.format(
                    (float) tntJson.get("kills_capture").getAsInt() /
                            tntJson.get("deaths_capture").getAsInt())));
        }
        if (tntJson.has("assists_capture")) {
            chain.append("\n  助攻: ");
            chain.append(new PlainText(String.valueOf(tntJson.get("assists_capture").getAsInt())));
            if (tntJson.has("deaths_capture")) {
                chain.append(" | (K+A)/D: ");
                if (tntJson.has("kills_capture")) {
                    chain.append(new PlainText(decimalFormat.format((float)
                            (tntJson.get("kills_capture").getAsInt() + tntJson.get("assists_capture").getAsInt()) /
                            tntJson.get("deaths_capture").getAsInt())));
                } else chain.append(new PlainText(decimalFormat.format((float)
                        tntJson.get("assists_capture").getAsInt() /
                        tntJson.get("deaths_capture").getAsInt())));
            }
        }

        return chain.build();
    }
}
