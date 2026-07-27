package com.example.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.data.model.DynamicArtStyle
import com.example.data.model.PlayButtonStyle
import com.example.data.model.PlayerTheme
import com.example.data.model.ProgressBarStyle
import com.example.data.model.ThemeConfig
import com.example.data.model.VisualizerStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HeliosThemeManager(context: Context? = null) {
    private var prefs: SharedPreferences? = context?.getSharedPreferences("heliostune_settings", Context.MODE_PRIVATE)

    private val _config = MutableStateFlow(loadConfig())
    val config: StateFlow<ThemeConfig> = _config.asStateFlow()

    fun initContext(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences("heliostune_settings", Context.MODE_PRIVATE)
            _config.value = loadConfig()
        }
    }

    private fun loadConfig(): ThemeConfig {
        val p = prefs ?: return ThemeConfig()
        return ThemeConfig(
            playerTheme = try { PlayerTheme.valueOf(p.getString("playerTheme", PlayerTheme.MINIMAL.name)!!) } catch (e: Exception) { PlayerTheme.MINIMAL },
            accentColorHex = p.getString("accentColorHex", "#FFB300") ?: "#FFB300",
            secondaryAccentHex = p.getString("secondaryAccentHex", "#FF6D00") ?: "#FF6D00",
            colorCombinationPresetName = p.getString("colorPreset", "Helios Sunset") ?: "Helios Sunset",
            isAmoledMode = p.getBoolean("isAmoledMode", true),
            isMaterialYou = p.getBoolean("isMaterialYou", false),
            cornerRadiusDp = p.getInt("cornerRadiusDp", 20),
            isAnimationsEnabled = p.getBoolean("isAnimationsEnabled", true),
            isBlurEffectEnabled = p.getBoolean("isBlurEffectEnabled", true),
            blurRadiusDp = p.getInt("blurRadiusDp", 20),
            isVisualizerEnabled = p.getBoolean("isVisualizerEnabled", true),
            progressBarStyle = try { ProgressBarStyle.valueOf(p.getString("progressBarStyle", ProgressBarStyle.SLIDER.name)!!) } catch (e: Exception) { ProgressBarStyle.SLIDER },
            playButtonStyle = try { PlayButtonStyle.valueOf(p.getString("playButtonStyle", PlayButtonStyle.CIRCULAR_FILLED.name)!!) } catch (e: Exception) { PlayButtonStyle.CIRCULAR_FILLED },
            visualizerStyle = try { VisualizerStyle.valueOf(p.getString("visualizerStyle", VisualizerStyle.BARS.name)!!) } catch (e: Exception) { VisualizerStyle.BARS },
            dynamicArtStyle = try { DynamicArtStyle.valueOf(p.getString("dynamicArtStyle", DynamicArtStyle.GRADIENT.name)!!) } catch (e: Exception) { DynamicArtStyle.GRADIENT },
            notificationPlayerStyle = p.getString("notificationPlayerStyle", "YouTube Music Glow") ?: "YouTube Music Glow",
            widgetStyle = p.getString("widgetStyle", "YouTube Music Dynamic Wave") ?: "YouTube Music Dynamic Wave",
            appIconId = p.getString("appIconId", "icon_helios_gold") ?: "icon_helios_gold",
            isRotateArtwork = p.getBoolean("isRotateArtwork", false),
            isGaplessPlayback = p.getBoolean("isGaplessPlayback", true),
            crossfadeDurationSec = p.getInt("crossfadeDurationSec", 2)
        )
    }

    private fun saveConfig(newConfig: ThemeConfig) {
        _config.value = newConfig
        prefs?.edit()?.apply {
            putString("playerTheme", newConfig.playerTheme.name)
            putString("accentColorHex", newConfig.accentColorHex)
            putString("secondaryAccentHex", newConfig.secondaryAccentHex)
            putString("colorPreset", newConfig.colorCombinationPresetName)
            putBoolean("isAmoledMode", newConfig.isAmoledMode)
            putBoolean("isMaterialYou", newConfig.isMaterialYou)
            putInt("cornerRadiusDp", newConfig.cornerRadiusDp)
            putBoolean("isAnimationsEnabled", newConfig.isAnimationsEnabled)
            putBoolean("isBlurEffectEnabled", newConfig.isBlurEffectEnabled)
            putInt("blurRadiusDp", newConfig.blurRadiusDp)
            putBoolean("isVisualizerEnabled", newConfig.isVisualizerEnabled)
            putString("progressBarStyle", newConfig.progressBarStyle.name)
            putString("playButtonStyle", newConfig.playButtonStyle.name)
            putString("visualizerStyle", newConfig.visualizerStyle.name)
            putString("dynamicArtStyle", newConfig.dynamicArtStyle.name)
            putString("notificationPlayerStyle", newConfig.notificationPlayerStyle)
            putString("widgetStyle", newConfig.widgetStyle)
            putString("appIconId", newConfig.appIconId)
            putBoolean("isRotateArtwork", newConfig.isRotateArtwork)
            putBoolean("isGaplessPlayback", newConfig.isGaplessPlayback)
            putInt("crossfadeDurationSec", newConfig.crossfadeDurationSec)
            apply()
        }
    }

    fun updatePlayerTheme(theme: PlayerTheme) = saveConfig(_config.value.copy(playerTheme = theme))
    fun updateAccentColor(hex: String) = saveConfig(_config.value.copy(accentColorHex = hex, colorCombinationPresetName = "Custom Pair"))
    fun updateSecondaryAccentColor(hex: String) = saveConfig(_config.value.copy(secondaryAccentHex = hex, colorCombinationPresetName = "Custom Pair"))
    fun updateColorCombinationPreset(name: String, primaryHex: String, secondaryHex: String) = saveConfig(_config.value.copy(colorCombinationPresetName = name, accentColorHex = primaryHex, secondaryAccentHex = secondaryHex))
    fun toggleAnimations(enabled: Boolean) = saveConfig(_config.value.copy(isAnimationsEnabled = enabled))
    fun toggleBlurEffect(enabled: Boolean) = saveConfig(_config.value.copy(isBlurEffectEnabled = enabled))
    fun updateBlurRadius(radiusDp: Int) = saveConfig(_config.value.copy(blurRadiusDp = radiusDp))
    fun updateNotificationPlayerStyle(style: String) = saveConfig(_config.value.copy(notificationPlayerStyle = style))
    fun updateWidgetStyle(style: String) = saveConfig(_config.value.copy(widgetStyle = style))
    fun toggleAmoledMode(enabled: Boolean) = saveConfig(_config.value.copy(isAmoledMode = enabled))
    fun toggleMaterialYou(enabled: Boolean) = saveConfig(_config.value.copy(isMaterialYou = enabled))
    fun updateCornerRadius(radiusDp: Int) = saveConfig(_config.value.copy(cornerRadiusDp = radiusDp))
    fun updateProgressBarStyle(style: ProgressBarStyle) = saveConfig(_config.value.copy(progressBarStyle = style))
    fun updatePlayButtonStyle(style: PlayButtonStyle) = saveConfig(_config.value.copy(playButtonStyle = style))
    fun updateVisualizerStyle(style: VisualizerStyle) = saveConfig(_config.value.copy(visualizerStyle = style))
    fun toggleVisualizer(enabled: Boolean) = saveConfig(_config.value.copy(isVisualizerEnabled = enabled))
    fun updateDynamicArtStyle(style: DynamicArtStyle) = saveConfig(_config.value.copy(dynamicArtStyle = style))
    fun updateAppIcon(iconId: String) = saveConfig(_config.value.copy(appIconId = iconId))
    fun toggleRotateArtwork(enabled: Boolean) = saveConfig(_config.value.copy(isRotateArtwork = enabled))
    fun toggleGaplessPlayback(enabled: Boolean) = saveConfig(_config.value.copy(isGaplessPlayback = enabled))
    fun updateCrossfadeDuration(seconds: Int) = saveConfig(_config.value.copy(crossfadeDurationSec = seconds))
    fun resetToDefaults() {
        saveConfig(ThemeConfig())
    }
}

val LocalHeliosTheme = staticCompositionLocalOf { ThemeConfig() }
