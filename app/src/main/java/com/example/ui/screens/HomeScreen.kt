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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Playlist
import com.example.data.model.Song
import com.example.data.model.ThemeConfig
import com.example.theme.HeliosColors
import com.example.ui.components.DynamicArtworkView
import com.example.ui.components.SongItemCard

@Composable
fun HomeScreen(
    recentlyPlayed: List<Song>,
    favorites: List<Song>,
    mostPlayed: List<Song>,
    recentlyAdded: List<Song>,
    playlists: List<Playlist>,
    allSongs: List<Song>,
    themeConfig: ThemeConfig,
    currentPlayingSongId: String?,
    onSongClick: (Song) -> Unit,
    onFavoriteToggle: (Song) -> Unit,
    onPinToggle: (Song) -> Unit,
    onEditMetadata: (Song) -> Unit,
    onCreatePlaylist: (String, String) -> Unit,
    onPlaylistSelect: (Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = HeliosColors.parseColor(themeConfig.accentColorHex)
    val heroSong = recentlyPlayed.firstOrNull() ?: favorites.firstOrNull() ?: allSongs.firstOrNull()
    var showCreateDialog by remember { mutableStateOf(false) }
    var newPlaylistName by remember { mutableStateOf("") }
    var newPlaylistDesc by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(if (themeConfig.isAmoledMode) HeliosColors.AmoledBlack else HeliosColors.DarkCardBg),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Top Welcome Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "HeliosTune",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Your Music. Your Way.",
                    color = accent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Hero Card / Quick Resume
        if (heroSong != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(accent.copy(alpha = 0.35f), Color(0xFF1E1E24))
                            )
                        )
                        .clickable { onSongClick(heroSong) }
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val secondaryAccent = HeliosColors.parseColor(themeConfig.secondaryAccentHex)
                        DynamicArtworkView(
                            song = heroSong,
                            style = themeConfig.dynamicArtStyle,
                            primaryColor = accent,
                            secondaryColor = secondaryAccent,
                            modifier = Modifier.size(80.dp),
                            cornerRadius = 16.dp
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "QUICK RESUME",
                                color = accent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = heroSong.title,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = heroSong.artist,
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(accent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "Play Hero Song",
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }

        // Recently Played Section
        if (recentlyPlayed.isNotEmpty()) {
            item {
                SectionHeader("Recently Played")
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(recentlyPlayed) { song ->
                        HorizontalSongCard(
                            song = song,
                            themeConfig = themeConfig,
                            onClick = { onSongClick(song) }
                        )
                    }
                }
            }
        }

        // Playlists Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Playlists",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { showCreateDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Create Playlist", tint = accent)
                }
            }
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(playlists) { playlist ->
                    PlaylistCard(
                        playlist = playlist,
                        themeConfig = themeConfig,
                        onClick = { onPlaylistSelect(playlist) }
                    )
                }
            }
        }

        // Favorites Section
        if (favorites.isNotEmpty()) {
            item {
                SectionHeader("Favorites")
            }
            items(favorites.take(5)) { song ->
                SongItemCard(
                    song = song,
                    themeConfig = themeConfig,
                    isCurrentPlaying = song.id == currentPlayingSongId,
                    onSongClick = { onSongClick(song) },
                    onFavoriteToggle = { onFavoriteToggle(song) },
                    onPinToggle = { onPinToggle(song) },
                    onEditMetadata = { onEditMetadata(song) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        // Most Played Section
        if (mostPlayed.isNotEmpty()) {
            item {
                SectionHeader("Most Played")
            }
            items(mostPlayed.take(5)) { song ->
                SongItemCard(
                    song = song,
                    themeConfig = themeConfig,
                    isCurrentPlaying = song.id == currentPlayingSongId,
                    onSongClick = { onSongClick(song) },
                    onFavoriteToggle = { onFavoriteToggle(song) },
                    onPinToggle = { onPinToggle(song) },
                    onEditMetadata = { onEditMetadata(song) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create New Playlist", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newPlaylistName,
                        onValueChange = { newPlaylistName = it },
                        label = { Text("Playlist Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accent,
                            focusedLabelColor = accent
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newPlaylistDesc,
                        onValueChange = { newPlaylistDesc = it },
                        label = { Text("Description (Optional)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accent,
                            focusedLabelColor = accent
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPlaylistName.isNotBlank()) {
                            onCreatePlaylist(newPlaylistName, newPlaylistDesc)
                            newPlaylistName = ""
                            newPlaylistDesc = ""
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accent)
                ) {
                    Text("Create", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel", color = Color.White.copy(alpha = 0.7f))
                }
            },
            containerColor = Color(0xFF1E1E24)
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
    )
}

@Composable
private fun HorizontalSongCard(
    song: Song,
    themeConfig: ThemeConfig,
    onClick: () -> Unit
) {
    val primaryAccent = HeliosColors.parseColor(themeConfig.accentColorHex)
    val secondaryAccent = HeliosColors.parseColor(themeConfig.secondaryAccentHex)

    Column(
        modifier = Modifier
            .width(130.dp)
            .clickable { onClick() }
    ) {
        DynamicArtworkView(
            song = song,
            style = themeConfig.dynamicArtStyle,
            primaryColor = primaryAccent,
            secondaryColor = secondaryAccent,
            modifier = Modifier.size(130.dp),
            cornerRadius = 20.dp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = song.title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = song.artist,
            color = Color.White.copy(alpha = 0.65f),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PlaylistCard(
    playlist: Playlist,
    themeConfig: ThemeConfig,
    onClick: () -> Unit
) {
    val accent = HeliosColors.parseColor(themeConfig.accentColorHex)
    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(accent.copy(alpha = 0.4f), Color(0xFF23232A))
                )
            )
            .clickable { onClick() }
            .padding(14.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Column {
            Text(
                text = playlist.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = playlist.description.ifEmpty { "Playlist" },
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
