package moe.luoluo

import com.google.gson.JsonObject
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import net.mamoe.mirai.console.data.PluginDataStorage
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

object Data : AutoSavePluginData("data") {
    @OptIn(ConsoleExperimentalApi::class)
    private lateinit var owner_: PluginDataHolder
    @OptIn(ConsoleExperimentalApi::class)
    private lateinit var storage_: PluginDataStorage
    @OptIn(ConsoleExperimentalApi::class)
    override fun onInit(owner: PluginDataHolder, storage: PluginDataStorage) {
        owner_ = owner
        storage_ = storage
    }
    @JvmStatic
    @OptIn(ConsoleExperimentalApi::class)
    private fun save() {
        kotlin.runCatching {
            storage_.store(owner_, this)
        }.onFailure { e ->
            // 处理异常
        }
    }
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
        HypixelData.getOrPut(uuid) { mutableMapOf() }
        val uuidMap = HypixelData.getOrPut(uuid) { mutableMapOf() }
        uuidMap[type] = (System.currentTimeMillis() to json.toString())
        save()
//        HypixelData[uuid]?.put(type,(System.currentTimeMillis() to json.toString()))
    }
}
