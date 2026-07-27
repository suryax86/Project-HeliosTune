package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DynamicArtStyle
import com.example.theme.HeliosColors
import kotlin.math.abs

@Composable
fun DynamicArtworkView(
    title: String,
    artist: String,
    style: DynamicArtStyle = DynamicArtStyle.GRADIENT,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp
) {
    val hash = abs((title + artist).hashCode())
    val palette = getPaletteForHash(hash)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(palette.first)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            when (style) {
                DynamicArtStyle.GRADIENT -> {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(palette.second, palette.first, palette.third),
                            center = Offset(width * 0.3f, height * 0.3f),
                            radius = width * 0.9f
                        )
                    )
                }
                DynamicArtStyle.VINYL -> {
                    drawRect(color = Color(0xFF121212))
                    // Draw vinyl rings
                    val center = Offset(width / 2f, height / 2f)
                    for (r in 1..8) {
                        drawCircle(
                            color = Color(0x22FFFFFF),
                            radius = (width / 2f) * (r / 9f),
                            center = center,
                            style = Stroke(width = 2f)
                        )
                    }
                    // Inner label
                    drawCircle(
                        color = palette.second,
                        radius = width * 0.18f,
                        center = center
                    )
                    drawCircle(
                        color = Color.Black,
                        radius = width * 0.04f,
                        center = center
                    )
                }
                DynamicArtStyle.SUNSET -> {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(palette.second, palette.third, palette.first)
                        )
                    )
                    // Sun circle
                    drawCircle(
                        color = palette.second.copy(alpha = 0.9f),
                        radius = width * 0.22f,
                        center = Offset(width / 2f, height * 0.45f)
                    )
                    // Reflection lines
                    for (i in 0..5) {
                        val y = height * (0.6f + i * 0.06f)
                        drawLine(
                            color = Color.White.copy(alpha = 0.3f),
                            start = Offset(width * (0.2f + i * 0.03f), y),
                            end = Offset(width * (0.8f - i * 0.03f), y),
                            strokeWidth = 3f
                        )
                    }
                }
                DynamicArtStyle.AURORA -> {
                    drawRect(color = Color(0xFF090A10))
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(palette.second.copy(alpha = 0.8f), Color.Transparent),
                            center = Offset(width * 0.2f, height * 0.3f),
                            radius = width * 0.7f
                        )
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(palette.third.copy(alpha = 0.7f), Color.Transparent),
                            center = Offset(width * 0.8f, height * 0.7f),
                            radius = width * 0.7f
                        )
                    )
                }
                DynamicArtStyle.ABSTRACT_SHAPES -> {
                    drawRect(color = palette.first)
                    drawCircle(
                        color = palette.second.copy(alpha = 0.6f),
                        radius = width * 0.35f,
                        center = Offset(width * 0.2f, height * 0.2f)
                    )
                    drawRect(
                        color = palette.third.copy(alpha = 0.5f),
                        topLeft = Offset(width * 0.4f, height * 0.4f),
                        size = Size(width * 0.5f, height * 0.5f)
                    )
                }
                DynamicArtStyle.CASSETTE -> {
                    drawRect(color = Color(0xFF26262B))
                    val inset = width * 0.1f
                    drawRect(
                        color = palette.second,
                        topLeft = Offset(inset, inset),
                        size = Size(width - inset * 2, height - inset * 2)
                    )
                    // Spools window
                    val winWidth = width * 0.5f
                    val winHeight = height * 0.25f
                    val winLeft = (width - winWidth) / 2f
                    val winTop = (height - winHeight) / 2f
                    drawRect(
                        color = Color(0xFF111111),
                        topLeft = Offset(winLeft, winTop),
                        size = Size(winWidth, winHeight)
                    )
                    drawCircle(color = Color.White, radius = winHeight * 0.3f, center = Offset(winLeft + winWidth * 0.25f, winTop + winHeight / 2f))
                    drawCircle(color = Color.White, radius = winHeight * 0.3f, center = Offset(winLeft + winWidth * 0.75f, winTop + winHeight / 2f))
                }
                else -> {
                    // Default Linear gradient
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(palette.first, palette.second, palette.third)
                        )
                    )
                }
            }
        }

        // Overlay Title and Artist for Lettermark & Text Styles
        if (style != DynamicArtStyle.VINYL && style != DynamicArtStyle.CASSETTE) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = title.takeIf { it.isNotEmpty() } ?: "Helios",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun getPaletteForHash(hash: Int): Triple<Color, Color, Color> {
    val palettes = listOf(
        Triple(Color(0xFF1A1A2E), Color(0xFFFFB300), Color(0xFFE91E63)),
        Triple(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)),
        Triple(Color(0xFF23074D), Color(0xFFCC5333), Color(0xFFFF8000)),
        Triple(Color(0xFF1D976C), Color(0xFF93F9B9), Color(0xFF00B0FF)),
        Triple(Color(0xFF4568DC), Color(0xFFB06AB3), Color(0xFFFF4081)),
        Triple(Color(0xFF1F1C2C), Color(0xFF928DAB), Color(0xFF00E5FF)),
        Triple(Color(0xFF141E30), Color(0xFF243B55), Color(0xFFFFD600))
    )
    return palettes[hash % palettes.size]
}
