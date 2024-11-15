package moe.luoluo.hypixel;

import com.google.gson.JsonObject;
import moe.luoluo.Api;
import moe.luoluo.ApiResult;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
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

public class Arcade {
    public static void arc(CommandSender context, String player, String type) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();
        JsonObject acdJson;
        JsonObject achievements;

        ApiResult result;
        JsonObject json;
        String uuid = Api.mojang(player, "uuid");
        if (Objects.equals(uuid, "NotFound")) {
            context.sendMessage("玩家不存在");
            return;
        } else {
            result = Api.hypixel("player", uuid);
            json = result.getJson();
        }
        type = type.toLowerCase();

        if (result.getTime() != -1) {
            Instant instant = Instant.ofEpochMilli(result.getTime());
            LocalDateTime localDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            chain.append("\uD83D\uDFE5").append(localDate.toString()).append("\n");
        }

        if (!(json.get("player").isJsonObject() && json.get("player").getAsJsonObject().has("stats") && json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("Arcade"))) {
            chain.append(new PlainText("该玩家的街机游戏数据为空"));
            context.sendMessage(chain.build());
            return;
        }
        achievements = json.get("player").getAsJsonObject().get("achievements").getAsJsonObject();
        acdJson = json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("Arcade").getAsJsonObject();

        chain.append(new PlainText(Rank.rank(json.get("player").getAsJsonObject()))); //玩家名称前缀
        chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
        chain.append(new PlainText(" | 街机游戏数据："));

        //硬币
        chain.append(new PlainText("\n街机硬币: "));
        if (acdJson.has("coins")) {
            chain.append(new PlainText(largeNumFormat(acdJson.get("coins").getAsInt())));
        } else chain.append(new PlainText("null"));

        //总胜利数
        chain.append(new PlainText(" | 总胜利数: "));
        if (achievements.has("arcade_arcade_winner")) {
            chain.append(new PlainText(String.valueOf(achievements.get("arcade_arcade_winner").getAsInt())));
        } else chain.append(new PlainText("null"));

        if (type.isEmpty()) {
            chain.append("\n\n请在命令末尾加上type参数获取各游戏数据，或使用all获取所有游戏数据");
            chain.append("\n支持的参数: ");
            chain.append("dropper (dpr), party games (party), blocking dead (dayone), bounty hunters (bounty), creeper attack (creeper), dragon wars (dw), farm hunt (hunt), football (soccer), hide and seek (has), hole in the wall (hitw), hypixel says (says), mini walls(walls), pixel painters (paint), pixel party (pixel), throw out (throw_out), zombies (zb)");
        }

        //心跳水立方
        if (acdJson.has("dropper") && (type.equals("dropper") || type.equals("dpr") || type.equals("all"))) {
            chain.append(new PlainText("\n心跳水立方: "));
            JsonObject dropper = acdJson.get("dropper").getAsJsonObject();

            if (dropper.has("fastest_game")) {
                chain.append(new PlainText("\n| 最佳时间: "));
                double fg = dropper.get("fastest_game").getAsDouble() / 1000;
                chain.append(new PlainText(fg >= 60 ?
                        ((int) fg / 60) + ":" + String.format("%02d", (int) fg % 60) + ":" + String.format("%03d", Math.round((fg - (int) fg) * 1000)) :
                        String.format("%02d", (int) fg % 60) + ":" + String.format("%03d", Math.round((fg - (int) fg) * 1000))
                ));
            }

            chain.append(new PlainText("\n| 游玩次数: "));
            if (dropper.has("games_played")) {
                chain.append(new PlainText(String.valueOf(dropper.get("games_played").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 胜场: "));
            if (dropper.has("wins")) {
                chain.append(new PlainText(String.valueOf(dropper.get("wins").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 无暇胜利: "));
            if (dropper.has("flawless_games")) {
                chain.append(new PlainText(String.valueOf(dropper.get("flawless_games").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n| 完成游戏: "));
            if (dropper.has("games_finished")) {
                chain.append(new PlainText(String.valueOf(dropper.get("games_finished").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 完成地图: "));
            if (dropper.has("maps_completed")) {
                chain.append(new PlainText(String.valueOf(dropper.get("maps_completed").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 死亡数: "));
            if (dropper.has("fails")) {
                chain.append(new PlainText(String.valueOf(dropper.get("fails").getAsInt())));
            } else chain.append(new PlainText("null"));


        }

        //派对游戏
        if (type.equals("party games") || type.equals("party") || type.equals("all")) {
            chain.append(new PlainText("\n派对游戏: "));
            chain.append(new PlainText("\n| 胜场: "));
            if (acdJson.has("wins_party")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_party").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 总胜利回合数: "));
            if (acdJson.has("round_wins_party")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("round_wins_party").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 总计获得星星: "));
            if (acdJson.has("total_stars_party")) {
                chain.append(new PlainText(acdJson.get("total_stars_party").getAsInt() + "⭐"));
            } else chain.append(new PlainText("null"));
        }

        //行尸走肉
        if (type.equals("blocking dead") || type.equals("dayone") || type.equals("all")) {
            chain.append(new PlainText("\n行尸走肉: "));
            chain.append(new PlainText("\n| 胜场: "));
            if (acdJson.has("wins_dayone")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_dayone").getAsInt())));
            } else chain.append(new PlainText("null"));

            if (acdJson.has("kills_dayone") && acdJson.has("headshots_dayone")) {
                chain.append(new PlainText(" | 爆头率: "));
                chain.append(new PlainText(decimalFormat.format(
                        (float) acdJson.get("headshots_dayone").getAsInt() /
                                acdJson.get("kills_dayone").getAsInt()
                )));
            }

            chain.append(new PlainText("\n| 击杀: "));
            if (acdJson.has("kills_dayone")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("kills_dayone").getAsInt())));
            } else chain.append(new PlainText("null"));

            if (acdJson.has("headshots_dayone")) {
                chain.append(new PlainText(" | 爆头: "));
                chain.append(new PlainText(String.valueOf(acdJson.get("headshots_dayone").getAsInt())));
            }
        }

        //赏金猎人
        if (type.equals("bounty hunters") || type.equals("bounty") || type.equals("oneinthequiver") || type.equals("all")) {
            chain.append(new PlainText("\n赏金猎人: "));
            chain.append(new PlainText("\n| 胜场: "));
            if (acdJson.has("wins_oneinthequiver")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_oneinthequiver").getAsInt())));
            } else chain.append("null");
            if (acdJson.has("bounty_kills_oneinthequiver")) {
                chain.append(" | 目标击杀: ");
                chain.append(new PlainText(String.valueOf(acdJson.get("bounty_kills_oneinthequiver").getAsInt())));
            }
            chain.append("\n| 击杀: ");
            if (acdJson.has("kills_oneinthequiver")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("kills_oneinthequiver").getAsInt())));
            } else chain.append("null");
            chain.append(" | 死亡: ");
            if (acdJson.has("deaths_oneinthequiver")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("deaths_oneinthequiver").getAsInt())));
            } else chain.append("null");
            if (acdJson.has("kills_oneinthequiver") && acdJson.has("deaths_oneinthequiver")) {
                chain.append(" | KDR: ");
                chain.append(new PlainText(decimalFormat.format(
                        (float) acdJson.get("kills_oneinthequiver").getAsInt() /
                                acdJson.get("deaths_oneinthequiver").getAsInt())));
            }
            chain.append("\n| 弓箭击杀");
            if (acdJson.has("bow_kills_oneinthequiver")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("bow_kills_oneinthequiver").getAsInt())));
            } else chain.append("null");
            chain.append(" | 近战击杀:");
            if (acdJson.has("sword_kills_oneinthequiver")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("sword_kills_oneinthequiver").getAsInt())));
            } else chain.append("null");
        }

        //进击的苦力怕
        if (type.equals("creeper attack") || type.equals("creeper") || type.equals("all")) {
            chain.append(new PlainText("\n进击的苦力怕: "));
            chain.append(new PlainText("\n| 最大波次: "));
            if (acdJson.has("max_wave")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("max_wave").getAsInt())));
            } else chain.append(new PlainText("null"));
        }

        //龙之战
        if (type.equals("dragon wars") || type.equals("dw") || type.equals("all")) {
            chain.append(new PlainText("\n龙之战: "));
            chain.append(new PlainText("\n| 胜场: "));
            if (acdJson.has("wins_dragonwars2")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_dragonwars2").getAsInt())));
            } else chain.append("null");
            chain.append(new PlainText(" | 击杀: "));
            if (acdJson.has("kills_dragonwars2")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("kills_dragonwars2").getAsInt())));
            } else chain.append("null");
        }

        //末影掘战
                /*
                chain.append(new PlainText("\n末影掘战胜场: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 破坏方块数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 总计激活加成数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 激活大型射击加成数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 激活三重射击加成数: "));
                chain.append(new PlainText("null"));
                 */

        //农场躲猫猫
        if (type.equals("farm hunt") || type.equals("hunt") || type.equals("all")) {
            chain.append(new PlainText("\n农场躲猫猫: "));
            chain.append(new PlainText("\n| 胜场: "));
            if (acdJson.has("wins_farm_hunt")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_farm_hunt").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 作为动物胜场: "));
            if (acdJson.has("animal_wins_farm_hunt")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("animal_wins_farm_hunt").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 作为猎人胜场: "));
            if (acdJson.has("hunter_wins_farm_hunt")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("hunter_wins_farm_hunt").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n| 击杀: "));
            if (acdJson.has("kills_farm_hunt")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("kills_farm_hunt").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 作为动物击杀数: "));
            if (acdJson.has("animal_kills_farm_hunt")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("animal_kills_farm_hunt").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 作为猎人击杀: "));
            if (acdJson.has("hunter_kills_farm_hunt")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("hunter_kills_farm_hunt").getAsInt())));
            } else chain.append(new PlainText("null"));

                    /*
                    chain.append(new PlainText("\n| 弓箭击杀: "));
                    chain.append(new PlainText("null"));
                    chain.append(new PlainText(" | 作为动物弓箭击杀: "));
                    chain.append(new PlainText("null"));
                    chain.append(new PlainText(" | 作为猎人弓箭击杀: "));
                    chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n| 嘲讽使用次数: "));
                    chain.append(new PlainText("null"));
                    chain.append(new PlainText(" | 安全的嘲讽使用次数: "));
                    chain.append(new PlainText("null"));
                    chain.append(new PlainText(" | 较危险的嘲讽使用次数: "));
                    chain.append(new PlainText("null"));
                    chain.append(new PlainText(" | 危险的嘲讽使用次数: "));
                    chain.append(new PlainText("null"));
                    chain.append(new PlainText(" | 烟花嘲讽使用次数: "));
                    chain.append(new PlainText("null"));
                    */

            chain.append(new PlainText("\n| 便便收集数: "));
            if (acdJson.has("poop_collected_farm_hunt")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("poop_collected_farm_hunt").getAsInt())));
            } else chain.append(new PlainText("null"));
        }

        //足球
        if (type.equals("football") || type.equals("soccer") || type.equals("all")) {
            chain.append(new PlainText("\n足球: "));
            chain.append(new PlainText("\n| 胜场: "));
            if (acdJson.has("wins_soccer")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_soccer").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n| 得分: "));
            if (acdJson.has("goals_soccer")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("goals_soccer").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n| 踢出数: "));
            if (acdJson.has("powerkicks_soccer")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("powerkicks_soccer").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 传球数: "));
            if (acdJson.has("kicks_soccer")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("kicks_soccer").getAsInt())));
            } else chain.append(new PlainText("null"));
        }

        //星际战争
                /* chain.append(new PlainText("\n星际战争胜场:  "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 击杀: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 死亡数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 帝国军击杀: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 反抗军击杀: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 射击次数: "));
                chain.append(new PlainText("null"));
                 */

        //躲猫猫
        if (type.equals("hide and seek") || type.equals("has") || type.equals("all")) {
            chain.append(new PlainText("\n躲猫猫: "));
            chain.append(new PlainText("\n| 躲藏者胜场: "));
            if (acdJson.has("hider_wins_hide_and_seek")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("hider_wins_hide_and_seek").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 寻找者胜场: "));
            if (acdJson.has("seeker_wins_hide_and_seek")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("seeker_wins_hide_and_seek").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append("\n|- 道具躲猫猫");
            chain.append(new PlainText("\n| 躲藏者胜场: "));
            if (acdJson.has("prop_hunt_hider_wins_hide_and_seek")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("prop_hunt_hider_wins_hide_and_seek").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 寻找者胜场: "));
            if (acdJson.has("prop_hunt_seeker_wins_hide_and_seek")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("prop_hunt_seeker_wins_hide_and_seek").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n|- 派对躲猫猫: "));
            chain.append(new PlainText("\n| 躲藏者胜场: "));
            if (acdJson.has("party_pooper_hider_wins_hide_and_seek")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("party_pooper_hider_wins_hide_and_seek").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 寻找者胜场: "));
            if (acdJson.has("party_pooper_seeker_wins_hide_and_seek")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("party_pooper_seeker_wins_hide_and_seek").getAsInt())));
            } else chain.append(new PlainText("null"));
        }

        //人体打印机
        if (type.equals("hole in the wall") || type.equals("hitw") || type.equals("all")) {
            chain.append(new PlainText("\n人体打印机: "));
            chain.append(new PlainText("\n| 胜场: "));
            if (acdJson.has("wins_hole_in_the_wall")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_hole_in_the_wall").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 游玩回合数: "));
            if (acdJson.has("rounds_hole_in_the_wall")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("rounds_hole_in_the_wall").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n| 最佳资格赛得分: "));
            if (acdJson.has("hitw_record_q")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("hitw_record_q").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 最佳决赛得分: "));
            if (acdJson.has("hitw_record_f")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("hitw_record_f").getAsInt())));
            } else chain.append(new PlainText("null"));
        }

        //我说你做
        if (type.equals("hypixel says") || type.equals("hypixel say") || type.equals("says") || type.equals("all")) {
            chain.append(new PlainText("\n我说你做: "));
            chain.append(new PlainText("\n| 我说你做胜场: "));
            if (acdJson.has("wins_simon_says")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_simon_says").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n| 我说你做分数: "));
            if (acdJson.has("rounds_simon_says")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("rounds_simon_says").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 最高分: "));
            if (acdJson.has("top_score_simon_says")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("top_score_simon_says").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n| 我说你做回合胜利数: "));
            if (acdJson.has("round_wins_simon_says")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("round_wins_simon_says").getAsInt())));
            } else chain.append(new PlainText("null"));
        }

        //迷你战墙
        if (type.equals("mini walls") || type.equals("walls") || type.equals("all")) {
            chain.append(new PlainText("\n迷你战墙: "));
            chain.append(new PlainText("\n| 胜场: "));
            if (acdJson.has("wins_mini_walls")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_mini_walls").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n| 最终击杀: "));
            if (acdJson.has("final_kills_mini_walls")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("final_kills_mini_walls").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 击杀: "));
            if (acdJson.has("kills_mini_walls")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("kills_mini_walls").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 死亡数: "));
            if (acdJson.has("deaths_mini_walls")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("deaths_mini_walls").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | KD: "));
            if (acdJson.has("kills_mini_walls") && acdJson.has("deaths_mini_walls")) {
                chain.append(new PlainText(decimalFormat.format(
                        (float) acdJson.get("kills_mini_walls").getAsInt() /
                                acdJson.get("deaths_mini_walls").getAsInt()
                )));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n| 箭矢命中数: "));
            if (acdJson.has("arrows_hit_mini_walls")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("arrows_hit_mini_walls").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 箭矢射击数: "));
            if (acdJson.has("arrows_shot_mini_walls")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("arrows_shot_mini_walls").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 命中率: "));
            if (acdJson.has("arrows_hit_mini_walls") && acdJson.has("arrows_shot_mini_walls")) {
                chain.append(new PlainText(decimalFormat.format(
                        (float) acdJson.get("arrows_hit_mini_walls").getAsInt() /
                                acdJson.get("arrows_shot_mini_walls").getAsInt()
                )));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText("\n| 凋零击杀: "));
            if (acdJson.has("wither_kills_mini_walls")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wither_kills_mini_walls").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 凋零伤害量: "));
            if (acdJson.has("wither_damage_mini_walls")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wither_damage_mini_walls").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 平均伤害: "));
            if (acdJson.has("wither_kills_mini_walls") && acdJson.has("wither_damage_mini_walls")) {
                chain.append(new PlainText(decimalFormat.format(
                        (float) acdJson.get("wither_damage_mini_walls").getAsInt() /
                                acdJson.get("wither_kills_mini_walls").getAsInt()
                )));
            }
        }


        //像素画家
        if (type.equals("pixel painters") || type.equals("paint") || type.equals("all")) {
            chain.append(new PlainText("\n像素画家: "));
            chain.append(new PlainText("\n| 胜场: "));
            if (acdJson.has("wins_pixel_painters")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_pixel_painters").getAsInt())));
            } else chain.append(new PlainText("null"));
        }

        //Pixel party
        if (type.equals("pixel party") || type.equals("pixel") || type.equals("all")) {
            chain.append(new PlainText("\n像素派对: "));
            if (acdJson.has("pixel_party")) {
                JsonObject pixel_party = acdJson.get("pixel_party").getAsJsonObject();

                chain.append(new PlainText("\n| 游戏场次: "));
                if (pixel_party.has("games_played")) {
                    chain.append(new PlainText(String.valueOf(pixel_party.get("games_played").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 完成回合数: "));
                if (pixel_party.has("rounds_completed")) {
                    chain.append(new PlainText(String.valueOf(pixel_party.get("rounds_completed").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n| 胜场: "));
                if (pixel_party.has("wins")) {
                    chain.append(new PlainText(String.valueOf(pixel_party.get("wins").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n| 经典胜场: "));
                if (pixel_party.has("wins_normal")) {
                    chain.append(new PlainText(String.valueOf(pixel_party.get("wins_normal").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 完成回合: "));
                if (pixel_party.has("rounds_completed_normal")) {
                    chain.append(new PlainText(String.valueOf(pixel_party.get("rounds_completed_normal").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n| 狂热胜场: "));
                if (pixel_party.has("wins_hyper")) {
                    chain.append(new PlainText(String.valueOf(pixel_party.get("wins_hyper").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 完成回合: "));
                if (pixel_party.has("rounds_completed_hyper")) {
                    chain.append(new PlainText(String.valueOf(pixel_party.get("rounds_completed_hyper").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n| 收集加成: "));
                if (pixel_party.has("power_ups_collected") && pixel_party.has("power_ups_collected_normal") && pixel_party.has("power_ups_collected_hyper")) {
                    chain.append(new PlainText(String.valueOf(
                            pixel_party.get("power_ups_collected").getAsInt()
                                    + pixel_party.get("power_ups_collected_normal").getAsInt()
                                    + pixel_party.get("power_ups_collected_hyper").getAsInt())));

                } else if (pixel_party.has("power_ups_collected") && pixel_party.has("power_ups_collected_normal")) {
                    chain.append(new PlainText(String.valueOf(
                            pixel_party.get("power_ups_collected").getAsInt()
                                    + pixel_party.get("power_ups_collected_normal").getAsInt())));

                } else if (pixel_party.has("power_ups_collected") && pixel_party.has("power_ups_collected_hyper")) {
                    chain.append(new PlainText(String.valueOf(
                            pixel_party.get("power_ups_collected").getAsInt()
                                    + pixel_party.get("power_ups_collected_hyper").getAsInt())));
                } else if (pixel_party.has("power_ups_collected_normal") && pixel_party.has("power_ups_collected_hyper")) {
                    chain.append(new PlainText(String.valueOf(
                            pixel_party.get("power_ups_collected_normal").getAsInt()
                                    + pixel_party.get("power_ups_collected_hyper").getAsInt())));
                } else chain.append(new PlainText("null"));
            } else chain.append(new PlainText("无数据"));
        }


        //乱棍之战
        if (type.equals("throw out") || type.equals("throw_out") || type.equals("all")) {
            chain.append(new PlainText("\n乱棍之战: "));
            chain.append(new PlainText("\n| 胜场: "));
            if (acdJson.has("wins_throw_out")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_throw_out").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 击杀: "));
            if (acdJson.has("kills_throw_out")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("kills_throw_out").getAsInt())));
            } else chain.append(new PlainText("null"));
        }


        //僵尸末日
        if (type.equals("zombies") || type.equals("zb") || type.equals("all")) {
            chain.append(new PlainText("\n僵尸末日"));

            chain.append(new PlainText("\n| 生存回合: "));
            if (acdJson.has("total_rounds_survived_zombies")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 胜场: "));
            if (acdJson.has("wins_zombies")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies").getAsInt())));
            } else chain.append("无");

            if (acdJson.has("best_round_zombies")) {
                chain.append(new PlainText("\n| 最佳回合: "));
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies").getAsInt())));
            }


            chain.append(new PlainText("\n| 最佳时间: "));
            if (acdJson.has("fastest_time_10_zombies")) {
                int t = acdJson.get("fastest_time_10_zombies").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            } else chain.append(new PlainText("null"));
            if (acdJson.has("fastest_time_20_zombies")) {
                chain.append(new PlainText(" | "));
                int t = acdJson.get("fastest_time_20_zombies").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            }
            if (acdJson.has("fastest_time_30_zombies")) {
                chain.append(new PlainText(" | "));
                int t = acdJson.get("fastest_time_30_zombies").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            }

            //僵尸末日穷途末路
            chain.append(new PlainText("\n|- 穷途末路: "));

            chain.append(new PlainText("\n| 生存回合: "));
            if (acdJson.has("total_rounds_survived_zombies_deadend")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_deadend").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 胜场: "));
            if (acdJson.has("wins_zombies_deadend")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_deadend").getAsInt())));
            } else chain.append("无");

            chain.append(new PlainText("\n| 最佳回合: "));
            if (acdJson.has("best_round_zombies_deadend_normal")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_deadend_normal").getAsInt())));
            } else chain.append("-");
            chain.append(" | ");
            if (acdJson.has("best_round_zombies_deadend_hard")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_deadend_hard").getAsInt())));
            } else chain.append("-");
            chain.append(" | ");
            if (acdJson.has("best_round_zombies_deadend_rip")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_deadend_rip").getAsInt())));
            } else chain.append("-");

            chain.append(new PlainText("\n| 最佳时间: "));
            if (acdJson.has("fastest_time_10_zombies_deadend_normal")) {
                int t = acdJson.get("fastest_time_10_zombies_deadend_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            } else chain.append(new PlainText("null"));
            if (acdJson.has("fastest_time_20_zombies_deadend_normal")) {
                chain.append(new PlainText(" | "));
                int t = acdJson.get("fastest_time_20_zombies_deadend_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            }
            if (acdJson.has("fastest_time_30_zombies_deadend_normal")) {
                chain.append(new PlainText(" | "));
                int t = acdJson.get("fastest_time_30_zombies_deadend_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            }

            //僵尸末日坏血之宫
            chain.append(new PlainText("\n|- 坏血之宫: "));

            chain.append(new PlainText("\n| 生存回合: "));
            if (acdJson.has("total_rounds_survived_zombies_badblood")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_badblood").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 胜场: "));
            if (acdJson.has("wins_zombies_badblood")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_badblood").getAsInt())));
            } else chain.append("无");

            chain.append(new PlainText("\n| 最佳回合: "));
            if (acdJson.has("best_round_zombies_badblood_normal")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_badblood_normal").getAsInt())));
            } else chain.append("-");
            chain.append(" | ");
            if (acdJson.has("best_round_zombies_badblood_hard")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_badblood_hard").getAsInt())));
            } else chain.append("-");
            chain.append(" | ");
            if (acdJson.has("best_round_zombies_badblood_rip")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_badblood_rip").getAsInt())));
            } else chain.append("-");

            chain.append(new PlainText("\n| 最佳时间: "));
            if (acdJson.has("fastest_time_10_zombies_badblood_normal")) {
                int t = acdJson.get("fastest_time_10_zombies_badblood_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            } else chain.append(new PlainText("null"));
            if (acdJson.has("fastest_time_20_zombies_badblood_normal")) {
                chain.append(new PlainText(" | "));
                int t = acdJson.get("fastest_time_20_zombies_badblood_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            }
            if (acdJson.has("fastest_time_30_zombies_badblood_normal")) {
                chain.append(new PlainText(" | "));
                int t = acdJson.get("fastest_time_30_zombies_badblood_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            }


            //僵尸末日外星游乐园
            chain.append(new PlainText("\n|- 外星游乐园: "));

            chain.append(new PlainText("\n| 生存回合: "));
            if (acdJson.has("total_rounds_survived_zombies_alienarcadium")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_alienarcadium").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 胜场: "));
            if (acdJson.has("wins_zombies_alienarcadium")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_alienarcadium").getAsInt())));
            } else chain.append("无");

            chain.append(new PlainText("\n| 最佳回合: "));
            if (acdJson.has("best_round_zombies_alienarcadium_normal")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_alienarcadium_normal").getAsInt())));
            } else chain.append("-");
            chain.append(" | ");
            if (acdJson.has("best_round_zombies_alienarcadium_hard")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_alienarcadium_hard").getAsInt())));
            } else chain.append("-");
            chain.append(" | ");
            if (acdJson.has("best_round_zombies_alienarcadium_rip")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_alienarcadium_rip").getAsInt())));
            } else chain.append("-");

            chain.append(new PlainText("\n| 最佳时间: "));
            if (acdJson.has("fastest_time_10_zombies_alienarcadium_normal")) {
                int t = acdJson.get("fastest_time_10_zombies_alienarcadium_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            } else chain.append(new PlainText("null"));
            if (acdJson.has("fastest_time_20_zombies_alienarcadium_normal")) {
                chain.append(new PlainText(" | "));
                int t = acdJson.get("fastest_time_20_zombies_alienarcadium_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            }
            if (acdJson.has("fastest_time_30_zombies_alienarcadium_normal")) {
                chain.append(new PlainText(" | "));
                int t = acdJson.get("fastest_time_30_zombies_alienarcadium_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            }

            chain.append(new PlainText("\n|- 监狱风云: "));

            chain.append(new PlainText("\n| 总生存回合: "));
            if (acdJson.has("total_rounds_survived_zombies_prison")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_prison").getAsInt())));
            } else chain.append(new PlainText("null"));

            chain.append(new PlainText(" | 胜场: "));
            if (acdJson.has("wins_zombies_prison")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_prison").getAsInt())));
            } else chain.append("无");

            chain.append(new PlainText("\n| 最佳回合: "));
            if (acdJson.has("best_round_zombies_prison_normal")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_prison_normal").getAsInt())));
            } else chain.append("-");
            chain.append(" | ");
            if (acdJson.has("best_round_zombies_prison_hard")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_prison_hard").getAsInt())));
            } else chain.append("-");
            chain.append(" | ");
            if (acdJson.has("best_round_zombies_prison_rip")) {
                chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_prison_rip").getAsInt())));
            } else chain.append("-");

            chain.append(new PlainText("\n| 最佳时间: "));
            if (acdJson.has("fastest_time_10_zombies_prison_normal")) {
                int t = acdJson.get("fastest_time_10_zombies_prison_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            } else chain.append(new PlainText("null"));
            if (acdJson.has("fastest_time_20_zombies_prison_normal")) {
                chain.append(new PlainText(" | "));
                int t = acdJson.get("fastest_time_20_zombies_prison_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            }
            if (acdJson.has("fastest_time_30_zombies_prison_normal")) {
                chain.append(new PlainText(" | "));
                int t = acdJson.get("fastest_time_30_zombies_prison_normal").getAsInt();
                chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
            }
        }


        if (context.getSubject() != null) {
            if (type.equals("all")) {
                ForwardMessageBuilder builder = new ForwardMessageBuilder(context.getSubject());
                builder.add(Objects.requireNonNull(context.getBot()).getId(), context.getBot().getNick(), chain.build());
                context.sendMessage(builder.build());
            } else {
                context.sendMessage(chain.build());
            }
        } else {
            context.sendMessage(chain.build());
        }
    }
}
