package com.example.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.data.model.AppIconPresets
import com.example.data.model.DynamicArtStyle
import com.example.data.model.PlayerTheme
import com.example.data.model.ThemeConfig
import com.example.data.model.VisualizerStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HeliosThemeManager {
    private val _config = MutableStateFlow(ThemeConfig())
    val config: StateFlow<ThemeConfig> = _config.asStateFlow()

    fun updatePlayerTheme(theme: PlayerTheme) {
        _config.value = _config.value.copy(playerTheme = theme)
    }

    fun updateAccentColor(hex: String) {
        _config.value = _config.value.copy(accentColorHex = hex)
    }

    fun toggleAmoledMode(enabled: Boolean) {
        _config.value = _config.value.copy(isAmoledMode = enabled)
    }

    fun toggleMaterialYou(enabled: Boolean) {
        _config.value = _config.value.copy(isMaterialYou = enabled)
    }

    fun updateCornerRadius(radiusDp: Int) {
        _config.value = _config.value.copy(cornerRadiusDp = radiusDp)
    }

    fun updateProgressBarStyle(style: com.example.data.model.ProgressBarStyle) {
        _config.value = _config.value.copy(progressBarStyle = style)
    }

    fun updatePlayButtonStyle(style: com.example.data.model.PlayButtonStyle) {
        _config.value = _config.value.copy(playButtonStyle = style)
    }

    fun updateVisualizerStyle(style: VisualizerStyle) {
        _config.value = _config.value.copy(visualizerStyle = style)
    }

    fun toggleVisualizer(enabled: Boolean) {
        _config.value = _config.value.copy(isVisualizerEnabled = enabled)
    }

    fun updateDynamicArtStyle(style: DynamicArtStyle) {
        _config.value = _config.value.copy(dynamicArtStyle = style)
    }

    fun updateAppIcon(iconId: String) {
        _config.value = _config.value.copy(appIconId = iconId)
    }

    fun toggleRotateArtwork(enabled: Boolean) {
        _config.value = _config.value.copy(isRotateArtwork = enabled)
    }

    fun toggleGaplessPlayback(enabled: Boolean) {
        _config.value = _config.value.copy(isGaplessPlayback = enabled)
    }

    fun updateCrossfadeDuration(seconds: Int) {
        _config.value = _config.value.copy(crossfadeDurationSec = seconds)
    }

    fun resetToDefaults() {
        _config.value = ThemeConfig()
    }
}

val LocalHeliosTheme = staticCompositionLocalOf { ThemeConfig() }
