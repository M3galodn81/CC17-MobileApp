package com.example.essence.ui.theme

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

// --- Blue Palette for Dark Theme (Uses light primary colors) ---
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = Color.Black,
    secondary = SecondaryDark,
    onSecondary = Color.Black,
    tertiary = TertiaryDark,
    onTertiary = Color.Black,

    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE3E2E6),

    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE3E2E6),

    surfaceVariant = Color(0xFF2C2F33),
    onSurfaceVariant = Color(0xFFC5C6CA),

    outline = Color(0xFF8E8E8E),

    error = Color(0xFFCF6679),
    onError = Color.Black,

    inverseSurface = Color(0xFFE3E2E6),
    inverseOnSurface = Color(0xFF1A1C1E),
    inversePrimary = PrimaryLight
)

// --- Blue Palette for Light Theme (Uses dark primary colors) ---
private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = Color.White,
    secondary = SecondaryLight,
    onSecondary = Color.White,
    tertiary = TertiaryLight,
    onTertiary = Color.Black,

    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1A1C1E),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1C1E),

    surfaceVariant = Color(0xFFF3F3F3),
    onSurfaceVariant = Color(0xFF44474A),

    outline = Color(0xFFBDBDBD),

    error = Color(0xFFB3261E),
    onError = Color.White,

    inverseSurface = Color(0xFF1A1C1E),
    inverseOnSurface = Color(0xFFE3E2E6),
    inversePrimary = PrimaryDark
)

@Composable
fun ESSenceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}