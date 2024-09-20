package moe.luoluo.hypixel;

import com.google.gson.JsonObject;
import moe.luoluo.Api;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Objects;

public class Bedwars {
    public static void bedwars(CommandSender context, String player, String type) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();
        MessageChainBuilder error = new MessageChainBuilder();

        JsonObject json;
        String uuid = Api.mojang(player, "uuid");
        if (Objects.equals(uuid, "NotFound")) {
            context.sendMessage("玩家不存在");
            return;
        } else json = Api.hypixel("player", uuid);

        JsonObject playerJson = json.get("player").getAsJsonObject();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");


        chain.append(new PlainText(Rank.rank(playerJson) + " ")); //玩家名称前缀
        chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
        chain.append(new PlainText(" | 起床战争数据:\n"));

        if (playerJson.has("stats") && playerJson.get("stats").getAsJsonObject().has("Bedwars")) {
            JsonObject bwJson = json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject();

            int k = 0;
            int d = 0;

            if (type.isEmpty()) {
                if (bwJson.has("games_played_bedwars") && bwJson.has("Experience")) {
                    chain.append(new PlainText("等级: "));
                    chain.append(new PlainText(String.valueOf(json.get("player").getAsJsonObject().get("achievements").getAsJsonObject().get("bedwars_level").getAsInt())));
                    chain.append(new PlainText("\n游戏场次: "));
                    chain.append(new PlainText(String.valueOf(bwJson.get("games_played_bedwars").getAsInt())));
                    if (bwJson.has("winstreak")) {
                        chain.append(new PlainText(" | 连胜: "));
                        chain.append(new PlainText(String.valueOf(bwJson.get("winstreak").getAsInt())));
                    }
                }

                chain.append(new PlainText("\n胜场: "));
                if (bwJson.has("wins_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 败场: "));
                if (bwJson.has("losses_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("losses_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | WLR: "));
                if (bwJson.has("wins_bedwars") && bwJson.has("losses_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("wins_bedwars").getAsInt() /
                                    (float) bwJson.get("losses_bedwars").getAsInt())));
                } else if (bwJson.has("wins_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n摧毁床: "));
                if (bwJson.has("beds_broken_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 被摧毁床: "));
                if (bwJson.has("beds_lost_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("beds_lost_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | BLR: "));
                if (bwJson.has("beds_broken_bedwars") && bwJson.has("beds_lost_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("beds_broken_bedwars").getAsInt() /
                                    (float) bwJson.get("beds_lost_bedwars").getAsInt())));
                } else if (bwJson.has("beds_broken_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n击杀: "));
                if (bwJson.has("kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 死亡: "));
                if (bwJson.has("deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | KDR: "));
                if (bwJson.has("kills_bedwars") && bwJson.has("deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("kills_bedwars").getAsInt() /
                                    (float) bwJson.get("deaths_bedwars").getAsInt())));
                } else if (bwJson.has("kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n最终击杀: "));
                if (bwJson.has("final_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 最终死亡: "));
                if (bwJson.has("final_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("final_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | FKDR："));
                if (bwJson.has("final_kills_bedwars") && bwJson.has("final_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("final_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("final_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("final_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                if (bwJson.has("kills_bedwars") || bwJson.has("final_kills_bedwars") ||
                        bwJson.has("deaths_bedwars") || bwJson.has("final_deaths_bedwars")) {
                    if (bwJson.has("kills_bedwars")) {
                        k += bwJson.get("kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("final_kills_bedwars")) {
                        k += bwJson.get("final_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("deaths_bedwars")) {
                        d += bwJson.get("deaths_bedwars").getAsInt();
                    }
                    if (bwJson.has("final_deaths_bedwars")) {
                        d += bwJson.get("final_deaths_bedwars").getAsInt();
                    }
                    chain.append(new PlainText("\n总击杀: "));
                    chain.append(new PlainText(String.valueOf(k)));
                    chain.append(new PlainText(" | 总死亡: "));
                    chain.append(new PlainText(String.valueOf(d)));
                    chain.append(new PlainText(" | 总KD: "));
                    if (d != 0) {
                        chain.append(new PlainText(decimalFormat.format((double) k / d)));
                    } else chain.append(new PlainText(decimalFormat.format(k)));
                }

                //Solo
            } else if (Objects.equals(type, "solo") || Objects.equals(type, "1s")) {
                chain.append(new PlainText("Solo"));
                if (bwJson.has("eight_one_games_played_bedwars")) {
                    chain.append(new PlainText(" | 场次: "));
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_one_games_played_bedwars").getAsInt())));
                }
                chain.append(new PlainText("\n胜场: "));
                if (bwJson.has("eight_one_wins_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_one_wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 败场: "));
                if (bwJson.has("eight_one_losses_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_one_losses_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | WLR: "));
                if (bwJson.has("eight_one_wins_bedwars") && bwJson.has("eight_one_losses_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("eight_one_wins_bedwars").getAsInt() /
                                    (float) bwJson.get("eight_one_losses_bedwars").getAsInt())));
                } else if (bwJson.has("eight_one_wins_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("eight_one_wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n摧毁床: "));
                if (bwJson.has("eight_one_beds_broken_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_one_beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 被摧毁床: "));
                if (bwJson.has("eight_one_beds_lost_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_one_beds_lost_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | BLR: "));
                if (bwJson.has("eight_one_beds_broken_bedwars") && bwJson.has("eight_one_beds_lost_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("eight_one_beds_broken_bedwars").getAsInt() /
                                    (float) bwJson.get("eight_one_beds_lost_bedwars").getAsInt())));
                } else if (bwJson.has("eight_one_beds_broken_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("eight_one_beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n击杀: "));
                if (bwJson.has("eight_one_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_one_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 死亡: "));
                if (bwJson.has("eight_one_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_one_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | KDR: "));
                if (bwJson.has("eight_one_kills_bedwars") && bwJson.has("eight_one_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("eight_one_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("eight_one_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("eight_one_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("eight_one_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n最终击杀: "));
                if (bwJson.has("eight_one_final_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_one_final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 最终死亡: "));
                if (bwJson.has("eight_one_final_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_one_final_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | FKDR："));
                if (bwJson.has("eight_one_final_kills_bedwars") && bwJson.has("eight_one_final_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("eight_one_final_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("eight_one_final_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("eight_one_final_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("eight_one_final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                if (bwJson.has("eight_one_kills_bedwars") || bwJson.has("eight_one_final_kills_bedwars") ||
                        bwJson.has("eight_one_deaths_bedwars") || bwJson.has("eight_one_final_deaths_bedwars")) {
                    if (bwJson.has("eight_one_kills_bedwars")) {
                        k += bwJson.get("eight_one_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("eight_one_final_kills_bedwars")) {
                        k += bwJson.get("eight_one_final_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("eight_one_deaths_bedwars")) {
                        d += bwJson.get("eight_one_deaths_bedwars").getAsInt();
                    }
                    if (bwJson.has("eight_one_final_deaths_bedwars")) {
                        d += bwJson.get("eight_one_final_deaths_bedwars").getAsInt();
                    }
                    chain.append(new PlainText("\n总击杀: "));
                    chain.append(new PlainText(String.valueOf(k)));
                    chain.append(new PlainText(" | 总死亡: "));
                    chain.append(new PlainText(String.valueOf(d)));
                    chain.append(new PlainText(" | 总KD: "));
                    if (d != 0) {
                        chain.append(new PlainText(decimalFormat.format((double) k / d)));
                    } else chain.append(new PlainText(decimalFormat.format(k)));
                }

                //双倍
            } else if (Objects.equals(type, "double") || Objects.equals(type, "2s")) {
                chain.append(new PlainText("Double"));
                if (bwJson.has("eight_two_games_played_bedwars")) {
                    chain.append(new PlainText(" | 场次: "));
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_two_games_played_bedwars").getAsInt())));
                }
                chain.append(new PlainText("\n胜场: "));
                if (bwJson.has("eight_two_wins_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_two_wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 败场: "));
                if (bwJson.has("eight_two_losses_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_two_losses_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | WLR: "));
                if (bwJson.has("eight_two_wins_bedwars") && bwJson.has("eight_two_losses_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("eight_two_wins_bedwars").getAsInt() /
                                    (float) bwJson.get("eight_two_losses_bedwars").getAsInt())));
                } else if (bwJson.has("eight_two_wins_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("eight_two_wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n摧毁床: "));
                if (bwJson.has("eight_two_beds_broken_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_two_beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 被摧毁床: "));
                if (bwJson.has("eight_two_beds_lost_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_two_beds_lost_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | BLR: "));
                if (bwJson.has("eight_two_beds_broken_bedwars") && bwJson.has("eight_two_beds_lost_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("eight_two_beds_broken_bedwars").getAsInt() /
                                    (float) bwJson.get("eight_two_beds_lost_bedwars").getAsInt())));
                } else if (bwJson.has("eight_two_beds_broken_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("eight_two_beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n击杀: "));
                if (bwJson.has("eight_two_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_two_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 死亡: "));
                if (bwJson.has("eight_two_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_two_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | KDR: "));
                if (bwJson.has("eight_two_kills_bedwars") && bwJson.has("eight_two_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("eight_two_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("eight_two_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("eight_two_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("eight_two_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n最终击杀: "));
                if (bwJson.has("eight_two_final_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_two_final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 最终死亡: "));
                if (bwJson.has("eight_two_final_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("eight_two_final_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | FKDR："));
                if (bwJson.has("eight_two_final_kills_bedwars") && bwJson.has("eight_two_final_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("eight_two_final_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("eight_two_final_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("eight_two_final_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("eight_two_final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                if (bwJson.has("eight_two_kills_bedwars") || bwJson.has("eight_two_final_kills_bedwars") ||
                        bwJson.has("eight_two_deaths_bedwars") || bwJson.has("eight_two_final_deaths_bedwars")) {
                    if (bwJson.has("eight_two_kills_bedwars")) {
                        k += bwJson.get("eight_two_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("eight_two_final_kills_bedwars")) {
                        k += bwJson.get("eight_two_final_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("eight_two_deaths_bedwars")) {
                        d += bwJson.get("eight_two_deaths_bedwars").getAsInt();
                    }
                    if (bwJson.has("eight_two_final_deaths_bedwars")) {
                        d += bwJson.get("eight_two_final_deaths_bedwars").getAsInt();
                    }
                    chain.append(new PlainText("\n总击杀: "));
                    chain.append(new PlainText(String.valueOf(k)));
                    chain.append(new PlainText(" | 总死亡: "));
                    chain.append(new PlainText(String.valueOf(d)));
                    chain.append(new PlainText(" | 总KD: "));
                    if (d != 0) {
                        chain.append(new PlainText(decimalFormat.format((double) k / d)));
                    } else chain.append(new PlainText(decimalFormat.format(k)));
                }

                //3s
            } else if (Objects.equals(type, "3s")) {
                chain.append(new PlainText("3v3v3v3"));
                if (bwJson.has("four_three_games_played_bedwars")) {
                    chain.append(new PlainText(" | 场次: "));
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_three_games_played_bedwars").getAsInt())));
                }
                chain.append(new PlainText("\n胜场: "));
                if (bwJson.has("four_three_wins_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_three_wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 败场: "));
                if (bwJson.has("four_three_losses_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_three_losses_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | WLR: "));
                if (bwJson.has("four_three_wins_bedwars") && bwJson.has("four_three_losses_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("four_three_wins_bedwars").getAsInt() /
                                    (float) bwJson.get("four_three_losses_bedwars").getAsInt())));
                } else if (bwJson.has("four_three_wins_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("four_three_wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n摧毁床: "));
                if (bwJson.has("four_three_beds_broken_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_three_beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 被摧毁床: "));
                if (bwJson.has("four_three_beds_lost_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_three_beds_lost_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | BLR: "));
                if (bwJson.has("four_three_beds_broken_bedwars") && bwJson.has("four_three_beds_lost_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("four_three_beds_broken_bedwars").getAsInt() /
                                    (float) bwJson.get("four_three_beds_lost_bedwars").getAsInt())));
                } else if (bwJson.has("four_three_beds_broken_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("four_three_beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n击杀: "));
                if (bwJson.has("four_three_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_three_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 死亡: "));
                if (bwJson.has("four_three_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_three_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | KDR: "));
                if (bwJson.has("four_three_kills_bedwars") && bwJson.has("four_three_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("four_three_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("four_three_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("four_three_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("four_three_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n最终击杀: "));
                if (bwJson.has("four_three_final_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_three_final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 最终死亡: "));
                if (bwJson.has("four_three_final_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_three_final_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | FKDR："));
                if (bwJson.has("four_three_final_kills_bedwars") && bwJson.has("four_three_final_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("four_three_final_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("four_three_final_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("four_three_final_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("four_three_final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                if (bwJson.has("four_three_kills_bedwars") || bwJson.has("four_three_final_kills_bedwars") ||
                        bwJson.has("four_three_deaths_bedwars") || bwJson.has("four_three_final_deaths_bedwars")) {
                    if (bwJson.has("four_three_kills_bedwars")) {
                        k += bwJson.get("four_three_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("four_three_final_kills_bedwars")) {
                        k += bwJson.get("four_three_final_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("four_three_deaths_bedwars")) {
                        d += bwJson.get("four_three_deaths_bedwars").getAsInt();
                    }
                    if (bwJson.has("four_three_final_deaths_bedwars")) {
                        d += bwJson.get("four_three_final_deaths_bedwars").getAsInt();
                    }
                    chain.append(new PlainText("\n总击杀: "));
                    chain.append(new PlainText(String.valueOf(k)));
                    chain.append(new PlainText(" | 总死亡: "));
                    chain.append(new PlainText(String.valueOf(d)));
                    chain.append(new PlainText(" | 总KD: "));
                    if (d != 0) {
                        chain.append(new PlainText(decimalFormat.format((double) k / d)));
                    } else chain.append(new PlainText(decimalFormat.format(k)));
                }

                //4s
            } else if (Objects.equals(type, "4s")) {
                chain.append(new PlainText("4v4v4v4"));
                if (bwJson.has("four_four_games_played_bedwars")) {
                    chain.append(new PlainText(" | 场次: "));
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_four_games_played_bedwars").getAsInt())));
                }
                chain.append(new PlainText("\n胜场: "));
                if (bwJson.has("four_four_wins_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_four_wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 败场: "));
                if (bwJson.has("four_four_losses_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_four_losses_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | WLR: "));
                if (bwJson.has("four_four_wins_bedwars") && bwJson.has("four_four_losses_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("four_four_wins_bedwars").getAsInt() /
                                    (float) bwJson.get("four_four_losses_bedwars").getAsInt())));
                } else if (bwJson.has("four_four_wins_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("four_four_wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n摧毁床: "));
                if (bwJson.has("four_four_beds_broken_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_four_beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 被摧毁床: "));
                if (bwJson.has("four_four_beds_lost_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_four_beds_lost_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | BLR: "));
                if (bwJson.has("four_four_beds_broken_bedwars") && bwJson.has("four_four_beds_lost_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("four_four_beds_broken_bedwars").getAsInt() /
                                    (float) bwJson.get("four_four_beds_lost_bedwars").getAsInt())));
                } else if (bwJson.has("four_four_beds_broken_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("four_four_beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n击杀: "));
                if (bwJson.has("four_four_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_four_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 死亡: "));
                if (bwJson.has("four_four_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_four_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | KDR: "));
                if (bwJson.has("four_four_kills_bedwars") && bwJson.has("four_four_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("four_four_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("four_four_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("four_four_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("four_four_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n最终击杀: "));
                if (bwJson.has("four_four_final_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_four_final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 最终死亡: "));
                if (bwJson.has("four_four_final_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("four_four_final_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | FKDR："));
                if (bwJson.has("four_four_final_kills_bedwars") && bwJson.has("four_four_final_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("four_four_final_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("four_four_final_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("four_four_final_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("four_four_final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                if (bwJson.has("four_four_kills_bedwars") || bwJson.has("four_four_final_kills_bedwars") ||
                        bwJson.has("four_four_deaths_bedwars") || bwJson.has("four_four_final_deaths_bedwars")) {
                    if (bwJson.has("four_four_kills_bedwars")) {
                        k += bwJson.get("four_four_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("four_four_final_kills_bedwars")) {
                        k += bwJson.get("four_four_final_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("four_four_deaths_bedwars")) {
                        d += bwJson.get("four_four_deaths_bedwars").getAsInt();
                    }
                    if (bwJson.has("four_four_final_deaths_bedwars")) {
                        d += bwJson.get("four_four_final_deaths_bedwars").getAsInt();
                    }
                    chain.append(new PlainText("\n总击杀: "));
                    chain.append(new PlainText(String.valueOf(k)));
                    chain.append(new PlainText(" | 总死亡: "));
                    chain.append(new PlainText(String.valueOf(d)));
                    chain.append(new PlainText(" | 总KD: "));
                    if (d != 0) {
                        chain.append(new PlainText(decimalFormat.format((double) k / d)));
                    } else chain.append(new PlainText(decimalFormat.format(k)));
                }

                //4v4
            } else if (Objects.equals(type, "4v4")) {
                chain.append(new PlainText("4v4"));
                if (bwJson.has("two_four_games_played_bedwars")) {
                    chain.append(new PlainText(" | 场次: "));
                    chain.append(new PlainText(String.valueOf(bwJson.get("two_four_games_played_bedwars").getAsInt())));
                }
                chain.append(new PlainText("\n胜场: "));
                if (bwJson.has("two_four_wins_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("two_four_wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 败场: "));
                if (bwJson.has("two_four_losses_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("two_four_losses_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | WLR: "));
                if (bwJson.has("two_four_wins_bedwars") && bwJson.has("two_four_losses_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("two_four_wins_bedwars").getAsInt() /
                                    (float) bwJson.get("two_four_losses_bedwars").getAsInt())));
                } else if (bwJson.has("two_four_wins_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("two_four_wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n摧毁床: "));
                if (bwJson.has("two_four_beds_broken_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("two_four_beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 被摧毁床: "));
                if (bwJson.has("two_four_beds_lost_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("two_four_beds_lost_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | BLR: "));
                if (bwJson.has("two_four_beds_broken_bedwars") && bwJson.has("two_four_beds_lost_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("two_four_beds_broken_bedwars").getAsInt() /
                                    (float) bwJson.get("two_four_beds_lost_bedwars").getAsInt())));
                } else if (bwJson.has("two_four_beds_broken_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("two_four_beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n击杀: "));
                if (bwJson.has("two_four_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("two_four_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 死亡: "));
                if (bwJson.has("two_four_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("two_four_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | KDR: "));
                if (bwJson.has("two_four_kills_bedwars") && bwJson.has("two_four_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("two_four_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("two_four_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("two_four_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("two_four_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n最终击杀: "));
                if (bwJson.has("two_four_final_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("two_four_final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 最终死亡: "));
                if (bwJson.has("two_four_final_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("two_four_final_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | FKDR："));
                if (bwJson.has("two_four_final_kills_bedwars") && bwJson.has("two_four_final_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("two_four_final_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("two_four_final_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("two_four_final_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("two_four_final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                if (bwJson.has("two_four_kills_bedwars") || bwJson.has("two_four_final_kills_bedwars") ||
                        bwJson.has("two_four_deaths_bedwars") || bwJson.has("two_four_final_deaths_bedwars")) {
                    if (bwJson.has("two_four_kills_bedwars")) {
                        k += bwJson.get("two_four_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("two_four_final_kills_bedwars")) {
                        k += bwJson.get("two_four_final_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("two_four_deaths_bedwars")) {
                        d += bwJson.get("two_four_deaths_bedwars").getAsInt();
                    }
                    if (bwJson.has("two_four_final_deaths_bedwars")) {
                        d += bwJson.get("two_four_final_deaths_bedwars").getAsInt();
                    }
                    chain.append(new PlainText("\n总击杀: "));
                    chain.append(new PlainText(String.valueOf(k)));
                    chain.append(new PlainText(" | 总死亡: "));
                    chain.append(new PlainText(String.valueOf(d)));
                    chain.append(new PlainText(" | 总KD: "));
                    if (d != 0) {
                        chain.append(new PlainText(decimalFormat.format((double) k / d)));
                    } else chain.append(new PlainText(decimalFormat.format(k)));
                }
            } else {
                error.append("type有误，支持参数：solo, double, 3s, 4s, 4v4");
                context.sendMessage(error.build());
                return;
            }
            context.sendMessage(chain.build());
        } else {
            error.append(new PlainText("该玩家的起床战争数据为空"));
            context.sendMessage(error.build());
        }

    }
}
