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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ThemeConfig
import com.example.player.AudioPlayerManager
import com.example.theme.HeliosColors

@Composable
fun AudioSettingsDialog(
    audioPlayer: AudioPlayerManager,
    themeConfig: ThemeConfig,
    sleepTimerMinutesLeft: Int?,
    onDismiss: () -> Unit
) {
    val accent = HeliosColors.parseColor(themeConfig.accentColorHex)
    var speed by remember { mutableFloatStateOf(audioPlayer.playbackSpeed.value) }
    var pitch by remember { mutableFloatStateOf(audioPlayer.pitch.value) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1E1E24),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Audio & Playback Controls",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Speed Slider
                Text(
                    text = "Playback Speed: ${String.format("%.2f", speed)}x",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Slider(
                    value = speed,
                    onValueChange = {
                        speed = it
                        audioPlayer.setPlaybackSpeed(it)
                    },
                    valueRange = 0.5f..2.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = accent,
                        activeTrackColor = accent,
                        inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Pitch Slider
                Text(
                    text = "Pitch Shift: ${String.format("%.2f", pitch)}x",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Slider(
                    value = pitch,
                    onValueChange = {
                        pitch = it
                        audioPlayer.setPitch(it)
                    },
                    valueRange = 0.5f..1.5f,
                    colors = SliderDefaults.colors(
                        thumbColor = accent,
                        activeTrackColor = accent,
                        inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Sleep Timer Section
                Text(
                    text = if (sleepTimerMinutesLeft != null) "Sleep Timer: $sleepTimerMinutesLeft mins remaining" else "Sleep Timer",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val timerOptions = listOf(15, 30, 45, 60)
                    timerOptions.forEach { mins ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(accent.copy(alpha = 0.2f))
                                .clickable { audioPlayer.startSleepTimer(mins) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text("${mins}m", color = accent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    if (sleepTimerMinutesLeft != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(HeliosColors.HeliosFlame.copy(alpha = 0.2f))
                                .clickable { audioPlayer.cancelSleepTimer() }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text("Off", color = HeliosColors.HeliosFlame, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = accent)
                    ) {
                        Text("Done", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
