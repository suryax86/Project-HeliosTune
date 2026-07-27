package com.example.data.model

enum class PlayerTheme(val displayName: String, val description: String) {
    MINIMAL("Minimal", "Sleek typography & refined control bar"),
    PIXEL("Pixel UI", "Clean Material M3 design with pill accents"),
    NOTHING("Nothing OS", "Dot-matrix typography & stark monochrome contrast"),
    OPPO("OPPO Glass", "Fluid curved glass panels & frosted overlays"),
    VINYL("Vinyl Record", "Rotating vinyl disc with animated tonearm"),
    CASSETTE("Cassette Tape", "Retro analog tape player with spinning reels"),
    CD_PLAYER("CD Player", "Laser reflective compact disc sheen"),
    NEON("Neon Cyberpunk", "Futuristic neon wireframes & dark grid"),
    GLASS("Glassmorphic", "Multi-layered frosted glass cards"),
    GRADIENT("Gradient Mesh", "Dynamic liquid gradient canvas"),
    AMOLED("AMOLED Pitch Black", "True #000000 deep dark for OLED screens"),
    AURORA("Aurora Borealis", "Soft animated northern lights flow"),
    FLOATING_ART("Floating Artwork", "3D floating album art with ambient shadow"),
    VISUALIZER_FOCUS("Visualizer Focus", "Large prominent audio frequency spectrum"),
    WALKMAN("Modern Walkman", "Classic tactile portable cassette player body"),
    STUDIO("Music Studio", "Professional VU meters & audio decibel bars"),
    FLOATING_ISLAND("Floating Island", "Compact dynamic capsule player"),
    FULL_BLEED("Full Bleed Artwork", "Full screen blurred background artwork"),
    WAVEFORM_MINIMAL("Waveform Minimal", "Minimal line wave audio display"),
    MONOCHROME("Monochrome Elegance", "Pure black and white luxury aesthetic")
}

enum class VisualizerStyle(val displayName: String) {
    BARS("Equalizer Bars"),
    CIRCULAR("Circular Spectrum"),
    WAVEFORM("Smooth Wave"),
    PARTICLES("Floating Particles"),
    MINIMAL_DOTS("Minimal Dots"),
    NOTHING_MATRIX("Nothing Dot Matrix"),
    MATERIAL_YOU("Material You Ripples"),
    VU_METER("Studio VU Meters")
}

enum class DynamicArtStyle(val displayName: String) {
    GRADIENT("Liquid Gradient"),
    MINIMAL_LETTER("Minimal Lettermark"),
    ABSTRACT_SHAPES("Abstract Geometry"),
    VINYL("Vinyl Groove"),
    CASSETTE("Retro Cassette"),
    COLOR_MESH("Color Mesh"),
    SUNSET("Solar Sunset"),
    AURORA("Aurora Glow"),
    GLASS("Frosted Glass"),
    NEON("Cyber Neon"),
    NOISE_PATTERN("Textured Noise"),
    MATERIAL_YOU("Dynamic Material")
}

enum class LibraryViewMode(val displayName: String) {
    COMFORTABLE_LIST("Comfortable List"),
    COMPACT_LIST("Compact List"),
    GRID("2-Column Grid"),
    LARGE_CARDS("Large Cards")
}

enum class ProgressBarStyle(val displayName: String, val description: String) {
    SLIDER("Standard Slider", "Classic Material smooth track with thumb"),
    WAVEFORM("Squiggle Waveform", "Audio waveform pattern with progress fill"),
    SEGMENTED("Segmented Dots", "Retro dot sequence progress line"),
    THIN_LINE("Minimal Thin Line", "Ultra-sleek borderless progress indicator"),
    GLOW_BAR("Glowing Tube", "Neon glowing progress bar with ambient blur"),
    VINTAGE_TAPE("Vintage Analog Bar", "Cassette-style dual striped gauge")
}

enum class PlayButtonStyle(val displayName: String, val description: String) {
    CIRCULAR_FILLED("Circular Filled", "Classic bold accent circle button"),
    GLASS_NEUMORPHIC("Glass Neumorphic", "Frosted glass panel with subtle rim highlight"),
    MINIMAL_OUTLINE("Minimal Outlined", "Sleek outlined accent ring"),
    RETRO_SQUARE("Retro Square", "Tactile square keycap with rounded corners"),
    FLOATING_PILL("Floating Pill", "Elongated modern pill container"),
    CYBER_GLOW("Cyber Glow", "Pulsing neon glowing center control")
}

data class ThemeConfig(
    val playerTheme: PlayerTheme = PlayerTheme.MINIMAL,
    val accentColorHex: String = "#FFB300", // Helios Gold
    val isAmoledMode: Boolean = true,
    val isMaterialYou: Boolean = false,
    val cornerRadiusDp: Int = 20,
    val glassBlurAmount: Float = 0.5f,
    val isVisualizerEnabled: Boolean = true,
    val progressBarStyle: ProgressBarStyle = ProgressBarStyle.SLIDER,
    val playButtonStyle: PlayButtonStyle = PlayButtonStyle.CIRCULAR_FILLED,
    val visualizerStyle: VisualizerStyle = VisualizerStyle.BARS,
    val dynamicArtStyle: DynamicArtStyle = DynamicArtStyle.GRADIENT,
    val appIconId: String = "icon_helios_gold",
    val isRotateArtwork: Boolean = false,
    val isGaplessPlayback: Boolean = true,
    val crossfadeDurationSec: Int = 2,
    val isVolumeNormalization: Boolean = false
)

data class AppIconOption(
    val id: String,
    val name: String,
    val primaryColorHex: String,
    val secondaryColorHex: String,
    val symbolType: String // "ring", "dot", "wave", "star", "disc", "walkman", etc.
)
