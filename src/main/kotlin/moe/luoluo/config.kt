package moe.luoluo

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value


object Config : AutoSavePluginConfig("config") { // 文件名为 MyData, 会被保存为 MyData.yml
    @ValueDescription("填写HypixelAPI，可在'https://developer.hypixel.net/dashboard'获取")
    var HypixelAPI: String by value("")
}
