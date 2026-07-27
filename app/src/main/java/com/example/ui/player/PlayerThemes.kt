package com.example.ui.player

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlayerTheme
import com.example.data.model.Song
import com.example.data.model.ThemeConfig
import com.example.theme.HeliosColors
import com.example.ui.components.AudioVisualizerView
import com.example.ui.components.CustomPlayButton
import com.example.ui.components.CustomProgressBar
import com.example.ui.components.DynamicArtworkView

@Composable
fun MainPlayerView(
    song: Song,
    isPlaying: Boolean,
    progressMs: Long,
    durationMs: Long,
    themeConfig: ThemeConfig,
    isFavorite: Boolean,
    isShuffle: Boolean,
    repeatModeState: String,
    onPlayPauseToggle: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSeek: (Long) -> Unit,
    onFavoriteToggle: () -> Unit,
    onShuffleToggle: () -> Unit,
    onRepeatToggle: () -> Unit,
    onOpenQueue: () -> Unit,
    onOpenAudioSettings: () -> Unit,
    onCollapse: (() -> Unit)? = null,
    spectrum: FloatArray? = null,
    modifier: Modifier = Modifier
) {
    val primaryAccent = HeliosColors.parseColor(themeConfig.accentColorHex)
    val secondaryAccent = HeliosColors.parseColor(themeConfig.secondaryAccentHex)
    val bg = if (themeConfig.isAmoledMode) HeliosColors.AmoledBlack else HeliosColors.DarkCardBg

    // Rotating album art animation if enabled
    val rotationAngle = remember { Animatable(0f) }
    LaunchedEffect(isPlaying) {
        if (isPlaying && themeConfig.isRotateArtwork && themeConfig.isAnimationsEnabled) {
            rotationAngle.animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 12000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        } else {
            rotationAngle.snapTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .padding(20.dp)
    ) {
        // Ambient background blur glow if enabled
        if (themeConfig.isBlurEffectEnabled) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(primaryAccent.copy(alpha = 0.25f), secondaryAccent.copy(alpha = 0.15f), Color.Transparent)
                        )
                    )
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onCollapse != null) {
                        IconButton(onClick = onCollapse) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Minimize Player", tint = Color.White)
                        }
                    }
                    Text(
                        text = "HeliosTune",
                        color = primaryAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Row {
                    IconButton(onClick = onFavoriteToggle) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) HeliosColors.HeliosFlame else Color.White
                        )
                    }
                    IconButton(onClick = onOpenAudioSettings) {
                        Icon(Icons.Default.Speed, contentDescription = "Audio Settings", tint = Color.White)
                    }
                    IconButton(onClick = onOpenQueue) {
                        Icon(Icons.AutoMirrored.Filled.QueueMusic, contentDescription = "Queue", tint = Color.White)
                    }
                }
            }

            // Theme Content Layout
            when (themeConfig.playerTheme) {
                PlayerTheme.VINYL -> {
                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .rotate(rotationAngle.value),
                        contentAlignment = Alignment.Center
                    ) {
                        DynamicArtworkView(
                            song = song,
                            style = themeConfig.dynamicArtStyle,
                            primaryColor = primaryAccent,
                            secondaryColor = secondaryAccent,
                            modifier = Modifier.size(260.dp),
                            cornerRadius = 130.dp
                        )
                    }
                }
                PlayerTheme.NOTHING -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .border(2.dp, primaryAccent, RoundedCornerShape(24.dp))
                                .padding(12.dp)
                        ) {
                            DynamicArtworkView(
                                song = song,
                                style = themeConfig.dynamicArtStyle,
                                primaryColor = primaryAccent,
                                secondaryColor = secondaryAccent,
                                modifier = Modifier.fillMaxSize(),
                                cornerRadius = 16.dp
                            )
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .clip(RoundedCornerShape(themeConfig.cornerRadiusDp.dp))
                    ) {
                        DynamicArtworkView(
                            song = song,
                            style = themeConfig.dynamicArtStyle,
                            primaryColor = primaryAccent,
                            secondaryColor = secondaryAccent,
                            modifier = Modifier.fillMaxSize(),
                            cornerRadius = themeConfig.cornerRadiusDp.dp
                        )
                    }
                }
            }

            // Visualizer Canvas
            if (themeConfig.isVisualizerEnabled) {
                AudioVisualizerView(
                    isPlaying = isPlaying,
                    isEnabled = themeConfig.isVisualizerEnabled,
                    style = themeConfig.visualizerStyle,
                    accentColor = primaryAccent,
                    spectrum = spectrum,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Song Information
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = song.title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${song.artist} • ${song.album}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Progress Slider
            Column(modifier = Modifier.fillMaxWidth()) {
                CustomProgressBar(
                    progressMs = progressMs,
                    durationMs = durationMs,
                    style = themeConfig.progressBarStyle,
                    accentColor = primaryAccent,
                    onSeek = onSeek,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(progressMs),
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = formatTime(durationMs),
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }

            // Playback Control Buttons (Shuffle, Prev, Play/Pause, Next, Repeat)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onShuffleToggle) {
                    Icon(
                        Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (isShuffle) primaryAccent else Color.White.copy(alpha = 0.5f)
                    )
                }

                IconButton(onClick = onSkipPrevious) {
                    Icon(
                        Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                CustomPlayButton(
                    isPlaying = isPlaying,
                    style = themeConfig.playButtonStyle,
                    accentColor = primaryAccent,
                    onClick = onPlayPauseToggle
                )

                IconButton(onClick = onSkipNext) {
                    Icon(
                        Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = onRepeatToggle) {
                    Icon(
                        imageVector = if (repeatModeState == "ONE") Icons.Default.RepeatOne else Icons.Default.Repeat,
                        contentDescription = "Repeat",
                        tint = if (repeatModeState != "OFF") primaryAccent else Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
