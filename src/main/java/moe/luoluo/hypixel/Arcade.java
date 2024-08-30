package moe.luoluo.hypixel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import moe.luoluo.Api;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Objects;

public class Arcade {
    public static void arc(CommandSender context, String player, String type) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();
        JsonObject playerJson;
        JsonObject acdJson;
        JsonObject achievements;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        JsonObject json = Api.hypixel("player", Api.mojang(player, "uuid"));

        if (json.get("player").isJsonObject()) {
            playerJson = json.get("player").getAsJsonObject();
            achievements = playerJson.get("achievements").getAsJsonObject();

            if (playerJson.has("stats") && playerJson.get("stats").getAsJsonObject().has("Arcade")) {
                acdJson = playerJson.get("stats").getAsJsonObject().get("Arcade").getAsJsonObject();

                chain.append(new PlainText(Rank.rank(playerJson))); //玩家名称前缀
                chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
                chain.append(new PlainText(" | 街机游戏 数据如下："));

                //硬币
                chain.append(new PlainText("\n街机硬币: "));
                if (acdJson.has("coins")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("coins").getAsInt())));
                } else chain.append(new PlainText("null"));

                //总胜利数
                chain.append(new PlainText(" | 总胜利数: "));
                if (achievements.has("arcade_arcade_winner")) {
                    chain.append(new PlainText(String.valueOf(achievements.get("arcade_arcade_winner").getAsInt())));
                } else chain.append(new PlainText("null"));

                //心跳水立方
                if (acdJson.has("dropper")) {
                    chain.append(new PlainText("\n心跳水立方: "));
                    JsonObject dropper = acdJson.get("dropper").getAsJsonObject();

                    chain.append(new PlainText("\n    最佳时间: "));
                    if (dropper.has("fastest_game")) {
                        double fg = dropper.get("fastest_game").getAsDouble() / 1000;
                        chain.append(new PlainText(fg >= 60 ?
                                ((int) fg / 60) + ":" + String.format("%02d", (int) fg % 60) + ":" + String.format("%03d", Math.round((fg - (int) fg) * 1000)) :
                                String.format("%02d", (int) fg % 60) + ":" + String.format("%03d", Math.round((fg - (int) fg) * 1000))
                        ));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    游玩次数: "));
                    if (dropper.has("game_played")) {
                        chain.append(new PlainText(String.valueOf(dropper.get("games_played").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 胜场: "));
                    if (dropper.has("wins_dropper")) {
                        chain.append(new PlainText(String.valueOf(dropper.get("wins").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 无暇胜利: "));
                    if (dropper.has("flawless_games")) {
                        chain.append(new PlainText(String.valueOf(dropper.get("flawless_games").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    完成游戏: "));
                    if (dropper.has("games_finished")) {
                        chain.append(new PlainText(String.valueOf(dropper.get("games_finished").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 完成地图: "));
                    if (dropper.has("maps_completed")) {
                        chain.append(new PlainText(String.valueOf(dropper.get("maps_completed").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 死亡数: "));
                    if (dropper.has("fails_dropper")) {
                        chain.append(new PlainText(String.valueOf(dropper.get("fails").getAsInt())));
                    } else chain.append(new PlainText("null"));


                }

                //派对游戏
                chain.append(new PlainText("\n派对游戏: "));
                chain.append(new PlainText("\n    胜场: "));
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

                //行尸走肉
                chain.append(new PlainText("\n行尸走肉: "));
                chain.append(new PlainText("\n    胜场: "));
                if (acdJson.has("wins_dayone")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_dayone").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 击杀: "));
                if (acdJson.has("kills_dayone")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("kills_dayone").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 爆头数: "));
                if (acdJson.has("headshots_dayone")) {
                    chain.append(new PlainText(acdJson.get("headshots_dayone").getAsInt() + "💀"));
                } else chain.append(new PlainText("null"));

                //赏金猎人
                /*
                chain.append(new PlainText("\n赏金猎人: "));
                chain.append(new PlainText("null"));

                 */

                //Capture The Wool
                /*
                chain.append(new PlainText("\n捕捉羊毛: "));
                chain.append(new PlainText("null"));

                 */

                //进击的苦力怕
                chain.append(new PlainText("\n进击的苦力怕: "));
                chain.append(new PlainText("\n    最大波次: "));
                if (acdJson.has("crreper_Attack")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("max_wave").getAsInt())));
                } else chain.append(new PlainText("null"));

                //龙之战
                /*
                chain.append(new PlainText("\n龙之战胜场: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 击杀: "));
                chain.append(new PlainText("null"));
                 */

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
                chain.append(new PlainText("\n农场躲猫猫: "));
                chain.append(new PlainText("\n    胜场: "));
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

                chain.append(new PlainText("\n    击杀: "));
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
                chain.append(new PlainText("\n    弓箭击杀: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 作为动物弓箭击杀: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 作为猎人弓箭击杀: "));
                chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    嘲讽使用次数: "));
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

                chain.append(new PlainText("\n    便便收集数: "));
                if (acdJson.has("poop_collected_farm_hunt")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("poop_collected_farm_hunt").getAsInt())));
                } else chain.append(new PlainText("null"));

                //足球
                chain.append(new PlainText("\n足球: "));
                chain.append(new PlainText("\n    胜场: "));
                if (acdJson.has("wins_soccer")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_soccer").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    得分: "));
                if (acdJson.has("goals_soccer")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("goals_soccer").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    踢出数: "));
                if (acdJson.has("powerkicks_soccer")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("powerkicks_soccer").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 传球数: "));
                if (acdJson.has("kicks_soccer")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("kicks_soccer").getAsInt())));
                } else chain.append(new PlainText("null"));

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
                chain.append(new PlainText("\n道具躲猫猫: "));
                chain.append(new PlainText("\n    躲藏者胜场: "));
                if (acdJson.has("hider_wins_hide_and_seek")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("hider_wins_hide_and_seek").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 寻找者胜场: "));
                if (acdJson.has("seeker_wins_hide_and_seek")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("seeker_wins_hide_and_seek").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n派对躲猫猫: "));
                chain.append(new PlainText("\n    躲藏者胜场: "));
                if (acdJson.has("party_pooper_hider_wins_hide_and_seek")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("party_pooper_hider_wins_hide_and_seek").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 寻找者胜场: "));
                if (acdJson.has("party_pooper_seeker_wins_hide_and_seek")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("party_pooper_seeker_wins_hide_and_seek").getAsInt())));
                } else chain.append(new PlainText("null"));

                //人体打印机
                chain.append(new PlainText("\n人体打印机: "));
                chain.append(new PlainText("\n    胜场: "));
                if (acdJson.has("wins_hole_in_the_wall")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_hole_in_the_wall").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 游玩回合数: "));
                if (acdJson.has("rounds_hole_in_the_wall")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("rounds_hole_in_the_wall").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    最佳资格赛得分: "));
                if (acdJson.has("hitw_record_q")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("hitw_record_q").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 最佳决赛得分: "));
                if (acdJson.has("hitw_record_f")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("hitw_record_f").getAsInt())));
                } else chain.append(new PlainText("null"));


                //我说你做
                chain.append(new PlainText("\n我说你做: "));
                chain.append(new PlainText("\n    我说你做胜场: "));
                if (acdJson.has("wins_simon_says")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_simon_says").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    我说你做分数: "));
                if (acdJson.has("rounds_simon_says")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("rounds_simon_says").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 最高分: "));
                if (acdJson.has("top_score_simon_says")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("top_score_simon_says").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    我说你做回合胜利数: "));
                if (acdJson.has("round_wins_simon_says")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("round_wins_simon_says").getAsInt())));
                } else chain.append(new PlainText("null"));

                //迷你战墙
                chain.append(new PlainText("\n迷你战墙: "));
                chain.append(new PlainText("\n    胜场: "));
                if (acdJson.has("wins_mini_walls")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_mini_walls").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    最终击杀: "));
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
                                    (float) acdJson.get("deaths_mini_walls").getAsInt()
                    )));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    箭矢命中数: "));
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
                                    (float) acdJson.get("arrows_shot_mini_walls").getAsInt()
                    )));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    凋零击杀: "));
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
                                    (float) acdJson.get("wither_kills_mini_walls").getAsInt()
                    )));
                }


                //像素画家
                chain.append(new PlainText("\n像素画家: "));
                chain.append(new PlainText("\n    胜场: "));
                if (acdJson.has("wins_pixel_painters")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_pixel_painters").getAsInt())));
                } else chain.append(new PlainText("null"));


                //Pixel party
                chain.append(new PlainText("\n像素派对: "));
                if (acdJson.has("pixel_party")) {
                    JsonObject pixel_party = acdJson.get("pixel_party").getAsJsonObject();

                    chain.append(new PlainText("\n    游戏场次: "));
                    if (pixel_party.has("games_played")) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("games_played").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 完成回合数: "));
                    if (pixel_party.has("rounds_completed")) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("rounds_completed").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    胜场: "));
                    if (pixel_party.has("wins_pixel_party")) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("wins").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    经典模式胜场: "));
                    if (pixel_party.has("wins_normal")) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("wins_normal").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 经典模式回合完成数: "));
                    if (pixel_party.has("rounds_completed_normal")) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("rounds_completed_normal").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    狂热模式胜场: "));
                    if (pixel_party.has("wins_hyper")) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("wins_hyper").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 狂热模式回合完成数: "));
                    if (pixel_party.has("rounds_completed_hyper")) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("rounds_completed_hyper").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    加成收集数: "));
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


                } else chain.append(new PlainText("null"));


                //乱棍之战
                chain.append(new PlainText("\n乱棍之战: "));
                chain.append(new PlainText("\n    胜场: "));
                if (acdJson.has("wins_throw_out")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_throw_out").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 击杀: "));
                if (acdJson.has("kills_throw_out")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("kills_throw_out").getAsInt())));
                } else chain.append(new PlainText("null"));


                //僵尸末日
                chain.append(new PlainText("\n僵尸末日"));

                chain.append(new PlainText("\n    总生存回合: "));
                if (acdJson.has("total_rounds_survived_zombies")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("best_round_zombies")) {
                    chain.append(new PlainText(" | 最佳回合: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies").getAsInt())));
                }

                if (acdJson.has("wins_zombies")) {
                    chain.append(new PlainText(" | 胜场: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies").getAsInt())));
                }

                chain.append(new PlainText("\n  最速记录 10回合: "));
                if (acdJson.has("fastest_time_10_zombies")) {
                    int t = acdJson.get("fastest_time_10_zombies").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                } else chain.append(new PlainText("null"));
                if (acdJson.has("fastest_time_20_zombies")) {
                    chain.append(new PlainText(" | 20回合: "));
                    int t = acdJson.get("fastest_time_20_zombies").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }
                if (acdJson.has("fastest_time_30_zombies")) {
                    chain.append(new PlainText(" | 30回合: "));
                    int t = acdJson.get("fastest_time_30_zombies").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                chain.append(new PlainText("\n    击杀僵尸: "));
                if (acdJson.has("zombie_kills_zombies")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("zombie_kills_zombies").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("players_revived_zombies")) {
                    chain.append(new PlainText(" | 救援玩家: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("players_revived_zombies").getAsInt())));
                }

                chain.append(new PlainText("\n    被击倒: "));
                if (acdJson.has("times_knocked_down_zombies")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("times_knocked_down_zombies").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("deaths_zombies")) {
                    chain.append(new PlainText(" | 死亡: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("deaths_zombies").getAsInt())));
                }

                chain.append(new PlainText("\n    修复窗户: "));
                if (acdJson.has("windows_repaired_zombies")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("windows_repaired_zombies").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("doors_opened_zombies")) {
                    chain.append(new PlainText(" | 开门: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("doors_opened_zombies").getAsInt())));
                }

                //僵尸末日穷途末路
                chain.append(new PlainText("\n\n穷途末路: "));

                chain.append(new PlainText("\n    总生存回合: "));
                if (acdJson.has("total_rounds_survived_zombies_deadend")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_deadend").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("best_round_zombies_deadend")) {
                    chain.append(new PlainText(" | 最佳回合: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_deadend").getAsInt())));
                }

                if (acdJson.has("wins_zombies_deadend")) {
                    chain.append(new PlainText(" | 胜场: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_deadend").getAsInt())));
                }

                chain.append(new PlainText("\n  普通最速 10回合: "));
                if (acdJson.has("fastest_time_10_zombies_deadend_normal")) {
                    int t = acdJson.get("fastest_time_10_zombies_deadend_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                } else chain.append(new PlainText("null"));
                if (acdJson.has("fastest_time_20_zombies_deadend_normal")) {
                    chain.append(new PlainText(" | 20回合: "));
                    int t = acdJson.get("fastest_time_20_zombies_deadend_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }
                if (acdJson.has("fastest_time_30_zombies_deadend_normal")) {
                    chain.append(new PlainText(" | 30回合: "));
                    int t = acdJson.get("fastest_time_30_zombies_deadend_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                chain.append(new PlainText("\n    击杀僵尸: "));
                if (acdJson.has("zombie_kills_zombies_deadend")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("zombie_kills_zombies_deadend").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("players_revived_zombies_deadend")) {
                    chain.append(new PlainText(" | 救援玩家: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("players_revived_zombies_deadend").getAsInt())));
                }

                chain.append(new PlainText("\n    被击倒: "));
                if (acdJson.has("times_knocked_down_zombies_deadend")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("times_knocked_down_zombies_deadend").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("deaths_zombies_deadend")) {
                    chain.append(new PlainText(" | 死亡: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("deaths_zombies_deadend").getAsInt())));
                }

                chain.append(new PlainText("\n    修复窗户: "));
                if (acdJson.has("windows_repaired_zombies_deadend")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("windows_repaired_zombies_deadend").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 开门: "));
                if (acdJson.has("doors_opened_zombies_deadend")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("doors_opened_zombies_deadend").getAsInt())));
                } else chain.append(new PlainText("null"));


                //僵尸末日坏血之宫
                chain.append(new PlainText("\n\n坏血之宫: "));

                chain.append(new PlainText("\n    总生存回合: "));
                if (acdJson.has("total_rounds_survived_zombies_badblood")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_badblood").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("best_round_zombies_badblood")) {
                    chain.append(new PlainText(" | 最佳回合: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_badblood").getAsInt())));
                }

                if (acdJson.has("wins_zombies_badblood")) {
                    chain.append(new PlainText(" | 胜场: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_badblood").getAsInt())));
                }

                chain.append(new PlainText("\n  普通最速 10回合: "));
                if (acdJson.has("fastest_time_10_zombies_badblood_normal")) {
                    int t = acdJson.get("fastest_time_10_zombies_badblood_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                } else chain.append(new PlainText("null"));
                if (acdJson.has("fastest_time_20_zombies_badblood_normal")) {
                    chain.append(new PlainText(" | 20回合: "));
                    int t = acdJson.get("fastest_time_20_zombies_badblood_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }
                if (acdJson.has("fastest_time_30_zombies_badblood_normal")) {
                    chain.append(new PlainText(" | 30回合: "));
                    int t = acdJson.get("fastest_time_30_zombies_badblood_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                chain.append(new PlainText("\n    击杀僵尸: "));
                if (acdJson.has("zombie_kills_zombies_badblood")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("zombie_kills_zombies_badblood").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("players_revived_zombies_badblood")) {
                    chain.append(new PlainText(" | 救援玩家: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("players_revived_zombies_badblood").getAsInt())));
                }

                chain.append(new PlainText("\n    被击倒: "));
                if (acdJson.has("times_knocked_down_zombies_badblood")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("times_knocked_down_zombies_badblood").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("deaths_zombies_badblood")) {
                    chain.append(new PlainText(" | 死亡: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("deaths_zombies_badblood").getAsInt())));
                }

                chain.append(new PlainText("\n    修复窗户: "));
                if (acdJson.has("windows_repaired_zombies_badblood")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("windows_repaired_zombies_badblood").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("doors_opened_zombies_badblood")) {
                    chain.append(new PlainText(" | 开门: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("doors_opened_zombies_badblood").getAsInt())));
                }


                //僵尸末日外星游乐园
                chain.append(new PlainText("\n\n外星游乐园: "));

                chain.append(new PlainText("\n    总生存回合: "));
                if (acdJson.has("total_rounds_survived_zombies_alienarcadium")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_alienarcadium").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("best_round_zombies_alienarcadium")) {
                    chain.append(new PlainText(" | 最佳回合: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_alienarcadium").getAsInt())));
                }

                if (acdJson.has("wins_zombies_alienarcadium")) {
                    chain.append(new PlainText(" | 胜场: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_alienarcadium").getAsInt())));
                }

                chain.append(new PlainText("\n  普通最速 10回合: "));
                if (acdJson.has("fastest_time_10_zombies_alienarcadium_normal")) {
                    int t = acdJson.get("fastest_time_10_zombies_alienarcadium_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                } else chain.append(new PlainText("null"));
                if (acdJson.has("fastest_time_20_zombies_alienarcadium_normal")) {
                    chain.append(new PlainText(" | 20回合: "));
                    int t = acdJson.get("fastest_time_20_zombies_alienarcadium_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }
                if (acdJson.has("fastest_time_30_zombies_alienarcadium_normal")) {
                    chain.append(new PlainText(" | 30回合: "));
                    int t = acdJson.get("fastest_time_30_zombies_alienarcadium_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                chain.append(new PlainText("\n    击杀僵尸: "));
                if (acdJson.has("zombie_kills_zombies_alienarcadium")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("zombie_kills_zombies_alienarcadium").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("players_revived_zombies_alienarcadium")) {
                    chain.append(new PlainText(" | 救援玩家: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("players_revived_zombies_alienarcadium").getAsInt())));
                }

                chain.append(new PlainText("\n    被击倒: "));
                if (acdJson.has("times_knocked_down_zombies_alienarcadium")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("times_knocked_down_zombies_alienarcadium").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("deaths_zombies_alienarcadium")) {
                    chain.append(new PlainText(" | 死亡: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("deaths_zombies_alienarcadium").getAsInt())));
                }

                chain.append(new PlainText("\n    修复窗户: "));
                if (acdJson.has("windows_repaired_zombies_alienarcadium")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("windows_repaired_zombies_alienarcadium").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("doors_opened_zombies_alienarcadium")) {
                    chain.append(new PlainText(" | 开门: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("doors_opened_zombies_alienarcadium").getAsInt())));
                }

                chain.append(new PlainText("\n\n监狱(译名未定): "));

                chain.append(new PlainText("\n    总生存回合: "));
                if (acdJson.has("total_rounds_survived_zombies_prison")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_prison").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("best_round_zombies_prison")) {
                    chain.append(new PlainText(" | 最佳回合: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_prison").getAsInt())));
                }

                if (acdJson.has("wins_zombies_prison")) {
                    chain.append(new PlainText(" | 胜场: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_prison").getAsInt())));
                }

                chain.append(new PlainText("\n  普通最速 10回合: "));
                if (acdJson.has("fastest_time_10_zombies_prison_normal")) {
                    int t = acdJson.get("fastest_time_10_zombies_prison_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                } else chain.append(new PlainText("null"));
                if (acdJson.has("fastest_time_20_zombies_prison_normal")) {
                    chain.append(new PlainText(" | 20回合: "));
                    int t = acdJson.get("fastest_time_20_zombies_prison_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }
                if (acdJson.has("fastest_time_30_zombies_prison_normal")) {
                    chain.append(new PlainText(" | 30回合: "));
                    int t = acdJson.get("fastest_time_30_zombies_prison_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                chain.append(new PlainText("\n    击杀僵尸: "));
                if (acdJson.has("zombie_kills_zombies_prison")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("zombie_kills_zombies_prison").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("players_revived_zombies_prison")) {
                    chain.append(new PlainText(" | 救援玩家: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("players_revived_zombies_prison").getAsInt())));
                }

                chain.append(new PlainText("\n    被击倒: "));
                if (acdJson.has("times_knocked_down_zombies_prison")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("times_knocked_down_zombies_prison").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (acdJson.has("deaths_zombies_prison")) {
                    chain.append(new PlainText(" | 死亡: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("deaths_zombies_prison").getAsInt())));
                }

                chain.append(new PlainText("\n    修复窗户: "));
                if (acdJson.has("windows_repaired_zombies_prison")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("windows_repaired_zombies_prison").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 开门: "));
                if (acdJson.has("doors_opened_zombies_prison")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("doors_opened_zombies_prison").getAsInt())));
                } else chain.append(new PlainText("null"));


                if (context.getSubject() != null) {
                    ForwardMessageBuilder builder = new ForwardMessageBuilder(context.getSubject());
                    builder.add(Objects.requireNonNull(context.getBot()).getId(), context.getBot().getNick(), chain.build());
                    context.sendMessage(builder.build());
                } else {
                    context.sendMessage(chain.build());
                }

            } else {
                chain.append(new PlainText(" 无法获取" + json.get("player").getAsJsonObject().get("displayname").getAsString() + "的街机游戏数据"));
                context.sendMessage(chain.build());
            }


        }


    }
}
