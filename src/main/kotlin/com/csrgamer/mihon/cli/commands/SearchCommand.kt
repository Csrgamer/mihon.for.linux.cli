package com.csrgamer.mihon.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.default
import mu.KotlinLogging
import com.csrgamer.mihon.cli.sources.SourceManager

private val logger = KotlinLogging.logger {}

class SearchCommand : CliktCommand(
    name = "search",
    help = "Search for manga across available sources"
) {
    private val query: String by argument(help = "Manga title to search for")
    private val source: String by option("--source", "-s", help = "Specific source to search (optional)")
        .default("all")
    private val limit: Int by option("--limit", "-l", help = "Limit number of results")
        .default(20)

    override fun run() {
        logger.info { "Searching for manga: $query" }
        
        try {
            val sourceManager = SourceManager()
            val results = if (source == "all") {
                sourceManager.searchAllSources(query, limit)
            } else {
                sourceManager.searchSource(source, query, limit)
            }

            if (results.isEmpty()) {
                echo("No manga found for: $query")
                return
            }

            echo("\n📚 Search Results ($query):\n")
            results.forEachIndexed { index, manga ->
                echo("[$index] ${manga.title}")
                echo("    Source: ${manga.source}")
                echo("    URL: ${manga.url}")
                echo("    Status: ${manga.status}")
                echo()
            }
        } catch (e: Exception) {
            logger.error(e) { "Search failed" }
            echo("Error: ${e.message}")
        }
    }
}