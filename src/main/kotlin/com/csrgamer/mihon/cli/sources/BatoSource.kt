package com.csrgamer.mihon.cli.sources

import mu.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.*

private val logger = KotlinLogging.logger {}

class BatoSource : MangaSource() {
    override val name = "Bato"
    private val baseUrl = "https://bato.to"
    private val apiUrl = "https://api.bato.to"
    private val httpClient = HttpClient()

    override suspend fun search(query: String, limit: Int): List<Manga> {
        logger.info { "Searching Bato for: $query" }
        return try {
            val response = httpClient.get("$apiUrl/v3/series/search") {
                parameter("query", query)
                parameter("limit", limit)
            }

            val json = Json.parseToJsonElement(response.body<String>()).jsonObject
            val items = json["series"]?.jsonArray ?: return emptyList()

            items.mapNotNull { item ->
                val obj = item.jsonObject
                val id = obj["id"]?.jsonPrimitive?.content ?: return@mapNotNull null
                val title = obj["title"]?.jsonPrimitive?.content ?: "Unknown"
                val status = obj["status"]?.jsonPrimitive?.content ?: "Ongoing"

                Manga(
                    title = title,
                    url = "$baseUrl/series/$id",
                    source = name,
                    status = status
                )
            }
        } catch (e: Exception) {
            logger.error(e) { "Bato search failed" }
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