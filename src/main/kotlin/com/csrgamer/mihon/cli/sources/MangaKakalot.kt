package com.csrgamer.mihon.cli.sources

import mu.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.jsoup.Jsoup

private val logger = KotlinLogging.logger {}

class MangaKakalotSource : MangaSource() {
    override val name = "MangaKakalot"
    private val baseUrl = "https://mangakakalot.com"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching MangaKakalot for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/search/story/$query")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".story_item").take(limit).map { element ->
                val link = element.selectFirst("a")
                val title = element.selectFirst(".story_name a")?.text() ?: "Unknown"
                val url = link?.attr("href") ?: return@map null

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = "Ongoing"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "MangaKakalot search failed" }
            emptyList()
        }
    }

    override suspend fun getChapters(mangaUrl: String): List<Chapter> {
        logger.info { "Fetching chapters from: $mangaUrl" }
        return try {
            val response = httpClient.get(mangaUrl)
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select("a.chapter").mapNotNull { element ->
                val url = element.attr("href") ?: return@mapNotNull null
                val text = element.text()
                val chapterNum = text.substringAfter("Chapter ").substringBefore(" ").toFloatOrNull() ?: 0f

                Chapter(
                    number = chapterNum,
                    title = text,
                    url = url,
                    date = "Unknown"
                )
            }.reversed()
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