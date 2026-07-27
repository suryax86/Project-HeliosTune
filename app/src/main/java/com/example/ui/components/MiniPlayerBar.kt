package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Song
import com.example.data.model.ThemeConfig
import com.example.theme.HeliosColors

@Composable
fun MiniPlayerBar(
    song: Song,
    isPlaying: Boolean,
    progressMs: Long,
    durationMs: Long,
    themeConfig: ThemeConfig,
    onPlayPauseToggle: () -> Unit,
    onSkipNext: () -> Unit,
    onClickPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryAccent = HeliosColors.parseColor(themeConfig.accentColorHex)
    val secondaryAccent = HeliosColors.parseColor(themeConfig.secondaryAccentHex)
    val progress = if (durationMs > 0) progressMs.toFloat() / durationMs.toFloat() else 0f
    val isBlur = themeConfig.isBlurEffectEnabled

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .then(
                if (isBlur) {
                    Modifier
                        .blur(radius = 0.dp) // Maintain container boundary while child backdrop blurs
                        .border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(listOf(primaryAccent.copy(alpha = 0.6f), secondaryAccent.copy(alpha = 0.3f))),
                            shape = RoundedCornerShape(20.dp)
                        )
                } else Modifier
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF1E1E26),
                        if (themeConfig.isAmoledMode) HeliosColors.AmoledBlack else Color(0xFF14141C)
                    )
                )
            )
            .clickable { onClickPlayer() }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Album Art with dual accent pass-through
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    DynamicArtworkView(
                        song = song,
                        style = themeConfig.dynamicArtStyle,
                        primaryColor = primaryAccent,
                        secondaryColor = secondaryAccent,
                        modifier = Modifier.size(48.dp),
                        cornerRadius = 12.dp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title & Artist
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = song.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = song.artist,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Controls
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onPlayPauseToggle,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(listOf(primaryAccent, secondaryAccent))
                            )
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.Black,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = onSkipNext) {
                        Icon(
                            Icons.Default.SkipNext,
                            contentDescription = "Next",
                            tint = Color.White
                        )
                    }
                }
            }

            // Bottom Progress Bar with Accent Gradient
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = primaryAccent,
                trackColor = Color.White.copy(alpha = 0.1f)
            )
        }
    }
}
