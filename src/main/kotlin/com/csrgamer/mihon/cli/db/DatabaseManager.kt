package com.csrgamer.mihon.cli.db

import mu.KotlinLogging
import java.io.File
import java.sql.DriverManager

private val logger = KotlinLogging.logger {}

data class MangaRecord(
    val id: Int,
    val title: String,
    val url: String,
    val source: String,
    val chapterCount: Int,
    val lastUpdated: String
)

class DatabaseManager {
    private val dbPath = "${System.getProperty("user.home")}/.mihon/library.db"

    init {
        initDatabase()
    }

    private fun initDatabase() {
        val file = File(dbPath)
        file.parentFile?.mkdirs()

        val connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
        connection.use {
            it.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS manga (
                    id INTEGER PRIMARY KEY,
                    title TEXT NOT NULL,
                    url TEXT UNIQUE NOT NULL,
                    source TEXT NOT NULL,
                    chapter_count INTEGER DEFAULT 0,
                    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """.trimIndent())

            it.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS chapters (
                    id INTEGER PRIMARY KEY,
                    manga_id INTEGER NOT NULL,
                    chapter_number REAL NOT NULL,
                    title TEXT,
                    downloaded BOOLEAN DEFAULT 0,
                    FOREIGN KEY(manga_id) REFERENCES manga(id)
                )
            """.trimIndent())
        }

        logger.info { "Database initialized at: $dbPath" }
    }

    fun getAllManga(): List<MangaRecord> {
        return try {
            val connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
            connection.use {
                val statement = it.createStatement()
                val resultSet = statement.executeQuery("SELECT * FROM manga ORDER BY last_updated DESC")
                val results = mutableListOf<MangaRecord>()

                while (resultSet.next()) {
                    results.add(MangaRecord(
                        id = resultSet.getInt("id"),
                        title = resultSet.getString("title"),
                        url = resultSet.getString("url"),
                        source = resultSet.getString("source"),
                        chapterCount = resultSet.getInt("chapter_count"),
                        lastUpdated = resultSet.getString("last_updated")
                    ))
                }

                results
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to retrieve manga" }
            emptyList()
        }
    }

    fun addManga(title: String, url: String, source: String) {
        try {
            val connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
            connection.use {
                val statement = it.prepareStatement(
                    "INSERT OR IGNORE INTO manga (title, url, source) VALUES (?, ?, ?)"
                )
                statement.setString(1, title)
                statement.setString(2, url)
                statement.setString(3, source)
                statement.executeUpdate()
            }
            logger.info { "Added manga: $title" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to add manga" }
        }
    }
}