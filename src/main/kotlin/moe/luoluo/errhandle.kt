package moe.luoluo

import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import java.io.FileNotFoundException

fun errhandle(action: () -> MessageChain): MessageChain {
    try {
        val result = action()
        return result
    } catch (e: FileNotFoundException) {
        return MessageChainBuilder().append("玩家不存在").build()
    }
}
