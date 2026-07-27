package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Song
import com.example.data.model.ThemeConfig
import com.example.theme.HeliosColors

@Composable
fun NotificationPlayerCard(
    song: Song,
    isPlaying: Boolean,
    progressMs: Long,
    durationMs: Long,
    themeConfig: ThemeConfig,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryAccent = HeliosColors.parseColor(themeConfig.accentColorHex)
    val secondaryAccent = HeliosColors.parseColor(themeConfig.secondaryAccentHex)
    val style = themeConfig.notificationPlayerStyle

    val isAnimations = themeConfig.isAnimationsEnabled
    val isBlur = themeConfig.isBlurEffectEnabled

    // Animation values
    val infiniteTransition = rememberInfiniteTransition(label = "notifAnim")
    val pulseGlow by if (isAnimations && isPlaying) {
        infiniteTransition.animateFloat(
            initialValue = 0.95f,
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseGlow"
        )
    } else {
        animateFloatAsState(targetValue = 1f, label = "staticGlow")
    }

    val wavePhase by if (isAnimations && isPlaying) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 6.28f,
            animationSpec = infiniteRepeatable(
                animation = tween(1800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "wavePhase"
        )
    } else {
        animateFloatAsState(targetValue = 0f, label = "staticWave")
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(themeConfig.cornerRadiusDp.dp))
            .then(
                if (isBlur) {
                    Modifier.border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            listOf(primaryAccent.copy(alpha = 0.5f), secondaryAccent.copy(alpha = 0.3f))
                        ),
                        shape = RoundedCornerShape(themeConfig.cornerRadiusDp.dp)
                    )
                } else Modifier
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        HeliosColors.DarkSurfaceBg,
                        if (themeConfig.isAmoledMode) HeliosColors.AmoledBlack else Color(0xFF14141A)
                    )
                )
            )
            .padding(14.dp)
    ) {
        // Decorative background blur glow layer if blur enabled
        if (isBlur) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = themeConfig.blurRadiusDp.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(primaryAccent.copy(alpha = 0.2f), Color.Transparent)
                        )
                    )
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Row: Notification Header & Style Label
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(primaryAccent)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "HELIOS MEDIA CONTROLLER",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = style.uppercase(),
                    color = primaryAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Middle Main Area: Album Art & Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Album Art
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .scale(pulseGlow)
                        .clip(
                            if (style.contains("Nothing")) RoundedCornerShape(4.dp)
                            else RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.5.dp,
                            brush = Brush.linearGradient(listOf(primaryAccent, secondaryAccent)),
                            shape = if (style.contains("Nothing")) RoundedCornerShape(4.dp) else RoundedCornerShape(12.dp)
                        )
                ) {
                    DynamicArtworkView(
                        song = song,
                        style = themeConfig.dynamicArtStyle,
                        primaryColor = primaryAccent,
                        secondaryColor = secondaryAccent,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Song Title & Artist
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = song.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = if (style.contains("Nothing")) FontFamily.Monospace else FontFamily.Default
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${song.artist} • ${song.album}",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Extra Style-Specific Action (e.g., Like for YouTube Music, Volume for OPPO)
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = if (style.contains("YouTube")) Icons.Default.ThumbUp else Icons.Default.VolumeUp,
                        contentDescription = "Action",
                        tint = secondaryAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Wave/Slider Progress Bar
            val progressFraction = if (durationMs > 0) progressMs.toFloat() / durationMs.toFloat() else 0f
            if (style.contains("YouTube") || style.contains("Aqua")) {
                // Squiggly animated waveform slider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clickable { }
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .align(Alignment.Center)
                    ) {
                        val width = size.width
                        val height = size.height
                        val midY = height / 2f
                        val progressX = width * progressFraction.coerceIn(0f, 1f)

                        val activePath = Path()
                        val inactivePath = Path()

                        // Draw active squiggly line
                        var x = 0f
                        activePath.moveTo(0f, midY)
                        while (x <= progressX) {
                            val y = midY + Math.sin(((x * 0.08f) + wavePhase).toDouble()).toFloat() * 5f
                            activePath.lineTo(x, y)
                            x += 4f
                        }

                        // Draw inactive straight/subtle line
                        inactivePath.moveTo(progressX, midY)
                        inactivePath.lineTo(width, midY)

                        drawPath(
                            path = activePath,
                            brush = Brush.horizontalGradient(listOf(primaryAccent, secondaryAccent)),
                            style = Stroke(width = 3.dp.toPx())
                        )
                        drawPath(
                            path = inactivePath,
                            color = Color.White.copy(alpha = 0.2f),
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Progress Thumb
                        drawCircle(
                            color = primaryAccent,
                            radius = 6.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(progressX, midY)
                        )
                    }
                }
            } else {
                // Clean M3 / Dot progress bar
                Slider(
                    value = progressFraction,
                    onValueChange = { onSeek(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = primaryAccent,
                        activeTrackColor = primaryAccent,
                        inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                    )
                )
            }

            // Bottom Player Control Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = song.getFormattedPosition(progressMs),
                    color = primaryAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onPrevious) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Center Play Button with Dynamic Accent Gradient & Press Effect
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(
                                if (style.contains("Nothing")) RoundedCornerShape(8.dp)
                                else CircleShape
                            )
                            .background(
                                brush = Brush.linearGradient(listOf(primaryAccent, secondaryAccent))
                            )
                            .clickable { onPlayPause() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.Black,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = onNext) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Text(
                    text = song.getFormattedDuration(),
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
