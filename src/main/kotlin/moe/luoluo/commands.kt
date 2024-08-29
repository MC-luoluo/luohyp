package moe.luoluo

import moe.luoluo.hypixel.*
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand


class KtCommands : CompositeCommand(
    luohyp.INSTANCE, "hypixel", "hyp",
    description = "查询玩家的Hypixel数据"
) {

    @SubCommand("player")
    @Description("查询玩家的Hypixel总体数据")
    suspend fun CommandSender.player(playerName: String, type: String = "") = sendMessage(
        message = Player.player(playerName)
    )

    /*fun player(context: CommandSender, player: String, type: String = "") {
        hypixel.hypixel(context,player, type)
    }*/
    @SubCommand("bedwars", "bw")
    @Description("查询玩家的起床战争数据")
    fun bw(context: CommandSender,player: String, type: String = "") {
        Bedwars.bedwars(context,player, type)
    }

    @SubCommand("skywars", "sw")
    @Description("查询玩家的空岛战争数据")
    suspend fun CommandSender.sw(player: String, type: String = "") {
        sendMessage(Skywars.skywars(player, type))
    }

    @SubCommand("tntgames", "tnt")
    @Description("查询玩家的掘战游戏数据")
    suspend fun CommandSender.tntgames(player: String, type: String = "") {
        sendMessage(TNT.tnt(player, type))
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

    @SubCommand("murdermystery","mm")
    @Description("查询玩家的密室杀手数据")
    fun mm(context: CommandSender, player: String, type: String = "") {
        MurderMystery.murdermystery(context, player, type)
    }

    @SubCommand("buildbattle","bb")
    @Description("查询玩家的建筑大师数据")
    fun bb(context: CommandSender, player: String, type: String = "") {
        BuildBattle.buildBattle(context, player)
    }

}