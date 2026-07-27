package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlayButtonStyle

@Composable
fun CustomPlayButton(
    isPlaying: Boolean,
    style: PlayButtonStyle,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow
    val contentDescription = if (isPlaying) "Pause" else "Play"

    when (style) {
        PlayButtonStyle.CIRCULAR_FILLED -> {
            Box(
                modifier = modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(accentColor)
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        PlayButtonStyle.GLASS_NEUMORPHIC -> {
            Box(
                modifier = modifier
                    .size(64.dp)
                    .shadow(12.dp, CircleShape, spotColor = accentColor)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.25f),
                                Color.White.copy(alpha = 0.05f)
                            )
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(accentColor, accentColor.copy(alpha = 0.3f))
                        ),
                        shape = CircleShape
                    )
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }
        }
        PlayButtonStyle.MINIMAL_OUTLINE -> {
            Box(
                modifier = modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(width = 2.dp, color = accentColor, shape = CircleShape)
                    .background(Color.Transparent)
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = accentColor,
                    modifier = Modifier.size(34.dp)
                )
            }
        }
        PlayButtonStyle.RETRO_SQUARE -> {
            Box(
                modifier = modifier
                    .size(60.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF23232C))
                    .border(width = 1.dp, color = accentColor.copy(alpha = 0.6f), shape = RoundedCornerShape(16.dp))
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = accentColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        PlayButtonStyle.FLOATING_PILL -> {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(accentColor)
                    .clickable { onClick() }
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isPlaying) "PAUSE" else "PLAY",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
        PlayButtonStyle.CYBER_GLOW -> {
            Box(
                modifier = modifier
                    .size(64.dp)
                    .shadow(16.dp, CircleShape, spotColor = accentColor, ambientColor = accentColor)
                    .clip(CircleShape)
                    .background(Color(0xFF101014))
                    .border(width = 2.dp, color = accentColor, shape = CircleShape)
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = accentColor,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
