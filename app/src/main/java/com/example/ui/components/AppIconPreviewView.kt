package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.AppIconOption
import com.example.theme.HeliosColors
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AppIconPreviewView(
    option: AppIconOption,
    isSelected: Boolean,
    accentColor: Color,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 64.dp
) {
    val primary = HeliosColors.parseColor(option.primaryColorHex)
    val secondary = HeliosColors.parseColor(option.secondaryColorHex)

    Box(
        modifier = modifier
            .size(sizeDp)
            .clip(RoundedCornerShape(sizeDp * 0.28f))
            .background(Color(0xFF0D0D11))
            .border(
                width = if (isSelected) 2.5.dp else 1.dp,
                color = if (isSelected) accentColor else Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(sizeDp * 0.28f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = this.size.width
            val height = this.size.height
            val center = Offset(width / 2f, height / 2f)
            val radius = width * 0.32f

            when (option.symbolType) {
                "orbit_ring" -> {
                    // Gradient Ring with Orbital Satellite
                    drawCircle(
                        brush = Brush.sweepGradient(listOf(primary, secondary, primary)),
                        radius = radius,
                        center = center,
                        style = Stroke(width = 2.5.dp.toPx())
                    )
                    val angle = -Math.PI / 4
                    val satelliteX = center.x + radius * cos(angle).toFloat()
                    val satelliteY = center.y + radius * sin(angle).toFloat()
                    drawCircle(color = primary, radius = 4.dp.toPx(), center = Offset(satelliteX, satelliteY))
                }
                "dot_orbit" -> {
                    // Crisp White Ring with Satellite Dot
                    drawCircle(
                        color = Color.White,
                        radius = radius,
                        center = center,
                        style = Stroke(width = 2.dp.toPx())
                    )
                    val satelliteX = center.x + radius * cos(Math.PI / 6).toFloat()
                    val satelliteY = center.y - radius * sin(Math.PI / 6).toFloat()
                    drawCircle(color = Color.White, radius = 5.dp.toPx(), center = Offset(satelliteX, satelliteY))
                }
                "star_crescent" -> {
                    // Crescent Ring + Star
                    drawArc(
                        color = primary,
                        startAngle = 40f,
                        sweepAngle = 280f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2f, radius * 2f),
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                    // Star
                    val starX = center.x + radius * 0.7f
                    val starY = center.y - radius * 0.3f
                    val starPath = Path().apply {
                        moveTo(starX, starY - 6.dp.toPx())
                        quadraticTo(starX, starY, starX + 6.dp.toPx(), starY)
                        quadraticTo(starX, starY, starX, starY + 6.dp.toPx())
                        quadraticTo(starX, starY, starX - 6.dp.toPx(), starY)
                        quadraticTo(starX, starY, starX, starY - 6.dp.toPx())
                    }
                    drawPath(starPath, color = primary)
                }
                "spectrum_bars" -> {
                    // 5 Equalizer Bars
                    val barWidth = 3.dp.toPx()
                    val heights = listOf(0.4f, 0.9f, 0.6f, 0.85f, 0.45f)
                    val colors = listOf(primary, primary, secondary, secondary, primary)
                    val totalW = barWidth * 5 + 4.dp.toPx() * 4
                    val startX = center.x - totalW / 2f + barWidth / 2f

                    for (i in 0..4) {
                        val h = height * 0.5f * heights[i]
                        val x = startX + i * (barWidth + 4.dp.toPx())
                        drawLine(
                            color = colors[i],
                            start = Offset(x, center.y - h / 2f),
                            end = Offset(x, center.y + h / 2f),
                            strokeWidth = barWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }
                "multi_orbit" -> {
                    // Concentric Orbital Arcs
                    for (i in 1..3) {
                        val r = radius * (0.4f + i * 0.22f)
                        drawArc(
                            color = if (i % 2 == 0) secondary else primary,
                            startAngle = 120f,
                            sweepAngle = 260f,
                            useCenter = false,
                            topLeft = Offset(center.x - r, center.y - r),
                            size = Size(r * 2f, r * 2f),
                            style = Stroke(width = 1.8.dp.toPx())
                        )
                    }
                }
                "play_symbol" -> {
                    // Vertical Bar + Play Triangle
                    drawLine(
                        color = Color.White,
                        start = Offset(center.x - 8.dp.toPx(), center.y - 12.dp.toPx()),
                        end = Offset(center.x - 8.dp.toPx(), center.y + 12.dp.toPx()),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    val trianglePath = Path().apply {
                        moveTo(center.x - 2.dp.toPx(), center.y - 12.dp.toPx())
                        lineTo(center.x + 12.dp.toPx(), center.y)
                        lineTo(center.x - 2.dp.toPx(), center.y + 12.dp.toPx())
                        close()
                    }
                    drawPath(trianglePath, color = Color.White)
                }
                "sunset_horizon" -> {
                    // Sunset Arc
                    drawArc(
                        color = primary,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius * 0.8f),
                        size = Size(radius * 2f, radius * 2f),
                        style = Stroke(width = 2.dp.toPx())
                    )
                    // Horizon Reflection lines
                    for (i in 1..3) {
                        val lineY = center.y + i * 4.dp.toPx()
                        val w = radius * (1f - i * 0.2f)
                        drawLine(
                            color = secondary,
                            start = Offset(center.x - w, lineY),
                            end = Offset(center.x + w, lineY),
                            strokeWidth = 1.5.dp.toPx()
                        )
                    }
                }
                "dotted_ring" -> {
                    // Ring of Dots
                    val dotCount = 14
                    for (i in 0 until dotCount) {
                        val angle = (2 * Math.PI / dotCount) * i
                        val x = center.x + radius * cos(angle).toFloat()
                        val y = center.y + radius * sin(angle).toFloat()
                        val dotRadius = if (i == 3) 4.dp.toPx() else 1.8.dp.toPx()
                        val dotColor = if (i < 7) primary else secondary
                        drawCircle(color = dotColor, radius = dotRadius, center = Offset(x, y))
                    }
                }
                "note_circle" -> {
                    // Circle + Music Note
                    drawCircle(
                        color = primary,
                        radius = radius,
                        center = center,
                        style = Stroke(width = 1.8.dp.toPx())
                    )
                    val noteX = center.x - 3.dp.toPx()
                    val noteY = center.y + 4.dp.toPx()
                    drawCircle(color = primary, radius = 4.dp.toPx(), center = Offset(noteX, noteY))
                    drawLine(
                        color = primary,
                        start = Offset(noteX + 3.dp.toPx(), noteY),
                        end = Offset(noteX + 3.dp.toPx(), noteY - 14.dp.toPx()),
                        strokeWidth = 2.dp.toPx()
                    )
                    val flagPath = Path().apply {
                        moveTo(noteX + 3.dp.toPx(), noteY - 14.dp.toPx())
                        lineTo(noteX + 9.dp.toPx(), noteY - 10.dp.toPx())
                        lineTo(noteX + 3.dp.toPx(), noteY - 6.dp.toPx())
                        close()
                    }
                    drawPath(flagPath, color = primary)
                }
                "gradient_ring" -> {
                    drawCircle(
                        brush = Brush.sweepGradient(listOf(primary, secondary, primary)),
                        radius = radius,
                        center = center,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
                else -> {
                    // Fallback ring / dot
                    drawCircle(
                        color = primary,
                        radius = radius,
                        center = center,
                        style = Stroke(width = 2.5.dp.toPx())
                    )
                    drawCircle(color = secondary, radius = 6.dp.toPx(), center = center)
                }
            }
        }
    }
}
