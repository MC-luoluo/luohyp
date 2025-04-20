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

    private var HypixelData: MutableMap<String, MutableMap<String, Pair<Long, String>>> by value()

    //保存：将目前已添加数据写入文件
    @JvmStatic
    @OptIn(ConsoleExperimentalApi::class)
    private fun save() {
        kotlin.runCatching {
            storage_.store(owner_, this)
        }.onFailure { e ->
            // 处理异常
        }
    }

    //获取：从文件读取数据
    @JvmStatic
    fun getHypixelData(type: String, uuid: String): String {
        //获取uuid对应的map
        val idMap = HypixelData[uuid]
        //失败返回"null"
        idMap ?: return "null"
        //尝试获取type对应的Pair
        val typePair = idMap[type]
        //失败返回"null"
        typePair ?: return "null"
        //找到返回Pair的第二项(JSON)
        return typePair.second
    }

    @JvmStatic
    fun getHypixelDataTime(type: String, uuid: String): String {
        val idMap = HypixelData[uuid]
        idMap ?: return "null"
        val typePair = idMap[type]
        typePair ?: return "null"
        return typePair.first.toString()
    }

    //设置：存储该玩家的新数据
    @JvmStatic
    fun setHypixelData(type: String, uuid: String, json: JsonObject) {
        //获取uuid对应的map，没有就创建
        val uuidMap = HypixelData.getOrPut(uuid) { mutableMapOf() }
        //将type和当前时间戳和JSON存入Map
        uuidMap[type] = (System.currentTimeMillis() to json.toString())
        HypixelData[uuid] = uuidMap
        save()
    }

    //清空：清除所有缓存数据
    @JvmStatic
    fun resetData() {
        HypixelData.clear()
    }
}
