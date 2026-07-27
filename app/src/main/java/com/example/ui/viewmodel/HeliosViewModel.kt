package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.HeliosDatabase
import com.example.data.model.LibraryViewMode
import com.example.data.model.Playlist
import com.example.data.model.Song
import com.example.data.model.ThemeConfig
import com.example.data.repository.MusicRepository
import com.example.player.AudioPlayerManager
import com.example.theme.HeliosThemeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HeliosViewModel(application: Application) : AndroidViewModel(application) {
    private val db = HeliosDatabase.getDatabase(application)
    val repository = MusicRepository(db.songDao(), db.playlistDao(), application)
    val audioPlayer = AudioPlayerManager(application)
    val themeManager = HeliosThemeManager()

    val allSongs: StateFlow<List<Song>> = repository.allSongs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteSongs: StateFlow<List<Song>> = repository.favoriteSongs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val recentlyPlayedSongs: StateFlow<List<Song>> = repository.recentlyPlayedSongs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val mostPlayedSongs: StateFlow<List<Song>> = repository.mostPlayedSongs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val recentlyAddedSongs: StateFlow<List<Song>> = repository.recentlyAddedSongs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allPlaylists: StateFlow<List<Playlist>> = repository.allPlaylists.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: StateFlow<List<Song>> = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) repository.allSongs else repository.searchSongs(query)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _libraryViewMode = MutableStateFlow(LibraryViewMode.COMFORTABLE_LIST)
    val libraryViewMode: StateFlow<LibraryViewMode> = _libraryViewMode.asStateFlow()

    private val _editingSong = MutableStateFlow<Song?>(null)
    val editingSong: StateFlow<Song?> = _editingSong.asStateFlow()

    val themeConfig: StateFlow<ThemeConfig> = themeManager.config

    init {
        viewModelScope.launch {
            repository.initializeDatabaseIfEmpty()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun setLibraryViewMode(mode: LibraryViewMode) {
        _libraryViewMode.value = mode
    }

    fun playSong(song: Song) {
        val list = allSongs.value.ifEmpty { listOf(song) }
        val index = list.indexOfFirst { it.id == song.id }.coerceAtLeast(0)
        audioPlayer.loadQueueAndPlay(list, index)
        viewModelScope.launch {
            repository.recordSongPlayed(song.id)
        }
    }

    fun toggleFavorite(song: Song) {
        viewModelScope.launch {
            repository.toggleFavorite(song.id)
        }
    }

    fun togglePinSong(song: Song) {
        viewModelScope.launch {
            repository.togglePinSong(song.id)
        }
    }

    fun openMetadataEditor(song: Song) {
        _editingSong.value = song
    }

    fun closeMetadataEditor() {
        _editingSong.value = null
    }

    fun saveMetadata(updatedSong: Song) {
        viewModelScope.launch {
            repository.updateSongMetadata(updatedSong)
            _editingSong.value = null
        }
    }

    fun createPlaylist(name: String, description: String, coverStyle: String) {
        viewModelScope.launch {
            repository.createPlaylist(name, description, coverStyle)
        }
    }

    fun scanLocalMedia() {
        viewModelScope.launch {
            repository.scanLocalMediaStore()
        }
    }
}
