package moe.luoluo

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value


object config : AutoSavePluginConfig("config") {
    @ValueDescription("填写Hypixel API key，可在'https://developer.hypixel.net/dashboard'获取")
    var HypixelAPIkey: String by value("")
}
