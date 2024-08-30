package moe.luoluo.hypixel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import moe.luoluo.Api;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;
import java.net.URISyntaxException;

public class Fish {
    public static MessageChain fish(String player) throws IOException, URISyntaxException {
        JsonObject json = Api.hypixel("player", Api.mojang(player, "uuid"));
        MessageChainBuilder chain = new MessageChainBuilder();
        /*
        MessageChainBuilder all = new MessageChainBuilder();
        MessageChainBuilder water = new MessageChainBuilder();
        MessageChainBuilder lava = new MessageChainBuilder();
        MessageChainBuilder ice = new MessageChainBuilder();*/

        //判断是否存在fishing数据
        if (json.get("player").getAsJsonObject().has("stats") &&
                json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("MainLobby") &&
                json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("MainLobby").getAsJsonObject().has("fishing")) {
            JsonObject playerJson = json.get("player").getAsJsonObject();
            JsonObject fishJson = playerJson.get("stats").getAsJsonObject().get("MainLobby").getAsJsonObject().get("fishing").getAsJsonObject();

            chain.append(new PlainText(Rank.rank(playerJson) + " "));
            chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
            chain.append(new PlainText(" | 大厅钓鱼数据:"));
            if (fishJson.has("stats") && fishJson.get("stats").getAsJsonObject().has("permanent")) {
                JsonObject permanent = fishJson.get("stats").getAsJsonObject().get("permanent").getAsJsonObject();
                chain.append(new PlainText("\n钓到鱼: "));
                int fish = 0;
                if (permanent.has("water") && permanent.get("water").getAsJsonObject().has("fish")) {
                    fish += permanent.get("water").getAsJsonObject().get("fish").getAsInt();
                }
                if (permanent.has("ice") && permanent.get("ice").getAsJsonObject().has("fish")) {
                    fish += permanent.get("ice").getAsJsonObject().get("fish").getAsInt();
                }
                if (permanent.has("lava") && permanent.get("lava").getAsJsonObject().has("fish")) {
                    fish += permanent.get("lava").getAsJsonObject().get("fish").getAsInt();
                }
                chain.append(new PlainText(String.valueOf(fish)));
                chain.append(new PlainText("\n钓到宝藏: "));
                int treasure = 0;
                if (permanent.has("water") && permanent.get("water").getAsJsonObject().has("treasure")) {
                    treasure += permanent.get("water").getAsJsonObject().get("treasure").getAsInt();
                }
                if (permanent.has("ice") && permanent.get("ice").getAsJsonObject().has("treasure")) {
                    treasure += permanent.get("ice").getAsJsonObject().get("treasure").getAsInt();
                }
                if (permanent.has("lava") && permanent.get("lava").getAsJsonObject().has("treasure")) {
                    treasure += permanent.get("lava").getAsJsonObject().get("treasure").getAsInt();
                }
                chain.append(new PlainText(String.valueOf(treasure)));
                chain.append(new PlainText("\n钓到垃圾: "));
                int junk = 0;
                if (permanent.has("water") && permanent.get("water").getAsJsonObject().has("junk")) {
                    junk += permanent.get("water").getAsJsonObject().get("junk").getAsInt();
                }
                if (permanent.has("ice") && permanent.get("ice").getAsJsonObject().has("junk")) {
                    junk += permanent.get("ice").getAsJsonObject().get("junk").getAsInt();
                }
                if (permanent.has("lava") && permanent.get("lava").getAsJsonObject().has("junk")) {
                    junk += permanent.get("lava").getAsJsonObject().get("junk").getAsInt();
                }
                chain.append(new PlainText(String.valueOf(junk)));
            }
            if (fishJson.has("orbs")) {
                chain.append(new PlainText("\n钓到神话鱼: "));
                int orb = 0;
                if (fishJson.get("orbs").getAsJsonObject().has("helios")) {
                    orb += fishJson.get("orbs").getAsJsonObject().get("helios").getAsInt();
                    chain.append(new PlainText("\n  Helios " + fishJson.get("orbs").getAsJsonObject().get("helios").getAsInt()));
                    if (weight(fishJson, "helios") > 0) {
                        chain.append(new PlainText(" | 最重" + weight(fishJson, "helios") + "kg"));
                    }
                }
                if (fishJson.get("orbs").getAsJsonObject().has("selene")) {
                    orb += fishJson.get("orbs").getAsJsonObject().get("selene").getAsInt();
                    chain.append(new PlainText("\n  Selene " + fishJson.get("orbs").getAsJsonObject().get("selene").getAsInt()));
                    if (weight(fishJson, "selene") > 0) {
                        chain.append(new PlainText(" | 最重" + weight(fishJson, "selene") + "kg"));
                    }
                }
                if (fishJson.get("orbs").getAsJsonObject().has("nyx")) {
                    orb += fishJson.get("orbs").getAsJsonObject().get("nyx").getAsInt();
                    chain.append(new PlainText("\n  Nyx " + fishJson.get("orbs").getAsJsonObject().get("nyx").getAsInt()));
                    if (weight(fishJson, "nyx") > 0) {
                        chain.append(new PlainText(" | 最重" + weight(fishJson, "nyx") + "kg"));
                    }
                }
                if (fishJson.get("orbs").getAsJsonObject().has("aphrodite")) {
                    orb += fishJson.get("orbs").getAsJsonObject().get("aphrodite").getAsInt();
                    chain.append(new PlainText("\n  Aphrodite " + fishJson.get("orbs").getAsJsonObject().get("aphrodite").getAsInt()));
                    if (weight(fishJson, "aphrodite") > 0) {
                        chain.append(new PlainText(" | 最重" + weight(fishJson, "aphrodite") + "kg"));
                    }
                }
                if (fishJson.get("orbs").getAsJsonObject().has("zeus")) {
                    orb += fishJson.get("orbs").getAsJsonObject().get("zeus").getAsInt();
                    chain.append(new PlainText("\n  Zeus " + fishJson.get("orbs").getAsJsonObject().get("zeus").getAsInt()));
                    if (weight(fishJson, "zeus") > 0) {
                        chain.append(new PlainText(" | 最重" + weight(fishJson, "zeus") + "kg"));
                    }
                }
                if (fishJson.get("orbs").getAsJsonObject().has("archimedes")) {
                    orb += fishJson.get("orbs").getAsJsonObject().get("archimedes").getAsInt();
                    chain.append(new PlainText("\n  Daedalus " + fishJson.get("orbs").getAsJsonObject().get("archimedes").getAsInt()));
                    if (weight(fishJson, "archimedes") > 0) {
                        chain.append(new PlainText(" | 最重" + weight(fishJson, "archimedes") + "kg"));
                    }
                }
                if (fishJson.get("orbs").getAsJsonObject().has("hades")) {
                    orb += fishJson.get("orbs").getAsJsonObject().get("hades").getAsInt();
                    chain.append(new PlainText("\n  Hades " + fishJson.get("orbs").getAsJsonObject().get("hades").getAsInt()));
                    if (weight(fishJson, "hades") > 0) {
                        chain.append(new PlainText(" | 最重" + weight(fishJson, "hades") + "kg"));
                    }
                }
                chain.append(new PlainText("\n共: " + orb));
            }
            if (fishJson.has("enchants")) {
                JsonObject enchants = fishJson.get("enchants").getAsJsonObject();
                chain.append(new PlainText("\n钓鱼竿附魔: "));
                if (enchants.has("lure")) {
                    chain.append(new PlainText(" 饵钓"));
                    chain.append(new PlainText(String.valueOf(ecLevel(enchants, "lure"))));
                    if (Boolean.FALSE.equals(toggle(enchants, "lure"))) {
                        chain.append(new PlainText(" (未启用) |"));
                    }
                }
                if (enchants.has("luck")) {
                    chain.append(new PlainText(" 海之眷顾"));
                    chain.append(new PlainText(String.valueOf(ecLevel(enchants, "luck"))));
                    if (Boolean.FALSE.equals(toggle(enchants, "luck"))) {
                        chain.append(new PlainText(" (未启用) |"));
                    }
                }
                if (enchants.has("collector")) {
                    chain.append(new PlainText(" 收藏家"));
                    chain.append(new PlainText(String.valueOf(ecLevel(enchants, "collector"))));
                    if (Boolean.FALSE.equals(toggle(enchants, "collector"))) {
                        chain.append(new PlainText(" (未启用) |"));
                    }
                }
                if (enchants.has("dumpster_diver")) {
                    chain.append(new PlainText(" 水域环卫工"));
                    chain.append(new PlainText(String.valueOf(ecLevel(enchants, "dumpster_diver"))));
                    if (Boolean.FALSE.equals(toggle(enchants, "dumpster_diver"))) {
                        chain.append(new PlainText(" (未启用) |"));
                    }
                }
                if (enchants.has("vulcans_blessing")) {
                    chain.append(new PlainText(" 火神祝福"));
                    chain.append(new PlainText(String.valueOf(ecLevel(enchants, "vulcans_blessing"))));
                    if (Boolean.FALSE.equals(toggle(enchants, "vulcans_blessing"))) {
                        chain.append(new PlainText(" (未启用) |"));
                    }
                }
                if (enchants.has("neptunes_fury")) {
                    chain.append(new PlainText(" 海神之怒"));
                    chain.append(new PlainText(String.valueOf(ecLevel(enchants, "neptunes_fury"))));
                    if (Boolean.FALSE.equals(toggle(enchants, "neptunes_fury"))) {
                        chain.append(new PlainText(" (未启用) |"));
                    }
                }
                if (enchants.has("mythical_hook")) {
                    chain.append(new PlainText(" 神话鱼钩"));
                    chain.append(new PlainText(String.valueOf(ecLevel(enchants, "mythical_hook"))));
                    if (Boolean.FALSE.equals(toggle(enchants, "mythical_hook"))) {
                        chain.append(new PlainText(" (未启用) |"));
                    }
                }
                if (enchants.has("herbivore")) {
                    chain.append(new PlainText(" herbivore"));
                    chain.append(new PlainText(String.valueOf(ecLevel(enchants, "herbivore"))));
                    if (Boolean.FALSE.equals(toggle(enchants, "herbivore"))) {
                        chain.append(new PlainText(" (未启用)"));
                    }
                }
            }
        } else {
            chain.append(new PlainText(" 该玩家的钓鱼数据为空"));
            //group.sendMessage(chain.build());
        }
        return chain.build();
    }

    public static Boolean toggle(JsonObject json, String str) {
        if (json.get(str).getAsJsonObject().has("toggle")) {
            return json.get(str).getAsJsonObject().get("toggle").getAsBoolean();
        }
        return true;
    }

    public static int weight(JsonObject json, String name) {
        if (json.get("orbs").getAsJsonObject().get("weight").getAsJsonObject().has(name)) {
            return json.get("orbs").getAsJsonObject().get("weight").getAsJsonObject().get(name).getAsInt();
        }
        return 0;
    }

    public static int ecLevel(JsonObject json, String name) {
        if (json.get(name).getAsJsonObject().has("level")) {
            return json.get(name).getAsJsonObject().get("level").getAsInt();
        }
        return 0;
    }
}
