package com.csrgamer.mihon.cli.sources

import mu.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.jsoup.Jsoup

private val logger = KotlinLogging.logger {}

class ScanSource : MangaSource() {
    override val name = "ManHuaScan"
    private val baseUrl = "https://manhuascan.com"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching ManHuaScan for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/?s=$query&post_type=wp-manga")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select("div.c-tabs-item__content").take(limit).map { element ->
                val link = element.selectFirst("h3 a")
                val title = link?.text() ?: "Unknown"
                val url = link?.attr("href") ?: return@map null

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = "Ongoing"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "ManHuaScan search failed" }
            emptyList()
        }
    }

    override suspend fun getChapters(mangaUrl: String): List<Chapter> {
        logger.info { "Fetching chapters from: $mangaUrl" }
        // TODO: Implement chapter fetching
        return emptyList()
    }

    override suspend fun downloadChapter(chapterUrl: String, outputPath: String) {
        logger.info { "Downloading chapter from: $chapterUrl" }
        // TODO: Implement chapter download
    }
}