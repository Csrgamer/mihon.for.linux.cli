package com.csrgamer.mihon.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.flag
import mu.KotlinLogging
import com.csrgamer.mihon.cli.db.DatabaseManager

private val logger = KotlinLogging.logger {}

class UpdateCommand : CliktCommand(
    name = "update",
    help = "Check for new chapters of saved manga"
) {
    private val all: Boolean by option("--all", "-a", help = "Update all saved manga")
        .flag(default = false)
    private val manga: String? by option("--manga", "-m", help = "Specific manga to update")

    override fun run() {
        logger.info { "Updating manga" }
        
        try {
            val dbManager = DatabaseManager()

            if (all) {
                echo("🔄 Updating all manga...")
                val manga = dbManager.getAllManga()
                manga.forEach { m ->
                    echo("Checking: ${m.title}")
                    // TODO: Implement update logic
                }
            } else if (manga != null) {
                echo("🔄 Updating: $manga")
                // TODO: Implement update logic for specific manga
            } else {
                echo("Use --all to update all manga or --manga <title> to update specific manga")
            }

            echo("✅ Update check completed!")
        } catch (e: Exception) {
            logger.error(e) { "Update failed" }
            echo("Error: ${e.message}")
        }
    }
}