package com.csrgamer.mihon.cli.sources

import mu.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.jsoup.Jsoup

private val logger = KotlinLogging.logger {}

// Crunchyroll Manga Source
class CrunchyrollSource : MangaSource() {
    override val name = "Crunchyroll"
    private val baseUrl = "https://www.crunchyroll.com"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching Crunchyroll for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/en/manga_list/all")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".manga-card").take(limit)
                .filter { it.selectFirst("h3")?.text()?.contains(query, ignoreCase = true) == true }
                .map { element ->
                    val link = element.selectFirst("a")
                    val title = element.selectFirst("h3")?.text() ?: "Unknown"
                    val url = link?.attr("href") ?: return@map null

                    Manga(
                        title = title,
                        url = url,
                        source = name,
                        status = "Ongoing"
                    )
                }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "Crunchyroll search failed" }
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

// Tapas Source
class TapasSource : MangaSource() {
    override val name = "Tapas"
    private val baseUrl = "https://www.tapas.io"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching Tapas for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/search?q=$query&page=1")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".series-item").take(limit).map { element ->
                val link = element.selectFirst("a")
                val title = element.selectFirst(".series-name")?.text() ?: "Unknown"
                val url = link?.attr("href") ?: return@map null

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = "Ongoing"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "Tapas search failed" }
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

// Webtoon Source
class WebtoonSource : MangaSource() {
    override val name = "Webtoon"
    private val baseUrl = "https://www.webtoons.com"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching Webtoon for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/search")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".search_card_item").take(limit)
                .filter { it.selectFirst("p")?.text()?.contains(query, ignoreCase = true) == true }
                .map { element ->
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
            logger.error(e) { "Webtoon search failed" }
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

// Mangaplus Source
class MangaPlusSource : MangaSource() {
    override val name = "MangaPlus"
    private val baseUrl = "https://mangaplus.shueisha.co.jp"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching MangaPlus for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/search")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".manga-title").take(limit).map { element ->
                val link = element.selectFirst("a")
                val title = element.text()
                val url = link?.attr("href") ?: return@map null

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = "Ongoing"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "MangaPlus search failed" }
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

// ComicWalker Source
class ComicWalkerSource : MangaSource() {
    override val name = "ComicWalker"
    private val baseUrl = "https://comic-walker.com"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching ComicWalker for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/search?word=$query")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".contents-inner").take(limit).map { element ->
                val link = element.selectFirst("a")
                val title = element.selectFirst(".title")?.text() ?: "Unknown"
                val url = link?.attr("href") ?: return@map null

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = "Ongoing"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "ComicWalker search failed" }
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

// KissManga Source
class KissMangaSource : MangaSource() {
    override val name = "KissManga"
    private val baseUrl = "https://kissmanga.org"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching KissManga for: $query" }
        return try {
            val response = httpClient.get("$baseUrl/search?query=$query")
            val html = response.body<String>()
            val doc = Jsoup.parse(html)

            doc.select(".manga-box").take(limit).map { element ->
                val link = element.selectFirst("a")
                val title = element.selectFirst("h3")?.text() ?: "Unknown"
                val url = link?.attr("href") ?: return@map null

                Manga(
                    title = title,
                    url = url,
                    source = name,
                    status = "Ongoing"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "KissManga search failed" }
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