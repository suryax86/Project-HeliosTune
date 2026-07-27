package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.data.model.VisualizerStyle
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AudioVisualizerView(
    isPlaying: Boolean,
    isEnabled: Boolean = true,
    style: VisualizerStyle = VisualizerStyle.BARS,
    accentColor: Color = Color(0xFFFFB300),
    spectrum: FloatArray? = null,
    modifier: Modifier = Modifier
) {
    if (!isEnabled) return

    val phase = remember { Animatable(0f) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            phase.animateTo(
                targetValue = 100f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 4000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        } else {
            phase.snapTo(0f)
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val t = phase.value

        when (style) {
            VisualizerStyle.BARS -> {
                val barCount = 24
                val barWidth = width / (barCount * 1.5f)
                val gap = barWidth * 0.5f

                for (i in 0 until barCount) {
                    val realVal = spectrum?.getOrNull(i % (spectrum.size))
                    val factor = if (isPlaying) {
                        realVal ?: (0.2f + 0.8f * sin((i * 0.4f + t * 0.2f).toDouble()).toFloat().let { if (it < 0) -it else it })
                    } else 0.12f

                    val barHeight = (height * factor).coerceAtLeast(4f)
                    val x = i * (barWidth + gap) + gap
                    val y = height - barHeight

                    drawRoundRect(
                        color = accentColor.copy(alpha = 0.85f),
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
                    )
                }
            }
            VisualizerStyle.WAVEFORM -> {
                val path = Path()
                val points = 50
                val step = width / points

                path.moveTo(0f, height / 2f)
                for (i in 0..points) {
                    val x = i * step
                    val bandIdx = (i % (spectrum?.size ?: 1))
                    val specVal = spectrum?.getOrNull(bandIdx)
                    val amp = if (isPlaying) {
                        val baseAmp = specVal?.let { (it - 0.5f) * height * 0.6f }
                            ?: (height * 0.35f * sin((i * 0.2f + t * 0.3f).toDouble())).toFloat()
                        baseAmp
                    } else 0f
                    val y = height / 2f + amp
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = accentColor,
                    style = Stroke(width = 6f)
                )
            }
            VisualizerStyle.CIRCULAR -> {
                val center = Offset(width / 2f, height / 2f)
                val radius = (width.coerceAtMost(height) / 2.5f)
                val totalBars = 36

                for (i in 0 until totalBars) {
                    val angle = (i * (360f / totalBars)) * (Math.PI / 180)
                    val bandIdx = i % (spectrum?.size ?: 1)
                    val specVal = spectrum?.getOrNull(bandIdx)
                    val factor = if (isPlaying) {
                        specVal ?: (0.15f + 0.75f * sin((i * 0.5f + t * 0.25f).toDouble()).toFloat().let { if (it < 0) -it else it })
                    } else 0.08f

                    val len = radius * factor
                    val startX = center.x + (radius * cos(angle)).toFloat()
                    val startY = center.y + (radius * sin(angle)).toFloat()
                    val endX = center.x + ((radius + len) * cos(angle)).toFloat()
                    val endY = center.y + ((radius + len) * sin(angle)).toFloat()

                    drawLine(
                        color = accentColor,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 5f
                    )
                }
            }
            VisualizerStyle.NOTHING_MATRIX -> {
                val cols = 16
                val rows = 8
                val cellWidth = width / cols
                val cellHeight = height / rows

                for (r in 0 until rows) {
                    for (c in 0 until cols) {
                        val specVal = spectrum?.getOrNull(c % (spectrum.size)) ?: 0.5f
                        val activeThreshold = if (isPlaying) {
                            (specVal + sin((r * 0.4f + t * 0.2f).toDouble()) * 0.3).coerceIn(0.0, 1.0)
                        } else 0.1

                        val isDotActive = activeThreshold > 0.45
                        val color = if (isDotActive) accentColor else Color.White.copy(alpha = 0.15f)

                        drawCircle(
                            color = color,
                            radius = (cellWidth.coerceAtMost(cellHeight) * 0.25f),
                            center = Offset(c * cellWidth + cellWidth / 2f, r * cellHeight + cellHeight / 2f)
                        )
                    }
                }
            }
            else -> {
                // Minimal dots
                val dotCount = 12
                val spacing = width / (dotCount + 1)
                for (i in 1..dotCount) {
                    val specVal = spectrum?.getOrNull(i % (spectrum?.size ?: 1))
                    val scale = if (isPlaying) {
                        specVal ?: (0.3f + 0.7f * sin((i * 0.6f + t * 0.3f).toDouble()).toFloat().let { if (it < 0) -it else it })
                    } else 0.2f

                    drawCircle(
                        color = accentColor,
                        radius = 8f * scale,
                        center = Offset(i * spacing, height / 2f)
                    )
                }
            }
        }
    }
}
