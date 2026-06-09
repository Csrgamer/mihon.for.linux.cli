package com.csrgamer.mihon.cli.sources

import mu.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.jsoup.Jsoup

private val logger = KotlinLogging.logger {}

class MangaReaderSource : MangaSource() {
    override val name = "MangaReader"
    private val baseUrl = "https://mangareader.to"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching MangaReader for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/search?query=$query")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".manga-card").take(limit).map { element ->
                val link = element.selectFirst("a")
                val title = element.selectFirst(".manga-title")?.text() ?: "Unknown"
                val url = link?.attr("href") ?: return@map null
                val status = element.selectFirst(".manga-status")?.text() ?: "Ongoing"

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = status
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "MangaReader search failed" }
            emptyList()
        }
    }

    override suspend fun getChapters(mangaUrl: String): List<Chapter> {
        logger.info { "Fetching chapters from: $mangaUrl" }
        return try {
            val response = httpClient.get(mangaUrl)
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".chapter").map { element ->
                val link = element.selectFirst("a") ?: return@map null
                val chapter = element.selectFirst(".chapter-no")?.text()?.toFloatOrNull() ?: 0f
                val title = element.selectFirst(".chapter-title")?.text() ?: "Chapter $chapter"
                val url = link.attr("href")
                val date = element.selectFirst(".chapter-date")?.text() ?: "Unknown"

                Chapter(number = chapter, title = title, url = url, date = date)
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "Failed to fetch chapters" }
            emptyList()
        }
    }

    override suspend fun downloadChapter(chapterUrl: String, outputPath: String) {
        logger.info { "Downloading chapter from: $chapterUrl" }
        // TODO: Implement chapter download
    }
}