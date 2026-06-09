package com.csrgamer.mihon.cli.config

import mu.KotlinLogging
import java.io.File
import java.util.Properties

private val logger = KotlinLogging.logger {}

class ConfigManager {
    private val configPath = "${System.getProperty("user.home")}/.mihon/config.properties"
    private val properties = Properties()

    init {
        loadConfig()
    }

    private fun loadConfig() {
        val file = File(configPath)
        file.parentFile?.mkdirs()

        if (file.exists()) {
            file.inputStream().use { properties.load(it) }
            logger.info { "Loaded config from: $configPath" }
        } else {
            // Set defaults
            properties["download_dir"] = "./manga"
            properties["threads"] = "3"
            properties["image_quality"] = "high"
            properties["auto_update"] = "false"
            saveConfig()
        }
    }

    private fun saveConfig() {
        File(configPath).parentFile?.mkdirs()
        File(configPath).outputStream().use { properties.store(it, "Mihon CLI Configuration") }
    }

    fun get(key: String): String? = properties.getProperty(key)

    fun set(key: String, value: String) {
        properties[key] = value
        saveConfig()
        logger.info { "Config updated: $key = $value" }
    }

    fun getAll(): Map<String, String> = properties.toMap() as Map<String, String>

    fun getInt(key: String, default: Int = 0): Int = get(key)?.toIntOrNull() ?: default

    fun getBoolean(key: String, default: Boolean = false): Boolean = 
        get(key)?.toBoolean() ?: default
}