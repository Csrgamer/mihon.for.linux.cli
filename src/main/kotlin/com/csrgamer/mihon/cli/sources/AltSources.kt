package com.csrgamer.mihon.cli.sources

import mu.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.jsoup.Jsoup

private val logger = KotlinLogging.logger {}

// ReadComics Source
class ReadComicsSource : MangaSource() {
    override val name = "ReadComics"
    private val baseUrl = "https://readcomics.app"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching ReadComics for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/search?q=$query")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".comic-item").take(limit).map { element ->
                val link = element.selectFirst("a")
                val title = element.selectFirst(".comic-title")?.text() ?: "Unknown"
                val url = link?.attr("href") ?: return@map null

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = "Ongoing"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "ReadComics search failed" }
            emptyList()
        }
    }

    override suspend fun getChapters(mangaUrl: String): List<Chapter> {
        logger.info { "Fetching chapters from: $mangaUrl" }
        return emptyList()
    }

    override suspend fun downloadChapter(chapterUrl: String, outputPath: String) {
        logger.info { "Downloading chapter from: $chapterUrl" }
    }
}

// Weeb Central Source
class WeebCentralSource : MangaSource() {
    override val name = "WeebCentral"
    private val baseUrl = "https://weebcentral.com"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching WeebCentral for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/search?query=$query")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".series").take(limit).map { element ->
                val link = element.selectFirst("a")
                val title = element.selectFirst(".name")?.text() ?: "Unknown"
                val url = link?.attr("href") ?: return@map null

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = "Ongoing"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "WeebCentral search failed" }
            emptyList()
        }
    }

    override suspend fun getChapters(mangaUrl: String): List<Chapter> {
        logger.info { "Fetching chapters from: $mangaUrl" }
        return emptyList()
    }

    override suspend fun downloadChapter(chapterUrl: String, outputPath: String) {
        logger.info { "Downloading chapter from: $chapterUrl" }
    }
}

// MangaHub Source
class MangaHubSource : MangaSource() {
    override val name = "MangaHub"
    private val baseUrl = "https://mangahub.io"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching MangaHub for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/search?q=$query")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".manga").take(limit).map { element ->
                val link = element.selectFirst("a")
                val title = element.selectFirst("p")?.text() ?: "Unknown"
                val url = link?.attr("href") ?: return@map null

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = "Ongoing"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "MangaHub search failed" }
            emptyList()
        }
    }

    override suspend fun getChapters(mangaUrl: String): List<Chapter> {
        logger.info { "Fetching chapters from: $mangaUrl" }
        return emptyList()
    }

    override suspend fun downloadChapter(chapterUrl: String, outputPath: String) {
        logger.info { "Downloading chapter from: $chapterUrl" }
    }
}

// JenovaStorm Source
class JenovaStormSource : MangaSource() {
    override val name = "JenovaStorm"
    private val baseUrl = "https://www.jenovastorm.com"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching JenovaStorm for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/?s=$query")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".post").take(limit).map { element ->
                val link = element.selectFirst("a")
                val title = element.selectFirst("h2")?.text() ?: "Unknown"
                val url = link?.attr("href") ?: return@map null

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = "Ongoing"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "JenovaStorm search failed" }
            emptyList()
        }
    }

    override suspend fun getChapters(mangaUrl: String): List<Chapter> {
        logger.info { "Fetching chapters from: $mangaUrl" }
        return emptyList()
    }

    override suspend fun downloadChapter(chapterUrl: String, outputPath: String) {
        logger.info { "Downloading chapter from: $chapterUrl" }
    }
}

// Manwha Source
class ManwhaSource : MangaSource() {
    override val name = "Manwha"
    private val baseUrl = "https://manwha.love"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching Manwha for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/?s=$query")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".series").take(limit).map { element ->
                val link = element.selectFirst("a")
                val title = element.selectFirst("h4")?.text() ?: "Unknown"
                val url = link?.attr("href") ?: return@map null

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = "Ongoing"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "Manwha search failed" }
            emptyList()
        }
    }

    override suspend fun getChapters(mangaUrl: String): List<Chapter> {
        logger.info { "Fetching chapters from: $mangaUrl" }
        return emptyList()
    }

    override suspend fun downloadChapter(chapterUrl: String, outputPath: String) {
        logger.info { "Downloading chapter from: $chapterUrl" }
    }
}