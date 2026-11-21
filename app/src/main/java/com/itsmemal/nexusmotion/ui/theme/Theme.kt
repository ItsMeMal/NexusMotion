package com.itsmemal.nexusmotion.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NexusDarkScheme = darkColorScheme(
    primary = NexusPalette.lime,
    onPrimary = NexusPalette.background,
    secondary = NexusPalette.cyan,
    onSecondary = NexusPalette.background,
    tertiary = NexusPalette.violet,
    background = NexusPalette.background,
    onBackground = NexusPalette.text,
    surface = NexusPalette.panel,
    onSurface = NexusPalette.text,
    surfaceVariant = NexusPalette.panelRaised,
    onSurfaceVariant = NexusPalette.muted,
    outline = NexusPalette.line
)

@Composable
fun NexusMotionTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = NexusDarkScheme, content = content)
}
