package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Song
import com.example.data.model.ThemeConfig
import com.example.theme.HeliosColors

@Composable
fun SongItemCard(
    song: Song,
    themeConfig: ThemeConfig,
    isCurrentPlaying: Boolean,
    onSongClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onPinToggle: () -> Unit,
    onEditMetadata: () -> Unit,
    onAddToQueue: (() -> Unit)? = null,
    onPlayNext: (() -> Unit)? = null,
    onAddToPlaylist: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val accent = HeliosColors.parseColor(themeConfig.accentColorHex)
    var showMenu by remember { mutableStateOf(false) }
    var showSongInfoDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isCurrentPlaying) accent.copy(alpha = 0.15f) else Color.Transparent)
            .clickable { onSongClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(52.dp)) {
            val primaryAccent = HeliosColors.parseColor(themeConfig.accentColorHex)
            val secondaryAccent = HeliosColors.parseColor(themeConfig.secondaryAccentHex)
            DynamicArtworkView(
                song = song,
                style = themeConfig.dynamicArtStyle,
                primaryColor = primaryAccent,
                secondaryColor = secondaryAccent,
                modifier = Modifier.size(52.dp),
                cornerRadius = 14.dp
            )
            if (song.isPinned) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(accent, RoundedCornerShape(topStart = 14.dp, bottomEnd = 8.dp))
                        .padding(2.dp)
                ) {
                    Icon(
                        Icons.Default.PushPin,
                        contentDescription = "Pinned",
                        tint = Color.Black,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                color = if (isCurrentPlaying) accent else Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${song.artist} • ${song.album}",
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(onClick = onFavoriteToggle) {
            Icon(
                imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (song.isFavorite) HeliosColors.HeliosFlame else Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }

        Box {
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Add to play queue") },
                    onClick = {
                        showMenu = false
                        onAddToQueue?.invoke()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Play next") },
                    onClick = {
                        showMenu = false
                        onPlayNext?.invoke()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Add to playlist") },
                    onClick = {
                        showMenu = false
                        onAddToPlaylist?.invoke()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Song info") },
                    onClick = {
                        showMenu = false
                        showSongInfoDialog = true
                    }
                )
                DropdownMenuItem(
                    text = { Text(if (song.isPinned) "Unpin Song" else "Pin Song") },
                    onClick = {
                        showMenu = false
                        onPinToggle()
                    }
                )
            }
        }
    }

    if (showSongInfoDialog) {
        AlertDialog(
            onDismissRequest = { showSongInfoDialog = false },
            title = { Text("Song Info", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Title: ${song.title}", color = Color.White, fontSize = 14.sp)
                    Text("Artist: ${song.artist}", color = Color.White, fontSize = 14.sp)
                    Text("Album: ${song.album}", color = Color.White, fontSize = 14.sp)
                    Text("Genre: ${song.genre.ifEmpty { "Unknown" }}", color = Color.White, fontSize = 14.sp)
                    Text("Duration: ${song.getFormattedDuration()}", color = Color.White, fontSize = 14.sp)
                    Text("Year: ${if (song.year > 0) song.year else "N/A"}", color = Color.White, fontSize = 14.sp)
                    Text("Path: ${song.path.ifEmpty { "Built-in Preset" }}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSongInfoDialog = false
                        onEditMetadata()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accent)
                ) {
                    Text("Edit song info", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSongInfoDialog = false }) {
                    Text("Close", color = Color.White.copy(alpha = 0.7f))
                }
            },
            containerColor = Color(0xFF1E1E24)
        )
    }
}
