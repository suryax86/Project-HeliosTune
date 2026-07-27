package com.example.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.data.model.ColorCombinationPreset
import com.example.data.model.ThemeConfig

object HeliosColors {
    // Primary Accents
    val HeliosGold = Color(0xFFFFB300)
    val HeliosFlame = Color(0xFFFF5252)
    val NothingRed = Color(0xFFFF1744)
    val OppoTeal = Color(0xFF00E5FF)
    val CyberNeon = Color(0xFFD500F9)
    val EmeraldMint = Color(0xFF00E676)
    val OceanBlue = Color(0xFF29B6F6)
    val SunsetOrange = Color(0xFFFF6D00)
    val AuroraPurple = Color(0xFFB388FF)

    // Preset Color Combinations (App UI Elements Dynamic Colors)
    val ColorPresets = listOf(
        ColorCombinationPreset("Helios Sunset", "#FFB300", "#FF6D00", "ArchiveTune Warm Gold & Flame Amber"),
        ColorCombinationPreset("Nothing OS Mono-Red", "#FF1744", "#FFFFFF", "Nothing OS Stark Dot-Matrix & Red Pulse"),
        ColorCombinationPreset("OPPO Aqua Glass", "#00E5FF", "#00E676", "OPPO Music Cyan Liquid & Emerald Mint"),
        ColorCombinationPreset("YouTube Vibrant Red", "#FF0000", "#D500F9", "YouTube Music Crimson & Electric Glow"),
        ColorCombinationPreset("Aurora Violet", "#B388FF", "#29B6F6", "Soft Northern Lights Violet & Ocean Blue"),
        ColorCombinationPreset("Cyber Neon", "#D500F9", "#00E5FF", "Futuristic Neon Magenta & Cyber Cyan"),
        ColorCombinationPreset("Emerald Gold", "#00E676", "#AEEA00", "Fresh Mint Green & Organic Lime Gold")
    )

    // Dark Backgrounds
    val AmoledBlack = Color(0xFF000000)
    val DarkCardBg = Color(0xFF121216)
    val DarkSurfaceBg = Color(0xFF1E1E24)
    val DarkGlassBorder = Color(0x33FFFFFF)
    val DarkGlassFill = Color(0x1AFFFFFF)

    // Light Backgrounds
    val LightBg = Color(0xFFF7F8FC)
    val LightCardBg = Color(0xFFFFFFFF)
    val LightSurfaceBg = Color(0xFFEDEEF4)

    fun parseColor(hex: String, fallback: Color = HeliosGold): Color {
        return try {
            val cleanHex = hex.removePrefix("#")
            val colorInt = cleanHex.toLong(16)
            if (cleanHex.length == 6) {
                Color(0xFF000000 or colorInt)
            } else {
                Color(colorInt)
            }
        } catch (e: Exception) {
            fallback
        }
    }

    fun getUiGradient(themeConfig: ThemeConfig): Brush {
        val primary = parseColor(themeConfig.accentColorHex, HeliosGold)
        val secondary = parseColor(themeConfig.secondaryAccentHex, SunsetOrange)
        return Brush.horizontalGradient(colors = listOf(primary, secondary))
    }

    fun getUiRadialGradient(themeConfig: ThemeConfig): Brush {
        val primary = parseColor(themeConfig.accentColorHex, HeliosGold)
        val secondary = parseColor(themeConfig.secondaryAccentHex, SunsetOrange)
        return Brush.radialGradient(colors = listOf(primary, secondary.copy(alpha = 0.5f)))
    }
}
