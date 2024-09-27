package moe.luoluo.hypixel;

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

public class MurderMystery {
    public static void murdermystery(CommandSender context, String player, String type) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();
        JsonObject mmJson;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        JsonObject json;
        String uuid = Api.mojang(player, "uuid");
        if (Objects.equals(uuid, "NotFound")) {
            context.sendMessage("玩家不存在");
            return;
        } else json = Api.hypixel("player", uuid);

        if (json.get("player").isJsonObject() && json.get("player").getAsJsonObject().has("stats") && json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("MurderMystery")) {
            mmJson = json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("MurderMystery").getAsJsonObject();

            chain.append(new PlainText(Rank.rank(json.get("player").getAsJsonObject()) + " "));
            chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
            chain.append(new PlainText(" | 密室杀手数据:"));

            if (type.isEmpty() || type.equals("all")) {
                //硬币
                chain.append(new PlainText("\n硬币: "));
                if (mmJson.has("coins")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("coins").getAsInt())));
                } else chain.append(new PlainText("null"));

                //捡起金锭
                chain.append(new PlainText(" | 捡起金锭: "));
                if (mmJson.has("coins_pickedup")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("coins_pickedup").getAsInt())));
                } else chain.append(new PlainText("null"));

                //游戏次数
                chain.append(new PlainText("\n游戏次数: "));
                if (mmJson.has("games")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("games").getAsInt())));
                } else chain.append(new PlainText("null"));

                //胜场
                chain.append(new PlainText(" | 胜场: "));
                if (mmJson.has("wins")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("wins").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | WLR: "));
                if (mmJson.has("games") && mmJson.has("wins")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("wins").getAsInt() /
                                    (mmJson.get("games").getAsInt() -
                                            mmJson.get("wins").getAsInt())
                    )));
                } else chain.append(new PlainText("null"));

                //侦探胜场
                if (mmJson.has("detective_wins") && mmJson.has("games")) {
                    chain.append(new PlainText("\n侦探胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("detective_wins").getAsInt())));
                    if (mmJson.has("quickest_detective_win_time_seconds")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_detective_win_time_seconds").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //杀手胜场
                if (mmJson.has("murderer_wins") && mmJson.has("games")) {
                    chain.append(new PlainText("\n杀手胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("murderer_wins").getAsInt())));
                    if (mmJson.has("quickest_murderer_win_time_seconds")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_murderer_win_time_seconds").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //英雄胜场
                if (mmJson.has("was_hero") && mmJson.has("games")) {
                    chain.append(new PlainText("\n英雄胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("was_hero").getAsInt())));
                }

                //总击杀
                chain.append(new PlainText("\n最终击杀: "));
                if (mmJson.has("kills")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills").getAsInt())));
                } else chain.append(new PlainText("null"));

                //总死亡
                chain.append(new PlainText(" | 最终死亡: "));
                if (mmJson.has("deaths")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("deaths").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (mmJson.has("kills") && mmJson.has("deaths")) {
                    chain.append(new PlainText(" | FKDR: "));
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("kills").getAsInt() /
                                    (float) mmJson.get("deaths").getAsInt()
                    )));
                }

                //总弓箭击杀
                chain.append(new PlainText("\n弓箭击杀: "));
                if (mmJson.has("bow_kills")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("bow_kills").getAsInt())));
                } else chain.append(new PlainText("null"));

                //总匕首击杀
                chain.append(new PlainText(" | 匕首击杀: "));
                if (mmJson.has("knife_kills")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("knife_kills").getAsInt())));
                } else chain.append(new PlainText("null"));

                //总飞刀击杀
                chain.append(new PlainText("\n飞刀击杀: "));
                if (mmJson.has("thrown_knife_kills")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("thrown_knife_kills").getAsInt())));
                } else chain.append(new PlainText("null"));

                //总陷阱击杀
                chain.append(new PlainText(" | 陷阱击杀: "));
                if (mmJson.has("trap_kills")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("trap_kills").getAsInt())));
                } else chain.append(new PlainText("null"));
            }

            //经典模式
            if (Objects.equals(type, "classic") || Objects.equals(type, "all")) {
                chain.append(new PlainText("\n\n经典模式: "));

                //场次
                chain.append(new PlainText("\n  游戏次数: "));
                if (mmJson.has("games_MURDER_CLASSIC")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("games_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //胜场
                chain.append(new PlainText(" | 胜场"));
                if (mmJson.has("wins_MURDER_CLASSIC")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("wins_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | WLR: "));
                if (mmJson.has("games_MURDER_CLASSIC") && mmJson.has("wins_MURDER_CLASSIC")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("wins_MURDER_CLASSIC").getAsInt() /
                                    (mmJson.get("games_MURDER_CLASSIC").getAsInt() -
                                            mmJson.get("wins_MURDER_CLASSIC").getAsInt())
                    )));
                } else chain.append(new PlainText("null"));

                //侦探胜场
                if (mmJson.has("detective_wins_MURDER_CLASSIC")) {
                    chain.append(new PlainText("\n  侦探胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("detective_wins_MURDER_CLASSIC").getAsInt())));
                    if (mmJson.has("quickest_detective_win_time_seconds_MURDER_CLASSIC")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_detective_win_time_seconds_MURDER_CLASSIC").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //杀手胜场
                if (mmJson.has("murderer_wins_MURDER_CLASSIC")) {
                    chain.append(new PlainText("\n  杀手胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("murderer_wins_MURDER_CLASSIC").getAsInt())));
                    if (mmJson.has("quickest_murderer_win_time_seconds_MURDER_CLASSIC")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_murderer_win_time_seconds_MURDER_CLASSIC").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //英雄胜场
                if (mmJson.has("was_hero_MURDER_CLASSIC")) {
                    chain.append(new PlainText("\n  英雄胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("was_hero_MURDER_CLASSIC").getAsInt())));
                }

                //经典模式击杀
                chain.append(new PlainText("\n  击杀: "));
                if (mmJson.has("kills_MURDER_CLASSIC")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //经典模式死亡
                chain.append(new PlainText(" | 死亡: "));
                if (mmJson.has("deaths_MURDER_CLASSIC")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("deaths_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //经典模式KD
                chain.append(new PlainText(" | KD: "));
                if (mmJson.has("kills_MURDER_CLASSIC") && mmJson.has("deaths_MURDER_CLASSIC")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("kills_MURDER_CLASSIC").getAsInt() /
                                    (float) mmJson.get("deaths_MURDER_CLASSIC").getAsInt()
                    )));
                }

                //弓箭击杀
                chain.append(new PlainText("\n  弓箭击杀: "));
                if (mmJson.has("bow_kills_MURDER_CLASSIC")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("bow_kills_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //匕首击杀
                chain.append(new PlainText(" | 匕首击杀: "));
                if (mmJson.has("knife_kills_MURDER_CLASSIC")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("knife_kills_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //飞刀击杀
                chain.append(new PlainText(" \n  飞刀击杀: "));
                if (mmJson.has("thrown_knife_kills_MURDER_CLASSIC")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("thrown_knife_kills_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //陷阱击杀
                chain.append(new PlainText(" | 陷阱击杀: "));
                if (mmJson.has("trap_kills_MURDER_CLASSIC")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("trap_kills_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));
            }

            //双倍模式
            if (Objects.equals(type, "double") || Objects.equals(type, "all")) {
                chain.append(new PlainText("\n\n双倍模式: "));

                //场次
                chain.append(new PlainText("\n  游戏次数: "));
                if (mmJson.has("games_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("games_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //胜场
                chain.append(new PlainText(" | 胜场: "));
                if (mmJson.has("wins_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("wins_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | WLR: "));
                if (mmJson.has("games_MURDER_DOUBLE_UP") && mmJson.has("wins_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("wins_MURDER_DOUBLE_UP").getAsInt() /
                                    (mmJson.get("games_MURDER_DOUBLE_UP").getAsInt() -
                                            mmJson.get("wins_MURDER_DOUBLE_UP").getAsInt())
                    )));
                } else chain.append(new PlainText("null"));

                //侦探胜场
                if (mmJson.has("detective_wins_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText("\n  侦探胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("detective_wins_MURDER_DOUBLE_UP").getAsInt())));
                    if (mmJson.has("quickest_detective_win_time_seconds_MURDER_DOUBLE_UP")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_detective_win_time_seconds_MURDER_DOUBLE_UP").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //杀手胜场
                if (mmJson.has("murderer_wins_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText("\n  杀手胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("murderer_wins_MURDER_DOUBLE_UP").getAsInt())));
                    if (mmJson.has("quickest_murderer_win_time_seconds_MURDER_DOUBLE_UP")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_murderer_win_time_seconds_MURDER_DOUBLE_UP").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //英雄胜场
                if (mmJson.has("was_hero_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText("\n  英雄胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("was_hero_MURDER_DOUBLE_UP").getAsInt())));
                }

                //双倍模式击杀
                chain.append(new PlainText("\n  击杀: "));
                if (mmJson.has("kills_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //双倍模式死亡
                chain.append(new PlainText(" | 死亡: "));
                if (mmJson.has("deaths_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("deaths_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //双倍模式KD
                chain.append(new PlainText(" | KD: "));
                if (mmJson.has("kills_MURDER_DOUBLE_UP") && mmJson.has("deaths_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("kills_MURDER_DOUBLE_UP").getAsInt() /
                                    (float) mmJson.get("deaths_MURDER_DOUBLE_UP").getAsInt()
                    )));
                } else chain.append(new PlainText("null"));

                //弓箭击杀
                chain.append(new PlainText("\n  弓箭击杀: "));
                if (mmJson.has("bow_kills_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("bow_kills_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //匕首击杀
                chain.append(new PlainText(" | 匕首击杀: "));
                if (mmJson.has("knife_kills_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("knife_kills_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //飞刀击杀
                chain.append(new PlainText("\n  飞刀击杀: "));
                if (mmJson.has("thrown_knife_kills_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("thrown_knife_kills_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //陷阱击杀
                chain.append(new PlainText(" | 陷阱击杀: "));
                if (mmJson.has("trap_kills_MURDER_DOUBLE_UP")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("trap_kills_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));
            }

            //感染模式
            if (Objects.equals(type, "infection") || Objects.equals(type, "all")) {
                chain.append(new PlainText("\n\n感染模式: "));

                chain.append(new PlainText("\n  总生存时间: "));
                if (mmJson.has("total_time_survived_seconds")) {
                    int t = mmJson.get("total_time_survived_seconds").getAsInt();
                    if (t >= 86400) {
                        chain.append(new PlainText(t / 86400 + "天" +
                                String.format("%02d", t % 86400 / 3600) + ":" +
                                String.format("%02d", t % 3600 / 60) + ":" +
                                String.format("%02d", t % 60)));
                    } else {
                        chain.append(new PlainText(String.format("%02d", t % 86400 / 3600) + ":" +
                                String.format("%02d", t % 3600 / 60) + ":" +
                                String.format("%02d", t % 60)));
                    }
                } else chain.append(new PlainText("null"));
                if (mmJson.has("longest_time_as_survivor_seconds")) {
                    chain.append(new PlainText(" | 幸存者最长生存时间: "));
                    int t = mmJson.get("longest_time_as_survivor_seconds").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                //场次
                chain.append(new PlainText("\n  场次: "));
                if (mmJson.has("games_MURDER_INFECTION")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("games_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));

                //胜场
                chain.append(new PlainText(" | 胜场: "));
                if (mmJson.has("wins_MURDER_INFECTION")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("wins_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (mmJson.has("games_MURDER_INFECTION") && mmJson.has("wins_MURDER_INFECTION")) {
                    chain.append(new PlainText(" | WLR: "));
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("wins_MURDER_INFECTION").getAsInt() /
                                    (mmJson.get("games_MURDER_INFECTION").getAsInt() -
                                            mmJson.get("wins_MURDER_INFECTION").getAsInt())
                    )));
                }

                //击杀
                chain.append(new PlainText("\n  最终击杀: "));
                if (mmJson.has("kills_MURDER_INFECTION")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));

                //死亡
                chain.append(new PlainText(" | 最终死亡: "));
                if (mmJson.has("deaths_MURDER_INFECTION")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("deaths_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));

                //KD
                if (mmJson.has("kills_MURDER_INFECTION") && mmJson.has("deaths_MURDER_INFECTION")) {
                    chain.append(new PlainText(" | FKDR: "));
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("kills_MURDER_INFECTION").getAsInt() /
                                    (float) mmJson.get("deaths_MURDER_INFECTION").getAsInt()
                    )));
                }

                //幸存者击杀
                chain.append(new PlainText("\n  幸存者击杀: "));
                if (mmJson.has("kills_as_survivor_MURDER_INFECTION")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_as_survivor_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));

                //感染者击杀
                chain.append(new PlainText(" | 感染者击杀: "));
                if (mmJson.has("kills_as_infected_MURDER_INFECTION")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_as_infected_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));
            }

            //刺客模式
            if (Objects.equals(type, "assassin") || Objects.equals(type, "assassins") || Objects.equals(type, "all")) {
                chain.append(new PlainText("\n\n刺客模式: "));

                //场次
                chain.append(new PlainText("\n  场次: "));
                if (mmJson.has("games_MURDER_ASSASSINS")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("games_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //胜场
                chain.append(new PlainText(" | 胜场: "));
                if (mmJson.has("wins_MURDER_ASSASSINS")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("wins_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | WLR: "));
                if (mmJson.has("games_MURDER_ASSASSINS") && mmJson.has("wins_MURDER_ASSASSINS")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("wins_MURDER_ASSASSINS").getAsInt() /
                                    (mmJson.get("games_MURDER_ASSASSINS").getAsInt() -
                                            mmJson.get("wins_MURDER_ASSASSINS").getAsInt())
                    )));
                } else chain.append(new PlainText("null"));

                //刺客击杀
                chain.append(new PlainText("\n  击杀: "));
                if (mmJson.has("kills_MURDER_ASSASSINS")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //刺客死亡
                chain.append(new PlainText(" | 死亡: "));
                if (mmJson.has("deaths_MURDER_ASSASSINS")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("deaths_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //刺客KD
                chain.append(new PlainText(" | KD: "));
                if (mmJson.has("kills_MURDER_ASSASSINS") && mmJson.has("deaths_MURDER_ASSASSINS")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("kills_MURDER_ASSASSINS").getAsInt() /
                                    (float) mmJson.get("deaths_MURDER_ASSASSINS").getAsInt()
                    )));
                } else chain.append(new PlainText("null"));

                //弓箭击杀
                chain.append(new PlainText("\n  弓箭击杀: "));
                if (mmJson.has("bow_kills_MURDER_ASSASSINS")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("bow_kills_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //匕首击杀
                chain.append(new PlainText(" | 匕首击杀: "));
                if (mmJson.has("knife_kills_MURDER_ASSASSINS")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("knife_kills_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //飞刀击杀
                chain.append(new PlainText("\n  飞刀击杀: "));
                if (mmJson.has("thrown_knife_kills_MURDER_ASSASSINS")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("thrown_knife_kills_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //陷阱击杀
                chain.append(new PlainText(" | 陷阱击杀: "));
                if (mmJson.has("trap_kills_MURDER_ASSASSINS")) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("trap_kills_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));


                if (context.getSubject() != null) {
                    ForwardMessageBuilder builder = new ForwardMessageBuilder(context.getSubject());
                    builder.add(Objects.requireNonNull(context.getBot()).getId(), context.getBot().getNick(), chain.build());
                    context.sendMessage(builder.build());
                } else context.sendMessage(chain.build());
            } else {
                context.sendMessage(chain.build());
            }
        } else {
            chain.append(new PlainText("该玩家的密室杀手数据为空"));
            context.sendMessage(chain.build());
        }
    }
}
