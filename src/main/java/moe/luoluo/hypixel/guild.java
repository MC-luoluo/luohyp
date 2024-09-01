package moe.luoluo.hypixel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import moe.luoluo.Api;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Guild {
    static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public static void guild(CommandSender context, String px, String name, String type) throws URISyntaxException, IOException {
        MessageChainBuilder chain = new MessageChainBuilder();
        MessageChainBuilder achievementChain = new MessageChainBuilder();
        MessageChainBuilder membersChain = new MessageChainBuilder();
        MessageChainBuilder preferredGames = new MessageChainBuilder();
        MessageChainBuilder membersList = new MessageChainBuilder();
        MessageChainBuilder membersList2 = new MessageChainBuilder();
        MessageChainBuilder membersList3 = new MessageChainBuilder();
        MessageChainBuilder gameExp = new MessageChainBuilder();


        JsonArray members;
        JsonObject achievements = new JsonObject();

        JsonObject json;
        switch (px) {
            case "name", "id":
                json = Api.hypixel("guild", px, name);
                break;
            default:
                String uuid = Api.mojang(name, "uuid");
                if (Objects.equals(uuid, "NotFound")) {
                    context.sendMessage("玩家不存在");
                    return;
                } else json = Api.hypixel("player", uuid);
                break;
        }

        if (json.get("guild").isJsonObject()) {
            json = json.get("guild").getAsJsonObject();
            members = json.get("members").getAsJsonArray();
            if (json.has("achievements")) achievements = json.get("achievements").getAsJsonObject();

            chain.append(new PlainText("公会名称: "));
            chain.append(new PlainText(json.get("name").getAsString()));

            //标签 & 颜色
            if (json.has("tag")) {
                //chain.append(new PlainText("\n标签: "));
                chain.append(new PlainText(" [" + json.get("tag").getAsString() + "]"));
                if (json.has("tagColor")) {
                    chain.append(new PlainText("(" + Rank.color(json.get("tagColor").getAsString()) + ")"));
                }
            }

            chain.append(new PlainText("\n公会id: "));
            chain.append(new PlainText(json.get("_id").getAsString()));

            chain.append(new PlainText("\n会长: "));
            for (int i = 0; i < members.size(); i++) {
                if (members.get(i).getAsJsonObject().get("rank").getAsString().equals("Guild Master") || members.get(i).getAsJsonObject().get("rank").getAsString().equals("GUILDMASTER")) {
                    chain.append(new PlainText(Api.mojang(members.get(i).getAsJsonObject().get("uuid").getAsString(), "name")));
                    break;
                }
            }

            chain.append(new PlainText("\n成员数量: "));
            chain.append(new PlainText(members.size() + "/125"));

            chain.append(new PlainText("\n公会创建时间: "));
            long created = json.get("created").getAsLong();
            Instant instant = Instant.ofEpochMilli(created);
            LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            chain.append(new PlainText(localDateTime.toString()));

            //经验 & 等级
            long exp = json.get("exp").getAsLong();
            int[] expNeeded = {100000, 150000, 250000, 500000, 750000, 1000000, 1250000, 1500000, 2000000, 2500000, 2500000, 2500000, 2500000, 2500000, 3000000};

            chain.append(new PlainText("\n等级: "));
            int level = 0;
            for (int j : expNeeded) {
                if (exp >= j) {
                    exp -= j;
                    level++;
                } else break;
            }
            while (exp >= 3000000) {
                level++;
                exp -= 3000000;
            }
            chain.append(new PlainText(String.valueOf(level)));

            //经验进度
            int xpLevel = Math.min(level, 14);
            chain.append(new PlainText(" (" + formatExp(exp) +
                    "/" + exp(xpLevel) + " " +
                    decimalFormat.format((float) exp / expNeeded[xpLevel] * 100) + "%)"
            ));

            //描述
            if (json.has("description")) {
                chain.append(new PlainText("\n描述: "));
                try {
                    chain.append(new PlainText(json.get("description").getAsString()));
                } catch (UnsupportedOperationException e) {
                    chain.append(new PlainText("null"));
                }
            }


            //公会成就
            achievementChain.append(new PlainText("公会成就:\n"));

            achievementChain.append(new PlainText("最高同时在线: "));
            achievementChain.append(new PlainText(String.valueOf(achievements.get("ONLINE_PLAYERS").getAsInt())));

            achievementChain.append(new PlainText("\n每日最高经验: "));
            if (determine(achievements, "EXPERIENCE_KINGS")) {
                achievementChain.append(new PlainText(formatExp(achievements.get("EXPERIENCE_KINGS").getAsInt())));
            } else achievementChain.append(new PlainText("null"));

            achievementChain.append(new PlainText("\n公会胜场数: "));
            if (determine(achievements, "WINNERS")) {
                achievementChain.append(new PlainText(String.valueOf(achievements.get("WINNERS").getAsInt())));
            } else achievementChain.append(new PlainText("null"));


            achievementChain.append(new PlainText("\n公会每周经验: "));
            Set<String> set = new HashSet<>();
            int sum = 0;

            //总计
            for (int i = 0; i < members.size(); i++) {
                JsonObject expHistory = new JsonObject();
                if (determine(members.get(i).getAsJsonObject(), "expHistory")) {
                    expHistory = members.get(i).getAsJsonObject().get("expHistory").getAsJsonObject();
                }
                set = expHistory.keySet();
                for (String j : set) {
                    sum += expHistory.get(j).getAsInt();
                }
            }
            achievementChain.append(new PlainText(formatExp(sum)));
            int weekExp = sum; //周 平均每位成员经验

            //周每日
            for (String s : set) {
                achievementChain.append(new PlainText("\n    " + s + ": "));
                sum = 0;
                for (int i = 0; i < members.size(); i++) {
                    JsonObject expHistory;
                    if (determine(members.get(i).getAsJsonObject(), "expHistory")) {
                        expHistory = members.get(i).getAsJsonObject().get("expHistory").getAsJsonObject();
                    } else continue;
                    sum += expHistory.get(s).getAsInt();
                }
                achievementChain.append(new PlainText(formatExp(sum)));
            }


            Iterator<String> iterator = set.iterator();
            String date = iterator.next();
            int daySum = 0;
            for (int i = 0; i < members.size(); i++) {
                JsonObject expHistory;
                if (determine(members.get(i).getAsJsonObject(), "expHistory")) {
                    expHistory = members.get(i).getAsJsonObject().get("expHistory").getAsJsonObject();
                } else continue;
                daySum += expHistory.get(date).getAsInt();
            }
            achievementChain.append(new PlainText("\n平均每位成员经验:"));
            achievementChain.append(new PlainText("\n今日: "));
            achievementChain.append(new PlainText(formatExp((float) daySum / members.size())));

            //一周
            achievementChain.append(new PlainText(" | 一周: "));
            achievementChain.append(new PlainText(formatExp((float) weekExp / members.size())));

            if (Objects.equals(px, "player")) {
                String uuid = Api.mojang(name, "uuid");
                membersChain.append(new PlainText("成员: "));
                membersChain.append(new PlainText(Api.mojang(uuid, "name")));

                for (int i = 0; i < members.size(); i++) {
                    JsonObject member = members.get(i).getAsJsonObject();
                    if (member.get("uuid").getAsString().equals(uuid)) {
                        //rank
                        membersChain.append(new PlainText("\n成员rank: "));
                        membersChain.append(member.get("rank").getAsString());

                        //加入时间
                        membersChain.append(new PlainText("\n加入时间: "));
                        instant = Instant.ofEpochMilli(member.get("joined").getAsLong());
                        LocalDateTime localDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                        membersChain.append(new PlainText(String.valueOf(localDate)));

                        //任务
                        if (determine(member, "questParticipation")) {
                            membersChain.append(new PlainText("\n完成任务: "));
                            membersChain.append(new PlainText(String.valueOf(member.get("questParticipation").getAsInt())));
                        }

                        //经验
                        if (determine(member, "expHistory")) {
                            membersChain.append(new PlainText("\n个人每周经验: "));
                            sum = 0;
                            JsonObject expHistory = member.get("expHistory").getAsJsonObject();
                            for (String s : set) {
                                sum += expHistory.get(s).getAsInt();
                            }
                            membersChain.append(new PlainText(formatExp(sum)));

                            for (String s : set) {
                                membersChain.append(new PlainText("\n    " + s + ": "));
                                membersChain.append(new PlainText(formatExp(expHistory.get(s).getAsInt())));

                                //排名
                                membersChain.append(new PlainText(" #"));
                                int e = expHistory.get(s).getAsInt();
                                int x = 1;
                                for (int m = 0; m < members.size(); m++) {
                                    if (e < members.get(m).getAsJsonObject().get("expHistory").getAsJsonObject().get(s).getAsInt()) {
                                        x++;
                                    }
                                }
                                membersChain.append(new PlainText(String.valueOf(x)));
                                membersChain.append(new PlainText("/"));
                                membersChain.append(new PlainText(String.valueOf(members.size())));
                            }
                        }
                        break;
                    }
                }
            }


            boolean game = Objects.equals(type, "games") || Objects.equals(type, "all");
            if (game && json.has("preferredGames")) {
                preferredGames.append(new PlainText("公会主打游戏: \n"));
                //preferredGames.append(new PlainText(String.valueOf(json.get("preferredGames").getAsJsonArray())));
                JsonArray perGames = json.get("preferredGames").getAsJsonArray();
                for (int i = 0; i < perGames.size(); i++) {
                    preferredGames.append(new PlainText(perGames.get(i).getAsString()));
                    if (i < perGames.size() - 1) {
                        preferredGames.append(new PlainText("、"));
                    }
                }
            }

            if (game && json.has("guildExpByGameType")) {
                JsonObject expType = json.get("guildExpByGameType").getAsJsonObject();
                gameExp.append(new PlainText("各游戏经验: "));
                if (expType.has("BEDWARS")) {
                    gameExp.append(new PlainText("\n起床战争: "));
                    gameExp.append(new PlainText(formatExp(expType.get("BEDWARS").getAsFloat())));
                }
                if (expType.has("SKYWARS")) {
                    gameExp.append(new PlainText("\n空岛战争: "));
                    gameExp.append(new PlainText(formatExp(expType.get("SKYWARS").getAsFloat())));
                }
                if (expType.has("DUELS")) {
                    gameExp.append(new PlainText("\n决斗游戏: "));
                    gameExp.append(new PlainText(formatExp(expType.get("DUELS").getAsFloat())));
                }
                if (expType.has("ARCADE")) {
                    gameExp.append(new PlainText("\n街机游戏: "));
                    gameExp.append(new PlainText(formatExp(expType.get("ARCADE").getAsFloat())));
                }
                if (expType.has("BUILD_BATTLE")) {
                    gameExp.append(new PlainText("\n建筑大师: "));
                    gameExp.append(new PlainText(formatExp(expType.get("BUILD_BATTLE").getAsFloat())));
                }
                if (expType.has("MURDER_MYSTERY")) {
                    gameExp.append(new PlainText("\n密室杀手: "));
                    gameExp.append(new PlainText(formatExp(expType.get("MURDER_MYSTERY").getAsFloat())));
                }
                if (expType.has("TNTGAMES")) {
                    gameExp.append(new PlainText("\n掘战游戏: "));
                    gameExp.append(new PlainText(formatExp(expType.get("TNTGAMES").getAsFloat())));
                }
                if (expType.has("PROTOTYPE")) {
                    gameExp.append(new PlainText("\n实验游戏: "));
                    gameExp.append(new PlainText(formatExp(expType.get("PROTOTYPE").getAsFloat())));
                }
                if (expType.has("HOUSING")) {
                    gameExp.append(new PlainText("\n家园: "));
                    gameExp.append(new PlainText(formatExp(expType.get("HOUSING").getAsFloat())));
                }
                if (expType.has("SURVIVAL_GAMES")) {
                    gameExp.append(new PlainText("\n闪电饥饿游戏: "));
                    gameExp.append(new PlainText(formatExp(expType.get("SURVIVAL_GAMES").getAsFloat())));
                }
                if (expType.has("WOOL_GAMES")) {
                    gameExp.append(new PlainText("\n羊毛战争: "));
                    gameExp.append(new PlainText(formatExp(expType.get("WOOL_GAMES").getAsFloat())));
                }
                if (expType.has("PIT")) {
                    gameExp.append(new PlainText("\n天坑乱斗: "));
                    gameExp.append(new PlainText(formatExp(expType.get("PIT").getAsFloat())));
                }
                if (expType.has("BATTLEGROUND")) {
                    gameExp.append(new PlainText("\n战争领主: "));
                    gameExp.append(new PlainText(formatExp(expType.get("BATTLEGROUND").getAsFloat())));
                }
                if (expType.has("SUPER_SMASH")) {
                    gameExp.append(new PlainText("\n星碎英雄: "));
                    gameExp.append(new PlainText(formatExp(expType.get("SUPER_SMASH").getAsFloat())));
                }
                if (expType.has("MCGO")) {
                    gameExp.append(new PlainText("\n警匪大战: "));
                    gameExp.append(new PlainText(formatExp(expType.get("MCGO").getAsFloat())));
                }
                if (expType.has("WALLS3")) {
                    gameExp.append(new PlainText("\n超级战墙: "));
                    gameExp.append(new PlainText(formatExp(expType.get("WALLS3").getAsFloat())));
                }
                if (expType.has("UHC")) {
                    gameExp.append(new PlainText("\nUHC: "));
                    gameExp.append(new PlainText(formatExp(expType.get("UHC").getAsFloat())));
                }
                if (expType.has("QUAKECRAFT")
                        || expType.has("ARENA")
                        || expType.has("WALLS")
                        || expType.has("VAMPIREZ")
                        || expType.has("GINGERBREAD")
                        || expType.has("PAINTBALL")) {
                    gameExp.append(new PlainText("\n经典游戏: "));
                    float legacySum = 0.0f;
                    if (expType.has("QUAKECRAFT")) legacySum += expType.get("QUAKECRAFT").getAsFloat();
                    if (expType.has("ARENA")) legacySum += expType.get("ARENA").getAsFloat();
                    if (expType.has("WALLS")) legacySum += expType.get("WALLS").getAsFloat();
                    if (expType.has("VAMPIREZ")) legacySum += expType.get("VAMPIREZ").getAsFloat();
                    if (expType.has("GINGERBREAD")) legacySum += expType.get("GINGERBREAD").getAsFloat();
                    if (expType.has("PAINTBALL")) legacySum += expType.get("PAINTBALL").getAsFloat();
                    gameExp.append(new PlainText(formatExp(legacySum)));
                }
            }

            if (Objects.equals(type, "members") || Objects.equals(type, "all")) {
                membersList.append(new PlainText("成员列表: "));
                for (int x = 0; x < members.size(); x++) {
                    if (x <= 42) {
                        gMembers(membersList, members, x);
                    } else if (x <= 84) {
                        gMembers(membersList2, members, x);
                    } else {
                        gMembers(membersList3, members, x);
                    }
                }
            }

            if (context.getSubject() != null) {
                ForwardMessageBuilder builder = new ForwardMessageBuilder(context.getSubject());
                builder.add(Objects.requireNonNull(context.getBot()).getId(), context.getBot().getNick(), chain.build());
                builder.add(context.getBot().getId(), context.getBot().getNick(), achievementChain.build());
                if (!membersChain.isEmpty()) {
                    builder.add(context.getBot().getId(), context.getBot().getNick(), membersChain.build());
                }
                if (!preferredGames.isEmpty()) {
                    builder.add(context.getBot().getId(), context.getBot().getNick(), preferredGames.build());
                }
                if (!gameExp.isEmpty()) {
                    builder.add(context.getBot().getId(), context.getBot().getNick(), gameExp.build());
                }
                if (!membersList.isEmpty()) {
                    builder.add(context.getBot().getId(), context.getBot().getNick(), membersList.build());
                }
                if (!membersList2.isEmpty()) {
                    builder.add(context.getBot().getId(), context.getBot().getNick(), membersList2.build());
                }
                if (!membersList3.isEmpty()) {
                    builder.add(context.getBot().getId(), context.getBot().getNick(), membersList3.build());
                }
                context.sendMessage(builder.build());
            } else {
                context.sendMessage(chain.build());
                context.sendMessage(achievementChain.build());
                if (!membersChain.isEmpty()) {
                    context.sendMessage(membersChain.build());
                }
                if (!preferredGames.isEmpty()) {
                    context.sendMessage(preferredGames.build());
                }
                if (!gameExp.isEmpty()) {
                    context.sendMessage(gameExp.build());
                }
                if (!membersList.isEmpty()) {
                    context.sendMessage(membersList.build());
                }
                if (!membersList2.isEmpty()) {
                    context.sendMessage(membersList2.build());
                }
                if (!membersList3.isEmpty()) {
                    context.sendMessage(membersList3.build());
                }
            }
        } else {
            if (px.equals("player")) {
                chain.append("该玩家未加入公会");
            } else {
                chain.append("不存在此公会");
            }
            context.sendMessage(chain.build());
        }
    }


    public static void gMembers(MessageChainBuilder chain, JsonArray members, int x) throws IOException, URISyntaxException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日-HH时mm分ss秒", Locale.CHINA);
        Set<String> set;
        chain.append(new PlainText("\n\n玩家: "));
        chain.append(new PlainText(Api.mojang(members.get(x).getAsJsonObject().get("uuid").getAsString(), "name")));
        chain.append(new PlainText("\nrank: "));
        chain.append(new PlainText(members.get(x).getAsJsonObject().get("rank").getAsString()));
        chain.append(new PlainText("\n加入时间: "));
        long t = members.get(x).getAsJsonObject().get("joined").getAsLong();
        chain.append(new PlainText(simpleDateFormat.format(new Date(t))));
        if (members.get(x).getAsJsonObject().has("questParticipation")) {
            chain.append(new PlainText("\n完成任务: "));
            chain.append(new PlainText(String.valueOf(members.get(x).getAsJsonObject().get("questParticipation").getAsInt())));
        }
        chain.append(new PlainText("\n周经验: "));
        JsonObject expHistory = new JsonObject();
        if (determine(members.get(x).getAsJsonObject(), "expHistory")) {
            expHistory = members.get(x).getAsJsonObject().get("expHistory").getAsJsonObject();
        }
        set = expHistory.keySet();
        int sum = 0;
        for (String j : set) {
            sum += expHistory.get(j).getAsInt();
        }
        chain.append(new PlainText(formatExp(sum)));
    }

    //格式化经验值
    public static String formatExp(float ex) {
        if (ex >= 100000 & ex < 1000000) {
            return decimalFormat.format(ex / 1000) + "K";
        } else if (ex > 1000000 & ex < 1000000000) {
            return decimalFormat.format(ex / 1000000) + "M";
        } else if (ex > 1000000000) {
            return decimalFormat.format(ex / 1000000000) + "B";
        } else {
            return String.valueOf((int) ex);
        }
    }

    public static Boolean determine(JsonObject json, String str) {
        return json.has(str);
    }

    public static String exp(int exp) {
        String[] map = {"100K", "150K", "250K", "500K", "750K", "1M", "1.25M", "1.50M", "2M", "2.5M", "2.5M", "2.5M", "2.5M", "2.5M", "3M"};
        return map[exp];
    }
}
