package com.example.data.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.data.local.PlaylistDao
import com.example.data.local.SongDao
import com.example.data.model.Playlist
import com.example.data.model.PlaylistSongCrossRef
import com.example.data.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class MusicRepository(
    private val songDao: SongDao,
    private val playlistDao: PlaylistDao,
    private val context: Context
) {
    val allSongs: Flow<List<Song>> = songDao.getAllSongs()
    val favoriteSongs: Flow<List<Song>> = songDao.getFavoriteSongs()
    val mostPlayedSongs: Flow<List<Song>> = songDao.getMostPlayedSongs()
    val recentlyAddedSongs: Flow<List<Song>> = songDao.getRecentlyAddedSongs()
    val recentlyPlayedSongs: Flow<List<Song>> = songDao.getRecentlyPlayedSongs()
    val allPlaylists: Flow<List<Playlist>> = playlistDao.getAllPlaylists()

    suspend fun initializeDatabaseIfEmpty() {
        withContext(Dispatchers.IO) {
            val existing = songDao.getAllSongs().first()
            if (existing.isEmpty()) {
                songDao.insertSongs(SampleMusicData.sampleSongs)
                SampleMusicData.samplePlaylists.forEach { playlist ->
                    playlistDao.insertPlaylist(playlist)
                }
                // Link some songs to initial playlist
                playlistDao.insertSongToPlaylist(PlaylistSongCrossRef("pl_01", "helios_02", 0))
                playlistDao.insertSongToPlaylist(PlaylistSongCrossRef("pl_01", "helios_03", 1))
                playlistDao.insertSongToPlaylist(PlaylistSongCrossRef("pl_02", "helios_01", 0))
                playlistDao.insertSongToPlaylist(PlaylistSongCrossRef("pl_02", "helios_06", 1))
            }
        }
    }

    suspend fun toggleFavorite(songId: String) {
        withContext(Dispatchers.IO) {
            val song = songDao.getSongById(songId)
            if (song != null) {
                songDao.setFavorite(songId, !song.isFavorite)
            }
        }
    }

    suspend fun recordSongPlayed(songId: String) {
        withContext(Dispatchers.IO) {
            songDao.incrementPlayCount(songId)
        }
    }

    suspend fun updateSongMetadata(updatedSong: Song) {
        withContext(Dispatchers.IO) {
            songDao.updateSong(updatedSong)
        }
    }

    suspend fun togglePinSong(songId: String) {
        withContext(Dispatchers.IO) {
            val song = songDao.getSongById(songId)
            if (song != null) {
                songDao.updateSong(song.copy(isPinned = !song.isPinned))
            }
        }
    }

    fun searchSongs(query: String): Flow<List<Song>> {
        return songDao.searchSongs(query)
    }

    suspend fun createPlaylist(name: String, description: String, coverStyle: String) {
        withContext(Dispatchers.IO) {
            val newPlaylist = Playlist(
                id = "pl_${System.currentTimeMillis()}",
                name = name,
                description = description,
                coverStyle = coverStyle
            )
            playlistDao.insertPlaylist(newPlaylist)
        }
    }

    suspend fun addSongToPlaylist(playlistId: String, songId: String) {
        withContext(Dispatchers.IO) {
            playlistDao.insertSongToPlaylist(PlaylistSongCrossRef(playlistId, songId))
        }
    }

    suspend fun removeSongFromPlaylist(playlistId: String, songId: String) {
        withContext(Dispatchers.IO) {
            playlistDao.removeSongFromPlaylist(playlistId, songId)
        }
    }

    fun getSongsForPlaylist(playlistId: String): Flow<List<Song>> {
        return playlistDao.getSongsForPlaylist(playlistId)
    }

    suspend fun scanLocalMediaStore(): Int {
        return withContext(Dispatchers.IO) {
            val scannedSongs = mutableListOf<Song>()
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.YEAR
            )

            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

            try {
                context.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    null,
                    sortOrder
                )?.use { cursor ->
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                    val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                    val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                    val trackColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
                    val yearColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val title = cursor.getString(titleColumn) ?: "Unknown Title"
                        val artist = cursor.getString(artistColumn) ?: "Unknown Artist"
                        val album = cursor.getString(albumColumn) ?: "Unknown Album"
                        val duration = cursor.getLong(durationColumn)
                        val path = cursor.getString(dataColumn) ?: ""
                        val albumId = cursor.getLong(albumIdColumn)
                        val track = cursor.getInt(trackColumn)
                        val year = cursor.getInt(yearColumn)

                        val albumArtUri = ContentUris.withAppendedId(
                            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            albumId
                        ).toString()

                        val folderPath = path.substringBeforeLast('/', "/Music")

                        scannedSongs.add(
                            Song(
                                id = "ms_$id",
                                title = title,
                                artist = artist,
                                album = album,
                                durationMs = duration,
                                path = path,
                                albumArtUri = albumArtUri,
                                trackNumber = track,
                                year = year,
                                folderPath = folderPath
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (scannedSongs.isNotEmpty()) {
                songDao.insertSongs(scannedSongs)
            }
            scannedSongs.size
        }
    }
}
