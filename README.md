# Project HeliosTune

A modern, highly customizable open-source Android music player built using Kotlin, Jetpack Compose, and Material Design 3. Inspired by minimalist audio players like ArchiveTune, HeliosTune gives users fine-grained control over UI customization, visualizer behaviors, custom app icons, and real-time audio playback controls.

Repository: [suryax86/Project-HeliosTune](https://github.com/suryax86/Project-HeliosTune)

---

## Features

### Deep Customization & Personalization
- Progress Bar Styles: Choose between Standard Slider, Squiggle Waveform, Segmented Dots, Minimal Thin Line, Glowing Tube, and Vintage Analog Bar.
- Play Control Button Styles: Select from Circular Filled, Glass Neumorphic, Minimal Outlined, Retro Square, Floating Pill, and Cyber Glow.
- Custom App Icons: Pick from a curated list of custom app icon presets including Helios Gold, Helios Orbit, Minimal Monochromic, Stellar Glow, Audio Spectrum, Solar Waves, Modern Play, and Sunset Horizon.
- Dynamic Color Engine: Full control over primary accent colors, corner radii, and glassmorphic blur effects.

### Real-Time Audio Spectrum Visualizer
- Live PCM Analysis: Real-time frequency band analysis synchronized with audio playback.
- Multiple Visualizer Modes: Toggle between Frequency Bars, Sine Waveform, Orbital Rings, Matrix Grid, and Pulsing Dots.
- Optional Toggle: Easily enable or disable the live visualizer via settings for reduced resource usage.

### Audio Player & Sound Controls
- Integrated Synth Engine: Real-time audio synthesis with adjustable pitch and speed controls.
- Play Queue Management: View and manage upcoming tracks with quick queue navigation.
- Track Metadata Editor: Edit song titles, artists, and album tags directly inside the app.
- Sleep Timer: Automatically pause playback after a set duration.

---

## Architecture & Tech Stack

- Language: Kotlin 100%
- UI Framework: Jetpack Compose with Material Design 3
- Architecture: MVVM (Model-View-ViewModel) + StateFlow & Coroutines
- Audio Playback: Custom Audio Track & MediaPlayer Integration
- Asynchronous Processing: Kotlin Coroutines & Flow
- Build System: Gradle with Kotlin DSL (.gradle.kts)

---

## Project Structure

```text
com.example
|-- data
|   |-- model (Song, ThemeConfig, AppIconPresets)
|   |-- repository (MusicRepository)
|-- player
|   |-- AudioPlayerManager
|-- theme
|   |-- HeliosThemeManager, HeliosColors
|-- ui
|   |-- components (CustomProgressBar, CustomPlayButton, AudioVisualizerView, AppIconPreviewView)
|   |-- navigation (HeliosNavigation)
|   |-- player (PlayerThemes, MainPlayerView)
|   |-- screens (HomeScreen, LibraryScreen, SettingsScreen)
|   |-- viewmodel (HeliosViewModel)
```

---

## Building and Running

### Prerequisites
- Android Studio Jellyfish or newer
- JDK 17
- Android SDK 24+ (Android 7.0 Nougat or higher)

### Local Build Commands

1. Clone the repository:
   ```bash
   git clone https://github.com/suryax86/Project-HeliosTune.git
   cd Project-HeliosTune
   ```

2. Assemble Debug APK:
   ```bash
   ./gradlew assembleDebug
   ```

3. Run Unit Tests:
   ```bash
   ./gradlew test
   ```

---

## CI/CD & Automated Signing Workflow

The repository includes a GitHub Actions workflow located at `.github/workflows/android.yml`. On every push or pull request to `main` or `master`, the workflow automatically sets up JDK 17, builds the debug APK, and uploads it as an artifact.

### Configuring Secrets for Signed Release Builds

To enable automated signed release builds, add the following Repository Secrets in GitHub (`Settings > Secrets and variables > Actions > New repository secret`):

1. `KEYSTORE_BASE64`: Base64 encoded string of your release `.jks` or `.keystore` file.
   Generate on Linux/macOS:
   ```bash
   base64 -w 0 my-release-key.jks
   ```
2. `KEYSTORE_PASSWORD`: Password for your keystore.
3. `KEY_ALIAS`: Alias name of your key inside the keystore.
4. `KEY_PASSWORD`: Password for your key.

When these secrets are set, the GitHub Actions workflow automatically decodes the keystore, compiles a signed release APK (`app-release.apk`), uploads it as a workflow artifact, and cleans up the temporary keystore file.

---

## License

This project is open-source under the MIT License.
