# Mihon CLI for Linux

A free and open source manga downloader for Linux CLI, based on Mihon.

## Features

- 📚 Search manga across multiple sources
- 💾 Download manga chapters
- 🔄 Automatic update checks
- 📖 Local library management
- ⚙️ Configurable settings
- 🚀 Concurrent downloads for faster speeds

## Supported Sources

- MangaDex
- AsuraScans
- ManhuaCalf
- (More sources coming)

## Installation

### Prerequisites
- Kotlin 1.9.21+
- Java 11+
- Gradle

### Build from Source

```bash
./gradlew build
./gradlew run
```

### Install

```bash
./gradlew installDist
# Binary will be in build/install/mihon-cli/bin/
```

## Usage

### Search for Manga

```bash
# Search across all sources
mihon search "attack on titan"

# Search specific source
mihon search "attack on titan" --source MangaDex

# Limit results
mihon search "attack on titan" --limit 10
```

### Download Manga

```bash
# Download specific chapter range
mihon download "https://mangadex.org/title/xxxxx" --start 1 --end 50

# Download to custom directory
mihon download "URL" --output /path/to/manga

# Use multiple threads
mihon download "URL" --threads 5

# Skip existing chapters
mihon download "URL" --skip-existing
```

### Manage Library

```bash
# List all manga
mihon list

# List all with sorting
mihon list --sort date

# Show all including completed
mihon list --all
```

### Check Updates

```bash
# Check updates for all manga
mihon update --all

# Check specific manga
mihon update --manga "Attack on Titan"
```

### Configuration

```bash
# Show all settings
mihon config

# Show specific setting
mihon config download_dir

# Change setting
mihon config download_dir /path/to/manga
mihon config threads 5
mihon config image_quality high
```

## Configuration File

Settings are stored in `~/.mihon/config.properties`:

```properties
download_dir=./manga
threads=3
image_quality=high
auto_update=false
```

## Database

Manga library is stored locally in `~/.mihon/library.db` (SQLite)

## Architecture

```
src/
├── commands/          # CLI commands
├── sources/           # Manga source implementations
├── downloader/        # Download logic
├── db/               # Database management
└── config/           # Configuration management
```

## Development

### Adding a New Source

1. Create a new class extending `MangaSource` in `src/main/kotlin/com/csrgamer/mihon/cli/sources/`
2. Implement required methods: `search()`, `getChapters()`, `downloadChapter()`
3. Register in `SourceManager.kt`

Example:

```kotlin
class NewSource : MangaSource() {
    override val name = "NewSource"
    
    override suspend fun search(query: String, limit: Int): List<Manga> {
        // Implement search
    }
    
    override suspend fun getChapters(mangaUrl: String): List<Chapter> {
        // Implement chapter fetching
    }
    
    override suspend fun downloadChapter(chapterUrl: String, outputPath: String) {
        // Implement download
    }
}
```

## License

This project is based on Mihon and maintains GPL compatibility.

## Disclaimer

This tool is for personal use only. Users are responsible for complying with copyright laws in their jurisdiction.

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Submit a pull request

## TODOs

- [ ] Complete MangaDex API integration
- [ ] Complete HTML parsing for other sources
- [ ] Implement image download and conversion
- [ ] Add PDF export
- [ ] Add CBZ/CBR format support
- [ ] Implement concurrent chapter downloads
- [ ] Add proxy support
- [ ] Add authentication for premium sources
- [ ] Implement resume on failed downloads
- [ ] Add scheduling for automatic updates
