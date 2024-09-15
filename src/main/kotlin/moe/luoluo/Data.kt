package moe.luoluo

import com.google.gson.JsonObject
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object Data : AutoSavePluginData("data") {
    private val HypixelData: MutableMap<String, MutableMap<String, Pair<Long, String>>> by value()

    @JvmStatic
    fun getHypixelData(type: String, uuid: String): String {
        val idMap = HypixelData[uuid]
        idMap ?: return "null"
        val typePair = idMap[type]
        typePair ?: return "null"
        return typePair.second
    }

    @JvmStatic
    fun setHypixelData(type: String, uuid: String, json: JsonObject) {
        val uuidMap = HypixelData.getOrPut(uuid) { mutableMapOf() }
        uuidMap[type] = (System.currentTimeMillis() to json.toString())
//        HypixelData[uuid]?.put(type,(System.currentTimeMillis() to json.toString()))
    }
}
