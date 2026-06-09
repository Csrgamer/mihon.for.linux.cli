package com.csrgamer.mihon.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.flag
import mu.KotlinLogging
import com.csrgamer.mihon.cli.db.DatabaseManager

private val logger = KotlinLogging.logger {}

class ListCommand : CliktCommand(
    name = "list",
    help = "List downloaded or saved manga"
) {
    private val showAll: Boolean by option("--all", "-a", help = "Show all manga including completed")
        .flag(default = false)
    private val sort: String by option("--sort", "-s", help = "Sort by: name, date, chapters")
        .default("date")

    override fun run() {
        logger.info { "Listing manga" }
        
        try {
            val dbManager = DatabaseManager()
            val manga = dbManager.getAllManga()

            if (manga.isEmpty()) {
                echo("No manga found in library")
                return
            }

            echo("\n📚 Your Manga Library:\n")
            echo("%-30s | %-10s | %-20s".format("Title", "Chapters", "Last Updated"))
            echo("-".repeat(65))

            manga.forEach { m ->
                echo("%-30s | %-10s | %-20s".format(
                    m.title.take(30),
                    m.chapterCount,
                    m.lastUpdated
                ))
            }

            echo("\nTotal: ${manga.size} manga")
        } catch (e: Exception) {
            logger.error(e) { "List failed" }
            echo("Error: ${e.message}")
        }
    }
}