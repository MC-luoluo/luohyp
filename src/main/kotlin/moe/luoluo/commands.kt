package moe.luoluo

import moe.luoluo.hypixel.*
import moe.luoluo.mcskin.MCskin
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.SimpleCommand


class Hypixel : CompositeCommand(
    luohyp.INSTANCE, "hypixel", "hyp",
    description = "查询玩家的Hypixel数据"
) {

    @SubCommand("arcade", "arc")
    @Description("查询玩家的街机游戏数据")
    fun arcade(context: CommandSender, player: String, type: String = "") {
        Arcade.arc(context, player, type)
    }

    @SubCommand("bw")
    @Description("查询玩家的起床战争数据")
    fun bw(context: CommandSender, player: String, type: String = "") {
        Bedwars.bedwars(context, player, type)
    }

    @SubCommand("bb")
    @Description("查询玩家的建筑大师数据")
    suspend fun CommandSender.bb(player: String) {
        sendMessage(BuildBattle.buildBattle(player))
    }

    @SubCommand("cac", "cvc")
    @Description("查询玩家的警匪大战数据")
    fun cac(context: CommandSender, player: String, type: String = "") {
        CAC.mcgo(context, player, type)
    }

    @SubCommand("duels", "duel")
    @Description("查询玩家的决斗游戏数据")
    fun duel(context: CommandSender, player: String, type: String = "") {
        Duels.duels(context, player, type)
    }

    @SubCommand("dpr")
    @Description("查询玩家的心跳水立方数据")
    fun dpr(context: CommandSender, player: String) {
        Arcade.arc(context, player, "dpr")
    }

    @SubCommand("fish")
    @Description("查询玩家的钓鱼数据")
    suspend fun CommandSender.fish(player: String) {
        sendMessage(Fish.fish(player))
    }

    @SubCommand("guild")
    @Description("查询玩家的公会数据")
    fun guild(context: CommandSender, player: String, type: String = "") {
        Guild.guild(context, "player", player, type)
    }

    @SubCommand("guildname")
    @Description("使用公会名称查询公会数据，带空格名称需使用英文双引号包括")
    fun guildName(context: CommandSender, player: String, type: String = "") {
        Guild.guild(context, "name", player, type)
    }

    @SubCommand("guildid")
    @Description("使用公会id查询公会数据")
    fun guildId(context: CommandSender, player: String, type: String = "") {
        Guild.guild(context, "id", player, type)
    }

    @SubCommand("list")
    @Description("查询各游戏玩家计数")
    fun list(context: CommandSender, type: String = "") {
        Counts.playerList(context, type)
    }

    @SubCommand("mm")
    @Description("查询玩家的密室杀手数据")
    fun mm(context: CommandSender, player: String, type: String = "") {
        MurderMystery.murdermystery(context, player, type)
    }

    @SubCommand("player")
    @Description("查询玩家的Hypixel总体数据")
    fun player(context: CommandSender, playerName: String, type: String = "") {
        Player.player(context, playerName, type)
    }

    @SubCommand("sheep")
    @Description("查询玩家的绵羊战争数据")
    fun sheep(context: CommandSender, player: String) {
        WoolGames.wool(context, player, "sheep")
    }

    @SubCommand("sw")
    @Description("查询玩家的空岛战争数据")
    suspend fun CommandSender.sw(player: String) {
        sendMessage(Skywars.skywars(player))
    }

    @SubCommand("tkr")
    @Description("查询玩家的方块赛车数据")
    suspend fun CommandSender.turbokartracers(player: String) {
        sendMessage(TurboKartRacers.GingerBread(player))
    }

    @SubCommand("tnt")
    @Description("查询玩家的掘战游戏数据")
    suspend fun CommandSender.tntgames(player: String) {
        sendMessage(TNT.tntgames(player))
    }

    @SubCommand("tournament", "tourney")
    @Description("查询玩家的锦标赛数据")
    fun tourney(context: CommandSender, player: String, type: String = "") {
        Tournament.tourney(context, player, type)
    }

    @SubCommand("wool", "wg")
    @Description("查询玩家的羊毛游戏数据")
    fun wg(context: CommandSender, player: String, type: String = "") {
        WoolGames.wool(context, player, type)
    }

    @SubCommand("wl")
    @Description("查询玩家的战争领主数据")
    suspend fun CommandSender.warlords(player: String) {
        sendMessage(Warlords.Battleground(player))
    }

    @SubCommand("zb")
    @Description("查询玩家的僵尸末日数据")
    fun zb(context: CommandSender, player: String) {
        Arcade.arc(context, player, "zb")
    }
}

class MCSkin : SimpleCommand(
    luohyp.INSTANCE, "mcskin","mc",
    description = "获取玩家的skin文件"
) {
    @Handler
    fun mcskin(context: CommandSender, player: String) {
        MCskin.mcskin(context, player)
    }
}
