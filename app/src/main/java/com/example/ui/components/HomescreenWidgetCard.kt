package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Song
import com.example.data.model.ThemeConfig
import com.example.theme.HeliosColors

@Composable
fun HomescreenWidgetCard(
    song: Song,
    isPlaying: Boolean,
    progressMs: Long,
    durationMs: Long,
    themeConfig: ThemeConfig,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryAccent = HeliosColors.parseColor(themeConfig.accentColorHex)
    val secondaryAccent = HeliosColors.parseColor(themeConfig.secondaryAccentHex)
    val widgetStyle = themeConfig.widgetStyle

    val isAnimations = themeConfig.isAnimationsEnabled
    val isBlur = themeConfig.isBlurEffectEnabled

    val infiniteTransition = rememberInfiniteTransition(label = "widgetAnim")
    val rotationAngle by if (isAnimations && isPlaying) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(6000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "widgetDiscRotate"
        )
    } else {
        infiniteTransition.animateFloat(initialValue = 0f, targetValue = 0f, animationSpec = infiniteRepeatable(tween(1)), label = "noRotate")
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1B1B22),
                        if (themeConfig.isAmoledMode) HeliosColors.AmoledBlack else Color(0xFF0F0F14)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(listOf(primaryAccent.copy(alpha = 0.6f), secondaryAccent.copy(alpha = 0.4f))),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        if (isBlur) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = themeConfig.blurRadiusDp.dp)
                    .background(primaryAccent.copy(alpha = 0.08f))
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            // Widget Title & Header Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Radio,
                        contentDescription = "Widget",
                        tint = primaryAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ANDROID HOMESCREEN WIDGET",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(primaryAccent.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = widgetStyle,
                        color = primaryAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            when {
                widgetStyle.contains("Nothing") -> {
                    // Nothing OS Dot Matrix Widget Layout (4x2)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, primaryAccent, RoundedCornerShape(12.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "●",
                                color = primaryAccent,
                                fontSize = 28.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = song.title,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = song.artist,
                                color = primaryAccent,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onPrevious) {
                                Icon(Icons.Default.SkipPrevious, "Prev", tint = Color.White)
                            }
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(primaryAccent)
                                    .clickable { onPlayPause() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    "Play",
                                    tint = Color.Black
                                )
                            }
                            IconButton(onClick = onNext) {
                                Icon(Icons.Default.SkipNext, "Next", tint = Color.White)
                            }
                        }
                    }
                }

                widgetStyle.contains("Cassette") || widgetStyle.contains("Archive") -> {
                    // ArchiveTune Vintage Cassette Widget
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "TAPE #01 • RETRO PLAY",
                                color = secondaryAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = song.title,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = song.artist,
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }

                        // Spinning Reel Icon
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .rotate(rotationAngle)
                                .clip(CircleShape)
                                .background(Brush.radialGradient(listOf(primaryAccent, Color.Black))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MusicNote, "Tape", tint = Color.Black, modifier = Modifier.size(24.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(primaryAccent, secondaryAccent)))
                                .clickable { onPlayPause() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                "Play",
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                widgetStyle.contains("Liquid") || widgetStyle.contains("OPPO") -> {
                    // OPPO Music Liquid Capsule Widget (4x1)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(primaryAccent.copy(alpha = 0.25f), secondaryAccent.copy(alpha = 0.25f))
                                )
                            )
                            .border(1.dp, primaryAccent.copy(alpha = 0.5f), CircleShape)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(primaryAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.GraphicEq, "Eq", tint = Color.Black)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = song.title,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = song.artist,
                                    color = primaryAccent,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onPrevious) {
                                Icon(Icons.Default.SkipPrevious, "Prev", tint = Color.White)
                            }
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(secondaryAccent)
                                    .clickable { onPlayPause() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    "Play",
                                    tint = Color.Black,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(onClick = onNext) {
                                Icon(Icons.Default.SkipNext, "Next", tint = Color.White)
                            }
                        }
                    }
                }

                else -> {
                    // YouTube Music Dynamic Wave Widget (Default 4x2)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, Brush.linearGradient(listOf(primaryAccent, secondaryAccent)), RoundedCornerShape(16.dp))
                        ) {
                            DynamicArtworkView(
                                song = song,
                                style = themeConfig.dynamicArtStyle,
                                primaryColor = primaryAccent,
                                secondaryColor = secondaryAccent,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = song.title,
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = song.artist,
                                color = secondaryAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = onPrevious, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.SkipPrevious, "Prev", tint = Color.White)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(listOf(primaryAccent, secondaryAccent)))
                                        .clickable { onPlayPause() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        "Play",
                                        tint = Color.Black,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = onNext, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.SkipNext, "Next", tint = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
