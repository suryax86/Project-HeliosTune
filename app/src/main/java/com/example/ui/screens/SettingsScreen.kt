package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppIconPresets
import com.example.data.model.PlayButtonStyle
import com.example.data.model.PlayerTheme
import com.example.data.model.ProgressBarStyle
import com.example.data.model.ThemeConfig
import com.example.data.model.VisualizerStyle
import com.example.theme.HeliosColors
import com.example.theme.HeliosThemeManager
import com.example.ui.components.AppIconPreviewView

@Composable
fun SettingsScreen(
    themeConfig: ThemeConfig,
    themeManager: HeliosThemeManager,
    onScanMedia: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = HeliosColors.parseColor(themeConfig.accentColorHex)
    var isAccentSectionExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(if (themeConfig.isAmoledMode) HeliosColors.AmoledBlack else HeliosColors.DarkCardBg),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 120.dp, top = 16.dp)
    ) {
        item {
            Text(
                text = "SETTINGS & ENGINE",
                color = accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Text(
                text = "Customization Engine",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Section: Customize Theme (Player Themes)
        item {
            SettingsSectionHeader("Customize Theme", Icons.Default.Palette, accent)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(PlayerTheme.entries) { pTheme ->
                    val isSelected = themeConfig.playerTheme == pTheme
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) accent.copy(alpha = 0.25f) else Color(0xFF1E1E24))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) accent else Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { themeManager.updatePlayerTheme(pTheme) }
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = pTheme.displayName,
                                color = if (isSelected) accent else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = pTheme.description,
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: Accent Color (Single line expandable option)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E1E24))
                    .clickable { isAccentSectionExpanded = !isAccentSectionExpanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ColorLens, contentDescription = null, tint = accent, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Accent Color", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Current: ${themeConfig.colorCombinationPresetName}",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                }
                Icon(
                    imageVector = if (isAccentSectionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = accent
                )
            }

            if (isAccentSectionExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF16161B))
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Choose dynamic color pairs applied to controls & accents",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(HeliosColors.ColorPresets) { preset ->
                            val isSelected = themeConfig.colorCombinationPresetName == preset.name
                            val pColor = HeliosColors.parseColor(preset.primaryHex)
                            val sColor = HeliosColors.parseColor(preset.secondaryHex)

                            Box(
                                modifier = Modifier
                                    .width(140.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) pColor.copy(alpha = 0.25f) else Color(0xFF23232C))
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) pColor else Color.White.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable {
                                        themeManager.updateColorCombinationPreset(preset.name, preset.primaryHex, preset.secondaryHex)
                                    }
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(pColor))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(sColor))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(preset.name, color = if (isSelected) pColor else Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Primary Color Picker", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val colors = listOf("#FFB300", "#FF5252", "#FF1744", "#00E5FF", "#D500F9", "#00E676", "#29B6F6", "#FF6D00", "#B388FF")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        colors.forEach { hex ->
                            val parsed = HeliosColors.parseColor(hex)
                            val isSelected = themeConfig.accentColorHex.equals(hex, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(parsed)
                                    .border(width = if (isSelected) 2.5.dp else 0.dp, color = Color.White, shape = CircleShape)
                                    .clickable { themeManager.updateAccentColor(hex) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: Motion & Blur Effects
        item {
            SettingsSectionHeader("Blur Effects & Motion Engine", Icons.Default.GraphicEq, accent)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Enable UI Motion & Animations", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Pulsing controls, wave progress & smooth reveals", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                }
                Switch(
                    checked = themeConfig.isAnimationsEnabled,
                    onCheckedChange = { themeManager.toggleAnimations(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = accent, checkedTrackColor = accent.copy(alpha = 0.4f))
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Enable Glass & Backdrop Blur", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Frosted glass panels & background ambient lighting", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                }
                Switch(
                    checked = themeConfig.isBlurEffectEnabled,
                    onCheckedChange = { themeManager.toggleBlurEffect(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = accent, checkedTrackColor = accent.copy(alpha = 0.4f))
                )
            }

            if (themeConfig.isBlurEffectEnabled) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text("Blur Strength Intensity: ${themeConfig.blurRadiusDp} dp", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Slider(
                        value = themeConfig.blurRadiusDp.toFloat(),
                        onValueChange = { themeManager.updateBlurRadius(it.toInt()) },
                        valueRange = 5f..40f,
                        colors = SliderDefaults.colors(thumbColor = accent, activeTrackColor = accent)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: Display & Visuals
        item {
            SettingsSectionHeader("Display & Visuals", Icons.Default.Smartphone, accent)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("AMOLED Pitch Black", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text("True #000000 deep dark theme for battery savings", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                }
                Switch(
                    checked = themeConfig.isAmoledMode,
                    onCheckedChange = { themeManager.toggleAmoledMode(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = accent, checkedTrackColor = accent.copy(alpha = 0.4f))
                )
            }

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Corner Radius: ${themeConfig.cornerRadiusDp} dp", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Slider(
                    value = themeConfig.cornerRadiusDp.toFloat(),
                    onValueChange = { themeManager.updateCornerRadius(it.toInt()) },
                    valueRange = 4f..32f,
                    colors = SliderDefaults.colors(thumbColor = accent, activeTrackColor = accent)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: Progress Bar Style
        item {
            SettingsSectionHeader("Progress Bar Style", Icons.Default.GraphicEq, accent)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(ProgressBarStyle.entries) { pbStyle ->
                    val isSelected = themeConfig.progressBarStyle == pbStyle
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) accent.copy(alpha = 0.25f) else Color(0xFF1E1E24))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) accent else Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { themeManager.updateProgressBarStyle(pbStyle) }
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = pbStyle.displayName,
                                color = if (isSelected) accent else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = pbStyle.description,
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: Play Button Control Style
        item {
            SettingsSectionHeader("Play Button Control Style", Icons.Default.Palette, accent)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(PlayButtonStyle.entries) { btnStyle ->
                    val isSelected = themeConfig.playButtonStyle == btnStyle
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) accent.copy(alpha = 0.25f) else Color(0xFF1E1E24))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) accent else Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { themeManager.updatePlayButtonStyle(btnStyle) }
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = btnStyle.displayName,
                                color = if (isSelected) accent else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = btnStyle.description,
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: Audio Visualizer Toggle & Style
        item {
            SettingsSectionHeader("Audio Visualizer", Icons.Default.GraphicEq, accent)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Enable Audio Visualizer", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text("Toggle off to conserve battery during long music sessions", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                }
                Switch(
                    checked = themeConfig.isVisualizerEnabled,
                    onCheckedChange = { themeManager.toggleVisualizer(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = accent, checkedTrackColor = accent.copy(alpha = 0.4f))
                )
            }
            if (themeConfig.isVisualizerEnabled) {
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(VisualizerStyle.entries) { vStyle ->
                        val isSelected = themeConfig.visualizerStyle == vStyle
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) accent else Color(0xFF1E1E24))
                                .clickable { themeManager.updateVisualizerStyle(vStyle) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = vStyle.displayName,
                                color = if (isSelected) Color.Black else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: Custom App Icons
        item {
            SettingsSectionHeader("Customize App Icon", Icons.Default.Smartphone, accent)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Choose your icon style from the HeliosTune collection",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(AppIconPresets.icons) { option ->
                    val isSelected = themeConfig.appIconId == option.id
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(84.dp)
                            .clickable { themeManager.updateAppIcon(option.id) }
                    ) {
                        AppIconPreviewView(
                            option = option,
                            isSelected = isSelected,
                            accentColor = accent,
                            sizeDp = 64.dp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = option.name,
                            color = if (isSelected) accent else Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: Scanner & Media Storage
        item {
            SettingsSectionHeader("Library & Media Storage", Icons.Default.Refresh, accent)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onScanMedia,
                colors = ButtonDefaults.buttonColors(containerColor = accent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Scan device media", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: About Creator Card
        item {
            SettingsSectionHeader("About HeliosTune", Icons.Default.Person, accent)
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1E1E24))
                    .padding(18.dp)
            ) {
                Text(
                    text = "Developer & Creator",
                    color = accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Surya Singha",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "GitHub: suryax86\nInstagram: s.rya86",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { themeManager.resetToDefaults() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2E38)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset Theme to Factory Defaults", color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "HeliosTune v1.0.0 • Open Source (GPLv3)\nPrivacy-First: Zero network, zero ads, zero telemetry.",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
