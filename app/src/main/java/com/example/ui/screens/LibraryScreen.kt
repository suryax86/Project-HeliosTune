package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LibraryViewMode
import com.example.data.model.Song
import com.example.data.model.ThemeConfig
import com.example.theme.HeliosColors
import com.example.ui.components.DynamicArtworkView
import com.example.ui.components.SongItemCard

@Composable
fun LibraryScreen(
    songs: List<Song>,
    themeConfig: ThemeConfig,
    viewMode: LibraryViewMode,
    currentPlayingSongId: String?,
    onSongClick: (Song) -> Unit,
    onFavoriteToggle: (Song) -> Unit,
    onPinToggle: (Song) -> Unit,
    onEditMetadata: (Song) -> Unit,
    onToggleViewMode: (LibraryViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = HeliosColors.parseColor(themeConfig.accentColorHex)
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Songs", "Albums", "Artists", "Genres", "Folders")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(if (themeConfig.isAmoledMode) HeliosColors.AmoledBlack else HeliosColors.DarkCardBg)
    ) {
        // Library Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "MUSIC LIBRARY",
                    color = accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "${songs.size} Tracks Available",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = {
                    val nextMode = when (viewMode) {
                        LibraryViewMode.COMFORTABLE_LIST -> LibraryViewMode.GRID
                        LibraryViewMode.GRID -> LibraryViewMode.COMPACT_LIST
                        LibraryViewMode.COMPACT_LIST -> LibraryViewMode.LARGE_CARDS
                        LibraryViewMode.LARGE_CARDS -> LibraryViewMode.COMFORTABLE_LIST
                    }
                    onToggleViewMode(nextMode)
                }
            ) {
                Icon(
                    imageVector = if (viewMode == LibraryViewMode.GRID) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView,
                    contentDescription = "Toggle View Mode",
                    tint = Color.White
                )
            }
        }

        // Category Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = accent,
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == index) accent else Color.White.copy(alpha = 0.6f),
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (songs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.MusicNote,
                        contentDescription = "Empty",
                        tint = accent,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No songs found in your library",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Go to Settings to scan local MediaStore or import audio files",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            when (viewMode) {
                LibraryViewMode.GRID -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 120.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(songs) { song ->
                            GridSongCard(
                                song = song,
                                themeConfig = themeConfig,
                                isCurrentPlaying = song.id == currentPlayingSongId,
                                onClick = { onSongClick(song) }
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 120.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(songs) { song ->
                            SongItemCard(
                                song = song,
                                themeConfig = themeConfig,
                                isCurrentPlaying = song.id == currentPlayingSongId,
                                onSongClick = { onSongClick(song) },
                                onFavoriteToggle = { onFavoriteToggle(song) },
                                onPinToggle = { onPinToggle(song) },
                                onEditMetadata = { onEditMetadata(song) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GridSongCard(
    song: Song,
    themeConfig: ThemeConfig,
    isCurrentPlaying: Boolean,
    onClick: () -> Unit
) {
    val accent = HeliosColors.parseColor(themeConfig.accentColorHex)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (isCurrentPlaying) accent.copy(alpha = 0.2f) else Color(0xFF1E1E24))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        DynamicArtworkView(
            title = song.title,
            artist = song.artist,
            style = themeConfig.dynamicArtStyle,
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            cornerRadius = 16.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = song.title,
            color = if (isCurrentPlaying) accent else Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = song.artist,
            color = Color.White.copy(alpha = 0.65f),
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
