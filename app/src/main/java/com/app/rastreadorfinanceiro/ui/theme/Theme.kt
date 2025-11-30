package com.app.rastreadorfinanceiro.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    secondary = DarkCardLight,
    tertiary = SuccessGreenLight,
    background = DarkBackground,
    surface = DarkCard,
    surfaceVariant = DarkCardLight,
    onPrimary = TextPrimary,
    onSecondary = TextSecondary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = TextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryDark,
    secondary = PurpleGrey40,
    tertiary = SuccessGreen,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    error = ErrorRed
)

@Composable
fun RastreadorFinanceiroTheme(
    darkTheme: Boolean = true, // Always use dark theme
    // Dynamic color is disabled for consistent dark theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Force dark color scheme for modern financial app aesthetic
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}