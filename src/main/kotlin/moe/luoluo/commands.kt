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
        message = player.player(playerName, type)
    )

    /*fun player(context: CommandSender, player: String, type: String = "") {
        hypixel.hypixel(context,player, type)
    }*/
    @SubCommand("bedwars", "bedwar", "bw")
    @Description("查询玩家的起床战争数据")
    fun bw(context: CommandSender,player: String, type: String = "") {
        bedwars.bedwars(context,player, type)
    }

    @SubCommand("skywars", "skywar", "sw")
    @Description("查询玩家的空岛战争数据")
    suspend fun CommandSender.sw(player: String, type: String = "") {
        sendMessage(skywars.skywars(player, type))
    }

    @SubCommand("tntgames", "tntgame", "tnt")
    @Description("查询玩家的掘战游戏数据")
    suspend fun CommandSender.tntgames(player: String, type: String = "") {
        sendMessage(tnt.tnt(player, type))
    }

    @SubCommand("guild")
    @Description("查询玩家的公会数据")
    fun guild(context: CommandSender, player: String, type: String = "") {
        guild.guild(context, "player", player, type)
    }

    @SubCommand("guildname")
    @Description("使用公会名称查询公会数据，带空格公会名称需要使用英文双引号或\\转义")
    fun guildName(context: CommandSender, player: String, type: String = "") {
        guild.guild(context, "name", player, type)
    }

    @SubCommand("guildid")
    @Description("使用公会id查询公会数据")
    fun guildId(context: CommandSender, player: String, type: String = "") {
        guild.guild(context, "id", player, type)
    }

}