package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.data.model.ProgressBarStyle
import kotlin.math.sin

@Composable
fun CustomProgressBar(
    progressMs: Long,
    durationMs: Long,
    style: ProgressBarStyle,
    accentColor: Color,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val progressRatio = if (durationMs > 0) (progressMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f

    when (style) {
        ProgressBarStyle.SLIDER -> {
            Slider(
                value = progressRatio,
                onValueChange = { frac -> onSeek((frac * durationMs).toLong()) },
                colors = SliderDefaults.colors(
                    thumbColor = accentColor,
                    activeTrackColor = accentColor,
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                ),
                modifier = modifier
            )
        }
        ProgressBarStyle.WAVEFORM -> {
            Canvas(
                modifier = modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .pointerInput(durationMs) {
                        detectTapGestures { offset ->
                            val fraction = (offset.x / size.width).coerceIn(0f, 1f)
                            onSeek((fraction * durationMs).toLong())
                        }
                    }
                    .pointerInput(durationMs) {
                        detectDragGestures { change, _ ->
                            val fraction = (change.position.x / size.width).coerceIn(0f, 1f)
                            onSeek((fraction * durationMs).toLong())
                        }
                    }
            ) {
                val width = size.width
                val height = size.height
                val middleY = height / 2f
                val activeX = width * progressRatio

                val wavePath = Path()
                val activeWavePath = Path()

                val barsCount = 32
                val barWidth = width / (barsCount * 1.5f)
                val spacing = barWidth * 0.5f

                for (i in 0 until barsCount) {
                    val x = i * (barWidth + spacing) + barWidth / 2f
                    val waveHeight = (sin(i * 0.5f) * 0.4f + 0.6f) * (height * 0.7f)
                    val topY = middleY - waveHeight / 2f
                    val bottomY = middleY + waveHeight / 2f

                    val color = if (x <= activeX) accentColor else Color.White.copy(alpha = 0.25f)
                    drawLine(
                        color = color,
                        start = Offset(x, topY),
                        end = Offset(x, bottomY),
                        strokeWidth = barWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
        ProgressBarStyle.SEGMENTED -> {
            Canvas(
                modifier = modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .pointerInput(durationMs) {
                        detectTapGestures { offset ->
                            val fraction = (offset.x / size.width).coerceIn(0f, 1f)
                            onSeek((fraction * durationMs).toLong())
                        }
                    }
                    .pointerInput(durationMs) {
                        detectDragGestures { change, _ ->
                            val fraction = (change.position.x / size.width).coerceIn(0f, 1f)
                            onSeek((fraction * durationMs).toLong())
                        }
                    }
            ) {
                val width = size.width
                val height = size.height
                val count = 20
                val totalSpacing = width * 0.15f
                val segmentWidth = (width - totalSpacing) / count
                val spacing = totalSpacing / (count - 1)
                val activeIndex = (progressRatio * count).toInt()

                for (i in 0 until count) {
                    val x = i * (segmentWidth + spacing)
                    val isActive = i <= activeIndex
                    val segColor = if (isActive) accentColor else Color.White.copy(alpha = 0.2f)

                    drawRoundRect(
                        color = segColor,
                        topLeft = Offset(x, height / 4f),
                        size = Size(segmentWidth, height / 2f),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )
                }
            }
        }
        ProgressBarStyle.THIN_LINE -> {
            Canvas(
                modifier = modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .pointerInput(durationMs) {
                        detectTapGestures { offset ->
                            val fraction = (offset.x / size.width).coerceIn(0f, 1f)
                            onSeek((fraction * durationMs).toLong())
                        }
                    }
                    .pointerInput(durationMs) {
                        detectDragGestures { change, _ ->
                            val fraction = (change.position.x / size.width).coerceIn(0f, 1f)
                            onSeek((fraction * durationMs).toLong())
                        }
                    }
            ) {
                val width = size.width
                val height = size.height
                val activeX = width * progressRatio

                // Inactive Track
                drawLine(
                    color = Color.White.copy(alpha = 0.2f),
                    start = Offset(0f, height / 2f),
                    end = Offset(width, height / 2f),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Active Track
                drawLine(
                    color = accentColor,
                    start = Offset(0f, height / 2f),
                    end = Offset(activeX, height / 2f),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Indicator Dot
                drawCircle(
                    color = Color.White,
                    radius = 6.dp.toPx(),
                    center = Offset(activeX, height / 2f)
                )
                drawCircle(
                    color = accentColor,
                    radius = 4.dp.toPx(),
                    center = Offset(activeX, height / 2f)
                )
            }
        }
        ProgressBarStyle.GLOW_BAR -> {
            Canvas(
                modifier = modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .pointerInput(durationMs) {
                        detectTapGestures { offset ->
                            val fraction = (offset.x / size.width).coerceIn(0f, 1f)
                            onSeek((fraction * durationMs).toLong())
                        }
                    }
                    .pointerInput(durationMs) {
                        detectDragGestures { change, _ ->
                            val fraction = (change.position.x / size.width).coerceIn(0f, 1f)
                            onSeek((fraction * durationMs).toLong())
                        }
                    }
            ) {
                val width = size.width
                val height = size.height
                val activeX = width * progressRatio

                // Outer Track
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.15f),
                    topLeft = Offset(0f, height / 3f),
                    size = Size(width, height / 3f),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )

                // Glowing Active Track
                if (activeX > 0) {
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(accentColor.copy(alpha = 0.7f), accentColor)
                        ),
                        topLeft = Offset(0f, height / 3f),
                        size = Size(activeX, height / 3f),
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                    )

                    // Head Glow Circle
                    drawCircle(
                        color = accentColor.copy(alpha = 0.4f),
                        radius = 12.dp.toPx(),
                        center = Offset(activeX, height / 2f)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 6.dp.toPx(),
                        center = Offset(activeX, height / 2f)
                    )
                }
            }
        }
        ProgressBarStyle.VINTAGE_TAPE -> {
            Canvas(
                modifier = modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .pointerInput(durationMs) {
                        detectTapGestures { offset ->
                            val fraction = (offset.x / size.width).coerceIn(0f, 1f)
                            onSeek((fraction * durationMs).toLong())
                        }
                    }
                    .pointerInput(durationMs) {
                        detectDragGestures { change, _ ->
                            val fraction = (change.position.x / size.width).coerceIn(0f, 1f)
                            onSeek((fraction * durationMs).toLong())
                        }
                    }
            ) {
                val width = size.width
                val height = size.height
                val activeX = width * progressRatio

                // Dual Track Gauge
                drawLine(
                    color = Color.White.copy(alpha = 0.2f),
                    start = Offset(0f, height * 0.35f),
                    end = Offset(width, height * 0.35f),
                    strokeWidth = 2.dp.toPx()
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.2f),
                    start = Offset(0f, height * 0.65f),
                    end = Offset(width, height * 0.65f),
                    strokeWidth = 2.dp.toPx()
                )

                // Fill area between gauge lines
                if (activeX > 0) {
                    drawRect(
                        color = accentColor.copy(alpha = 0.8f),
                        topLeft = Offset(0f, height * 0.35f),
                        size = Size(activeX, height * 0.3f)
                    )
                }

                // Ticks along the gauge
                val ticks = 16
                val tickStep = width / ticks
                for (i in 0..ticks) {
                    val x = i * tickStep
                    val tickColor = if (x <= activeX) accentColor else Color.White.copy(alpha = 0.4f)
                    drawLine(
                        color = tickColor,
                        start = Offset(x, height * 0.2f),
                        end = Offset(x, height * 0.8f),
                        strokeWidth = 1.5.dp.toPx()
                    )
                }
            }
        }
    }
}
