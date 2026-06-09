package com.csrgamer.mihon.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.default
import mu.KotlinLogging
import com.csrgamer.mihon.cli.commands.*

private val logger = KotlinLogging.logger {}

class MihonCli : CliktCommand(
    name = "mihon",
    help = "Mihon CLI - A free and open source manga downloader for Linux"
) {
    override fun run() {
        logger.info { "Mihon CLI initialized" }
    }
}

fun main(args: Array<String>) {
    MihonCli()
        .subcommands(
            SearchCommand(),
            DownloadCommand(),
            ListCommand(),
            UpdateCommand(),
            ConfigCommand()
        )
        .main(args)
}