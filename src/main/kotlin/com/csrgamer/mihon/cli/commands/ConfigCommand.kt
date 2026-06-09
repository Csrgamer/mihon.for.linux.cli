package com.csrgamer.mihon.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.option
import mu.KotlinLogging
import com.csrgamer.mihon.cli.config.ConfigManager

private val logger = KotlinLogging.logger {}

class ConfigCommand : CliktCommand(
    name = "config",
    help = "Manage configuration settings"
) {
    private val key: String? by argument(help = "Configuration key").optional()
    private val value: String? by argument(help = "Configuration value").optional()

    override fun run() {
        logger.info { "Managing config" }
        
        try {
            val configManager = ConfigManager()

            when {
                key == null -> {
                    // Show all config
                    echo("\n⚙️  Current Configuration:\n")
                    val config = configManager.getAll()
                    config.forEach { (k, v) ->
                        echo("$k = $v")
                    }
                }
                value == null -> {
                    // Show specific config
                    val v = configManager.get(key!!)
                    echo("$key = $v")
                }
                else -> {
                    // Set config
                    configManager.set(key!!, value!!)
                    echo("✅ Configuration updated: $key = $value")
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "Config failed" }
            echo("Error: ${e.message}")
        }
    }
}