package com.daniloscataloni.liftking.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LiftKingColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = DeepBlack,
    primaryContainer = ButtonBackgroundGray,
    onPrimaryContainer = Color.White,
    secondary = SmoothGray,
    onSecondary = Color.White,
    secondaryContainer = BackgroundGray,
    onSecondaryContainer = Color.White,
    background = DeepBlack,
    onBackground = Color.White,
    surface = BackgroundGray,
    onSurface = Color.White,
    surfaceVariant = ButtonBackgroundGray,
    onSurfaceVariant = SmoothGray,
    outline = SmoothGray,
    outlineVariant = ButtonBackgroundGray
)

@Composable
fun LiftKingTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LiftKingColorScheme,
        typography = Typography,
        content = content
    )
}
