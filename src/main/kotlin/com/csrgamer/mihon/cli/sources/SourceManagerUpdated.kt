package com.csrgamer.mihon.cli.sources

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class SourceManager {
    private val sources: List<MangaSource> = listOf(
        // Original 3
        MangadexSource(),
        AsuraScansSource(),
        ManhuaCalfSource(),
        // First 4 additions
        MangaReaderSource(),
        MangaKakalotSource(),
        BatoSource(),
        ScanSource(),
        ChainSource(),
        // Popular international sources
        CrunchyrollSource(),
        TapasSource(),
        WebtoonSource(),
        MangaPlusSource(),
        ComicWalkerSource(),
        KissMangaSource(),
        // Alternative sources
        ReadComicsSource(),
        WeebCentralSource(),
        MangaHubSource(),
        JenovaStormSource(),
        ManwhaSource()
    )

    suspend fun searchAllSources(query: String, limit: Int): List<Manga> {
        logger.info { "Searching all ${sources.size} sources for: $query" }
        val results = mutableListOf<Manga>()

        sources.forEach { source ->
            try {
                val sourceResults = source.search(query, limit)
                results.addAll(sourceResults)
                logger.info { "${source.name}: ${sourceResults.size} results" }
            } catch (e: Exception) {
                logger.warn(e) { "Search failed for source: ${source.name}" }
            }
        }

        return results.take(limit)
    }

    suspend fun searchSource(sourceName: String, query: String, limit: Int): List<Manga> {
        logger.info { "Searching $sourceName for: $query" }
        val source = sources.find { it.name.equals(sourceName, ignoreCase = true) }
            ?: throw IllegalArgumentException("Source not found: $sourceName. Available: ${listSources().joinToString(", ")}")

        return source.search(query, limit)
    }

    fun listSources(): List<String> = sources.map { it.name }.sorted()

    fun getSourceCount(): Int = sources.size
}