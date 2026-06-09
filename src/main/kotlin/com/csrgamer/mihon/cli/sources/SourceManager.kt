package com.csrgamer.mihon.cli.sources

import mu.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

private val logger = KotlinLogging.logger {}

data class Manga(
    val title: String,
    val url: String,
    val source: String,
    val status: String,
    val cover: String? = null
)

class SourceManager {
    private val httpClient = HttpClient()
    private val sources: List<MangaSource> = listOf(
        MangadexSource(),
        AsuraScansSource(),
        ManhuaCalfSource()
    )

    suspend fun searchAllSources(query: String, limit: Int): List<Manga> {
        logger.info { "Searching all sources for: $query" }
        val results = mutableListOf<Manga>()

        sources.forEach { source ->
            try {
                val sourceResults = source.search(query, limit)
                results.addAll(sourceResults)
            } catch (e: Exception) {
                logger.warn(e) { "Search failed for source: ${source.name}" }
            }
        }

        return results.take(limit)
    }

    suspend fun searchSource(sourceName: String, query: String, limit: Int): List<Manga> {
        logger.info { "Searching $sourceName for: $query" }
        val source = sources.find { it.name.equals(sourceName, ignoreCase = true) }
            ?: throw IllegalArgumentException("Source not found: $sourceName")

        return source.search(query, limit)
    }

    fun listSources(): List<String> = sources.map { it.name }
}

abstract class MangaSource {
    abstract val name: String
    abstract suspend fun search(query: String, limit: Int): List<Manga>
    abstract suspend fun getChapters(mangaUrl: String): List<Chapter>
    abstract suspend fun downloadChapter(chapterUrl: String, outputPath: String)
}

data class Chapter(
    val number: Float,
    val title: String,
    val url: String,
    val date: String
)