package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String = "Unknown Artist",
    val album: String = "Unknown Album",
    val genre: String = "Unknown Genre",
    val durationMs: Long = 0L,
    val path: String = "",
    val albumArtUri: String? = null,
    val trackNumber: Int = 0,
    val year: Int = 0,
    val composer: String = "",
    val lyrics: String = "",
    val isFavorite: Boolean = false,
    val playCount: Int = 0,
    val dateAdded: Long = System.currentTimeMillis(),
    val lastPlayedTimestamp: Long = 0L,
    val isPinned: Boolean = false,
    val folderPath: String = ""
)
