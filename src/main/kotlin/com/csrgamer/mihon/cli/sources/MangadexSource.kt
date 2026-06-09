package com.csrgamer.mihon.cli.sources

import mu.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.*

private val logger = KotlinLogging.logger {}

class MangadexSource : MangaSource() {
    override val name = "MangaDex"
    private val apiUrl = "https://api.mangadex.org"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching MangaDex for: $query" }
        return try {
            val response = httpClient.get("$apiUrl/manga") {
                parameter("title", query)
                parameter("limit", limit)
            }
            
            val json = Json.parseToJsonElement(response.body<String>()).jsonObject
            val data = json["data"]?.jsonArray ?: return emptyList()

            data.map { item ->
                val obj = item.jsonObject
                val attributes = obj["attributes"]?.jsonObject ?: return@map null
                val id = obj["id"]?.jsonPrimitive?.content ?: return@map null

                Manga(
                    title = attributes["title"]?.jsonObject?.get("en")?.jsonPrimitive?.content ?: "Unknown",
                    url = "https://mangadex.org/title/$id",
                    source = name,
                    status = attributes["status"]?.jsonPrimitive?.content ?: "Unknown"
                )
            }.filterNotNull()
        } catch (e: Exception) {
            logger.error(e) { "MangaDex search failed" }
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