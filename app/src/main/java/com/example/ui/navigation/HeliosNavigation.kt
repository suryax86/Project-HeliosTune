package com.example.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.HeliosColors
import com.example.ui.components.AudioSettingsDialog
import com.example.ui.components.MetadataEditorDialog
import com.example.ui.components.MiniPlayerBar
import com.example.ui.components.QueueBottomSheet
import com.example.ui.player.MainPlayerView
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.viewmodel.HeliosViewModel

enum class NavTab { HOME, LIBRARY, SEARCH, SETTINGS }

@Composable
fun HeliosAppContainer(viewModel: HeliosViewModel) {
    var activeTab by remember { mutableStateOf(NavTab.HOME) }
    var isPlayerExpanded by remember { mutableStateOf(false) }
    var showAudioSettings by remember { mutableStateOf(false) }
    var showQueueSheet by remember { mutableStateOf(false) }

    val themeConfig by viewModel.themeConfig.collectAsState()
    val accent = HeliosColors.parseColor(themeConfig.accentColorHex)

    val currentSong by viewModel.audioPlayer.currentSong.collectAsState()
    val isPlaying by viewModel.audioPlayer.isPlaying.collectAsState()
    val progressMs by viewModel.audioPlayer.progressMs.collectAsState()
    val durationMs by viewModel.audioPlayer.durationMs.collectAsState()
    val isShuffle by viewModel.audioPlayer.isShuffle.collectAsState()
    val repeatMode by viewModel.audioPlayer.repeatMode.collectAsState()
    val sleepTimerMins by viewModel.audioPlayer.sleepTimerMinutesLeft.collectAsState()

    val allSongs by viewModel.allSongs.collectAsState()
    val favoriteSongs by viewModel.favoriteSongs.collectAsState()
    val recentlyPlayed by viewModel.recentlyPlayedSongs.collectAsState()
    val mostPlayed by viewModel.mostPlayedSongs.collectAsState()
    val recentlyAdded by viewModel.recentlyAddedSongs.collectAsState()
    val playlists by viewModel.allPlaylists.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val viewMode by viewModel.libraryViewMode.collectAsState()
    val editingSong by viewModel.editingSong.collectAsState()

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (themeConfig.isAmoledMode) HeliosColors.AmoledBlack else HeliosColors.DarkSurfaceBg)
            ) {
                // Docked Mini Player
                if (currentSong != null && !isPlayerExpanded) {
                    MiniPlayerBar(
                        song = currentSong!!,
                        isPlaying = isPlaying,
                        progressMs = progressMs,
                        durationMs = durationMs,
                        themeConfig = themeConfig,
                        onPlayPauseToggle = { viewModel.audioPlayer.togglePlayPause() },
                        onSkipNext = { viewModel.audioPlayer.skipToNext() },
                        onClickPlayer = { isPlayerExpanded = true }
                    )
                }

                // Modern Navigation Bar
                NavigationBar(
                    containerColor = if (themeConfig.isAmoledMode) HeliosColors.AmoledBlack else HeliosColors.DarkSurfaceBg,
                    contentColor = Color.White
                ) {
                    NavigationBarItem(
                        selected = activeTab == NavTab.HOME,
                        onClick = { activeTab = NavTab.HOME },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = accent,
                            indicatorColor = accent,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                        )
                    )

                    NavigationBarItem(
                        selected = activeTab == NavTab.LIBRARY,
                        onClick = { activeTab = NavTab.LIBRARY },
                        icon = { Icon(Icons.Default.LibraryMusic, contentDescription = "Library") },
                        label = { Text("Library", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = accent,
                            indicatorColor = accent,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                        )
                    )

                    NavigationBarItem(
                        selected = activeTab == NavTab.SEARCH,
                        onClick = { activeTab = NavTab.SEARCH },
                        icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        label = { Text("Search", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = accent,
                            indicatorColor = accent,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                        )
                    )

                    NavigationBarItem(
                        selected = activeTab == NavTab.SETTINGS,
                        onClick = { activeTab = NavTab.SETTINGS },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = accent,
                            indicatorColor = accent,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                NavTab.HOME -> {
                    HomeScreen(
                        recentlyPlayed = recentlyPlayed,
                        favorites = favoriteSongs,
                        mostPlayed = mostPlayed,
                        recentlyAdded = recentlyAdded,
                        playlists = playlists,
                        allSongs = allSongs,
                        themeConfig = themeConfig,
                        currentPlayingSongId = currentSong?.id,
                        onSongClick = { song -> viewModel.playSong(song) },
                        onFavoriteToggle = { song -> viewModel.toggleFavorite(song) },
                        onPinToggle = { song -> viewModel.togglePinSong(song) },
                        onEditMetadata = { song -> viewModel.openMetadataEditor(song) },
                        onCreatePlaylist = {
                            viewModel.createPlaylist("Custom Playlist #${playlists.size + 1}", "Personal collection", "Gradient")
                        }
                    )
                }
                NavTab.LIBRARY -> {
                    LibraryScreen(
                        songs = allSongs,
                        themeConfig = themeConfig,
                        viewMode = viewMode,
                        currentPlayingSongId = currentSong?.id,
                        onSongClick = { song -> viewModel.playSong(song) },
                        onFavoriteToggle = { song -> viewModel.toggleFavorite(song) },
                        onPinToggle = { song -> viewModel.togglePinSong(song) },
                        onEditMetadata = { song -> viewModel.openMetadataEditor(song) },
                        onToggleViewMode = { mode -> viewModel.setLibraryViewMode(mode) }
                    )
                }
                NavTab.SEARCH -> {
                    SearchScreen(
                        query = searchQuery,
                        searchResults = searchResults,
                        themeConfig = themeConfig,
                        currentPlayingSongId = currentSong?.id,
                        onQueryChanged = { viewModel.onSearchQueryChanged(it) },
                        onSongClick = { song -> viewModel.playSong(song) },
                        onFavoriteToggle = { song -> viewModel.toggleFavorite(song) },
                        onPinToggle = { song -> viewModel.togglePinSong(song) },
                        onEditMetadata = { song -> viewModel.openMetadataEditor(song) }
                    )
                }
                NavTab.SETTINGS -> {
                    SettingsScreen(
                        themeConfig = themeConfig,
                        themeManager = viewModel.themeManager,
                        onScanMedia = { viewModel.scanLocalMedia() }
                    )
                }
            }

            // Expanded Full Screen Now Playing Sheet
            AnimatedVisibility(
                visible = isPlayerExpanded && currentSong != null,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                if (currentSong != null) {
                    val audioSpectrum by viewModel.audioPlayer.audioSpectrum.collectAsState()
                    MainPlayerView(
                        song = currentSong!!,
                        isPlaying = isPlaying,
                        progressMs = progressMs,
                        durationMs = durationMs,
                        themeConfig = themeConfig,
                        isFavorite = currentSong!!.isFavorite,
                        isShuffle = isShuffle,
                        repeatModeState = repeatMode.name,
                        onPlayPauseToggle = { viewModel.audioPlayer.togglePlayPause() },
                        onSkipNext = { viewModel.audioPlayer.skipToNext() },
                        onSkipPrevious = { viewModel.audioPlayer.skipToPrevious() },
                        onSeek = { pos -> viewModel.audioPlayer.seekTo(pos) },
                        onFavoriteToggle = { viewModel.toggleFavorite(currentSong!!) },
                        onShuffleToggle = { viewModel.audioPlayer.toggleShuffle() },
                        onRepeatToggle = { viewModel.audioPlayer.toggleRepeat() },
                        onOpenQueue = { showQueueSheet = true },
                        onOpenAudioSettings = { showAudioSettings = true },
                        onCollapse = { isPlayerExpanded = false },
                        spectrum = audioSpectrum,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    )
                }
            }
        }
    }

    // Dialog Overlays
    if (showQueueSheet) {
        val queue by viewModel.audioPlayer.queue.collectAsState()
        val currentIndex by viewModel.audioPlayer.currentIndex.collectAsState()
        QueueBottomSheet(
            queue = queue,
            currentIndex = currentIndex,
            themeConfig = themeConfig,
            onSongSelect = { song -> viewModel.playSong(song) },
            onDismiss = { showQueueSheet = false }
        )
    }

    if (showAudioSettings) {
        AudioSettingsDialog(
            audioPlayer = viewModel.audioPlayer,
            themeConfig = themeConfig,
            sleepTimerMinutesLeft = sleepTimerMins,
            onDismiss = { showAudioSettings = false }
        )
    }

    if (editingSong != null) {
        MetadataEditorDialog(
            song = editingSong!!,
            themeConfig = themeConfig,
            onDismiss = { viewModel.closeMetadataEditor() },
            onSave = { updated -> viewModel.saveMetadata(updated) }
        )
    }
}
