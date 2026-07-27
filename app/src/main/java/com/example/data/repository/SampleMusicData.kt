package com.example.data.repository

import com.example.data.model.Playlist
import com.example.data.model.Song

object SampleMusicData {
    val sampleSongs = listOf(
        Song(
            id = "helios_01",
            title = "Solar Horizon",
            artist = "Helios Collective",
            album = "Cosmic Rays",
            genre = "Ambient Synthwave",
            durationMs = 214000L,
            trackNumber = 1,
            year = 2026,
            composer = "A. Sol",
            lyrics = "[00:15.00]Rising through the golden haze\n[00:30.00]Chasing light in endless waves\n[00:45.00]Solar winds begin to sweep\n[01:05.00]Waking oceans from their sleep\n[01:30.00]Echoes in the stratosphere\n[02:00.00]Pure horizon crystal clear",
            isFavorite = true,
            playCount = 42,
            folderPath = "/Music/Helios Ambient"
        ),
        Song(
            id = "helios_02",
            title = "Midnight Drive Lo-Fi",
            artist = "Aura Noir",
            album = "City Lights at 2 AM",
            genre = "Lo-Fi Beats",
            durationMs = 185000L,
            trackNumber = 2,
            year = 2025,
            composer = "N. Vesper",
            lyrics = "[00:10.00]Neon reflections on wet asphalt\n[00:25.00]Low bass humming softly\n[00:40.00]Streetlights flickering past\n[01:10.00]Time stands still in midnight air",
            isFavorite = true,
            playCount = 68,
            folderPath = "/Music/Chill Beats"
        ),
        Song(
            id = "helios_03",
            title = "Neon Afterglow",
            artist = "Cyber Pulse",
            album = "Future Systems",
            genre = "Retrowave",
            durationMs = 240000L,
            trackNumber = 3,
            year = 2026,
            composer = "K. Byte",
            lyrics = "[00:12.00]Digital pulse in violet streams\n[00:28.00]Cybernetic winter dreams\n[00:50.00]Frequency high, voltage clean\n[01:20.00]Sailing through the neon screen",
            isFavorite = false,
            playCount = 19,
            folderPath = "/Music/Retrowave"
        ),
        Song(
            id = "helios_04",
            title = "Acoustic Reflection",
            artist = "Elysian Strings",
            album = "Quiet Mornings",
            genre = "Acoustic Folk",
            durationMs = 198000L,
            trackNumber = 1,
            year = 2024,
            composer = "M. Rivers",
            lyrics = "[00:18.00]Gentle breeze across the lake\n[00:35.00]Promises we meant to make\n[00:55.00]Sunlight filtering through leaves\n[01:25.00]Simple peace that heart receives",
            isFavorite = true,
            playCount = 35,
            folderPath = "/Music/Acoustic"
        ),
        Song(
            id = "helios_05",
            title = "Starlight Resonance",
            artist = "Orion Ensemble",
            album = "Deep Space Harmonies",
            genre = "Cinematic Neo-Classical",
            durationMs = 310000L,
            trackNumber = 5,
            year = 2026,
            composer = "L. Kepler",
            lyrics = "[Instrumental Classical Piece]",
            isFavorite = false,
            playCount = 27,
            folderPath = "/Music/Classical"
        ),
        Song(
            id = "helios_06",
            title = "Nothing Os Matrix Jam",
            artist = "Glyph Collective",
            album = "Dot Matrix Tapes",
            genre = "Experimental Glitch",
            durationMs = 162000L,
            trackNumber = 1,
            year = 2025,
            composer = "C. Glyph",
            lyrics = "[00:08.00]Dot matrix pulse\n[00:20.00]Monochrome rhythms\n[00:40.00]Binary frequency shift",
            isFavorite = true,
            playCount = 51,
            folderPath = "/Music/Experimental"
        ),
        Song(
            id = "helios_07",
            title = "Velvet Evening Jazz",
            artist = "Blue Note Quartet",
            album = "Smokey Room Sessions",
            genre = "Smooth Jazz",
            durationMs = 275000L,
            trackNumber = 4,
            year = 2023,
            composer = "D. Miles",
            lyrics = "[00:22.00]Soft sax whisper\n[00:45.00]Brushed snare cadence\n[01:15.00]Warm piano chords in C minor",
            isFavorite = false,
            playCount = 14,
            folderPath = "/Music/Jazz"
        ),
        Song(
            id = "helios_08",
            title = "Quantum Echoes",
            artist = "Vector Field",
            album = "Subatomic Waves",
            genre = "Ambient Electronic",
            durationMs = 230000L,
            trackNumber = 2,
            year = 2026,
            composer = "P. Dirac",
            lyrics = "[00:15.00]Particles in superposition\n[00:35.00]Entangled resonance across space",
            isFavorite = true,
            playCount = 33,
            folderPath = "/Music/Ambient"
        )
    )

    val samplePlaylists = listOf(
        Playlist("pl_01", "Night Drive Chill", "Relaxing lo-fi beats & ambient synth", "Gradient"),
        Playlist("pl_02", "Helios Favorites", "Curated top tracks for deep focus", "Minimal Letter"),
        Playlist("pl_03", "Acoustic & Classical", "Soothing organic instruments", "Abstract Shapes"),
        Playlist("pl_04", "Future Retrowave", "High energy cyberpunk synthwave", "Vinyl")
    )
}
