package net.eduard.api.lib.config

import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.plugin.IPlugin
import net.eduard.api.lib.hybrid.Hybrid
import java.io.File
import java.lang.Exception


/**
 * Sistema Interprador de YAML usando a secao [ConfigSection]
 *
 * @author Eduard
 */
class Config(
    @Transient
    var folder: File,
    val name: String,
    @Transient
    private var plugin: Any? = null
) {

    @Transient
    var config: ConfigSection
    @Transient
    var file: File = File(folder, name)
    init {
        file.parentFile.mkdirs()
        config = ConfigSection("root", "")
        config.indent = 1
        reloadConfig()
    }

    constructor(plugin: IPlugin, name: String) : this(plugin.pluginFolder, name, plugin)
    constructor(plugin: Any, name: String) : this(plugin.dataFolder, name, plugin)



    companion object {
        private val Any.dataFolder: File
            get() {
                try {
                    return Extra.getMethodInvoke(this, "getDataFolder") as File
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return File("plugins/data/")
            }

        var isDebug = false

    }
    fun getIntList(path: String?): List<Int> {
        return config.getIntList(path!!)
    }

    fun log(msg: String) {
        if (isDebug)
            Hybrid.instance.console
                .sendMessage("§b[ConfigAPI] §3($name) §f$msg")
    }

    fun saveDefaultConfig() {
        file.parentFile.mkdirs()
        if (file.exists()) return
        if (Extra.isDirectory(file)) {
            log("IS A FOLDER")
            file.mkdirs()
            return
        }
        if (plugin == null) {
            log("PLUGIN NULL")
            return
        }
        try {
            log("COPYING DEFAULT...")
            val defaultResourceFile = Extra.getResource(plugin!!.javaClass.classLoader, name)
            if (defaultResourceFile == null) {
                log("DEFAULT NOT FOUNDED!")
                return
            }
            Extra.copyAsUTF8(defaultResourceFile, file)
            log("DEFAULT COPIED!")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun saveConfig() {
        log("SAVING...")
        try {
            if (Extra.isDirectory(file)) {
                log("NOT SAVED!")
                return
            }
            val lines = mutableListOf<String>()
            config.save(lines, -1)
            Extra.writeLines(file, lines)
            log("SAVED!")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun reloadConfig() {
        try {
            saveDefaultConfig()
            if (file.isFile && file.exists()) {
                log("RELOADING...")
                config.map.clear()
                config.reload(Extra.readLines(file))
                log("RELOADED!")
            } else {
                log("NOT RELOADED!")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    operator fun <E> get(clazz: Class<E>?): E {
        return config["", clazz]
    }

    operator fun <E> get(path: String, clazz: Class<E>?): E {
        return config[path, clazz]
    }

    fun add(path: String, value: Any?): ConfigSection {
        return config.add(path, value, *arrayOf())
    }

    fun add(path: String, value: Any?, vararg comments: String): ConfigSection {
        return config.add(path, value, *comments)
    }

    fun newConfig(name: String): Config {
        return createConfig(name)
    }

    fun addHeader(vararg header: String) {
        setHeader(*header)
    }

    operator fun contains(path: String): Boolean {
        return config.contains(path)
    }

    fun createConfig(name: String): Config {
        return Config(folder, name)
    }

    fun deleteConfig() {
        file.delete()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as Config
        if (name != other.name) return false
        return if (plugin == null) {
            other.plugin == null
        } else plugin == other.plugin
    }

    fun existConfig(): Boolean {
        return file.exists()
    }

    operator fun get(path: String): Any {
        return config[path]
    }

    fun getBoolean(path: String): Boolean {
        return config.getBoolean(path)
    }


    fun getDouble(path: String): Double {
        return config.getDouble(path)
    }

    fun getFloat(path: String): Float {
        return config.getFloat(path)
    }

    fun getInt(path: String): Int {
        return config.getInt(path)
    }

    val keys: Set<String>
        get() = config.keys

    fun getKeys(path: String): Set<String> {
        return config.getKeys(path)
    }

    fun getLong(path: String): Long {
        return config.getLong(path)
    }

    fun getMessages(path: String): List<String> {
        return config.getMessages(path)
    }

    fun getSection(path: String): ConfigSection {
        return config.getSection(path)
    }

    fun getString(path: String): String {
        return config.getString(path)
    }

    fun getStringList(path: String): List<String> {
        return config.getStringList(path)
    }

    val title: String
        get() = file.name.replace(".yml", "")

    fun getValues(path: String): Collection<ConfigSection> {
        return config.getValues(path)
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (name.hashCode() ?: 0)
        result = prime * result + if (plugin == null) 0 else plugin.hashCode()
        return result
    }

    fun message(path: String): String {
        return config.message(path)
    }

    fun remove(path: String) {
        config.remove(path)
    }

    fun set(value: Any?): ConfigSection {
        return set("", value)
    }

    operator fun set(path: String, value: Any?): ConfigSection {
        return config.set(path, value, *arrayOf())
    }

    fun set(path: String, value: Any?, vararg comments: String): ConfigSection {
        return config.set(path, value, *comments)
    }

    fun setHeader(vararg header: String) {
        config.comments.clear()
        config.comments.addAll(header)
    }

    override fun toString(): String {
        return "Config [plugin=$plugin, name=$name]"
    }

    fun setIndent(amount: Int) {
        config.indent = amount
    }
}