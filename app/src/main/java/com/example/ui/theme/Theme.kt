package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = InsaneRed,
    onPrimary = Color.White,
    primaryContainer = InsaneRedDark,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFF1F1F1),
    onSecondary = Color(0xFF0F0F0F),
    background = YouTubeBlack,
    onBackground = YouTubeTextPrimaryDark,
    surface = YouTubeBlack,
    onSurface = YouTubeTextPrimaryDark,
    surfaceVariant = YouTubeDarkSurfaceVariant,
    onSurfaceVariant = YouTubeTextSecondaryDark,
    outline = YouTubeDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = InsaneRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDEDE),
    onPrimaryContainer = InsaneRedDark,
    secondary = Color(0xFF0F0F0F),
    onSecondary = Color.White,
    background = YouTubeWhite,
    onBackground = YouTubeTextPrimaryLight,
    surface = YouTubeWhite,
    onSurface = YouTubeTextPrimaryLight,
    surfaceVariant = YouTubeLightSurfaceVariant,
    onSurfaceVariant = YouTubeTextSecondaryLight,
    outline = YouTubeLightBorder
)

@Composable
fun InsaneTubeTheme(
    darkTheme: Boolean = true, // Default to YouTube's iconic dark aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
