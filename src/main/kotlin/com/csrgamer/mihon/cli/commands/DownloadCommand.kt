package com.csrgamer.mihon.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import mu.KotlinLogging
import com.csrgamer.mihon.cli.downloader.MangaDownloader
import java.io.File

private val logger = KotlinLogging.logger {}

class DownloadCommand : CliktCommand(
    name = "download",
    help = "Download manga chapters"
) {
    private val mangaUrl: String by argument(help = "URL of the manga")
    private val output: String by option("--output", "-o", help = "Output directory")
        .default("./manga")
    private val startChapter: Int by option("--start", help = "Start chapter number")
        .default(1)
    private val endChapter: Int by option("--end", help = "End chapter number (optional)")
        .default(Int.MAX_VALUE)
    private val threads: Int by option("--threads", "-t", help = "Number of concurrent downloads")
        .default(3)
    private val skipExisting: Boolean by option("--skip-existing", help = "Skip already downloaded chapters")
        .flag(default = true)

    override fun run() {
        logger.info { "Starting download from: $mangaUrl" }
        
        try {
            val outputDir = File(output)
            if (!outputDir.exists()) {
                outputDir.mkdirs()
                echo("Created output directory: ${outputDir.absolutePath}")
            }

            val downloader = MangaDownloader(
                outputDir = outputDir,
                threads = threads,
                skipExisting = skipExisting
            )

            echo("📥 Downloading manga chapters...")
            downloader.download(
                mangaUrl = mangaUrl,
                startChapter = startChapter,
                endChapter = endChapter
            )

            echo("✅ Download completed!")
            echo("Saved to: ${outputDir.absolutePath}")
        } catch (e: Exception) {
            logger.error(e) { "Download failed" }
            echo("❌ Error: ${e.message}")
        }
    }
}