package moe.luoluo

import moe.luoluo.hypixel.bedwars
import moe.luoluo.hypixel.player
import moe.luoluo.hypixel.tnt
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.message.data.ForwardMessage


class KtCommands : CompositeCommand(
    luohyp.INSTANCE, "hypixel","hyp",
    description = "hypixel command"
) {

    @SubCommand("player")
    @Description("查询玩家的Hypixel总体数据")
    suspend fun CommandSender.player(playerName: String, type: String = "") = sendMessage(
        message = player.player(playerName, type)
    )
    /*fun player(context: CommandSender, player: String, type: String = "") {
        hypixel.hypixel(context,player, type)
    }*/
    @SubCommand("bedwars","bedwar","bw")
    @Description("查询玩家的起床战争数据")
    suspend fun CommandSender.bw(player: String, type: String = "") {
        sendMessage(bedwars.bedwars(player,type))
        //sendMessage(ForwardMessage(test.test1(),"title","brief","source","summary",test.test2()))
    }
    @SubCommand("skywars","skywar","sw")
    @Description("查询玩家的空岛战争数据")
    suspend fun CommandSender.sw(player: String, type: String = "") {
        sendMessage(bedwars.bedwars(player,type))
        //sendMessage(ForwardMessage(test.test1(),"title","brief","source","summary",test.test2()))
    }
    @SubCommand("tntgames","tntgame","tnt")
    @Description("查询玩家的空岛战争数据")
    suspend fun CommandSender.tntgames(player: String, type: String = "") {
        sendMessage(tnt.tnt(player,type))
        //sendMessage(ForwardMessage(test.test1(),"title","brief","source","summary",test.test2()))
    }

}