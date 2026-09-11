package org.opencampus.elearning.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = OpenCampusColors.CorporatePurple,
    onPrimary = OpenCampusColors.GrayscaleWhite,
    primaryContainer = OpenCampusColors.CorporateDarkPurple,
    onPrimaryContainer = OpenCampusColors.GrayscaleWhite,
    secondary = OpenCampusColors.Blue,
    onSecondary = OpenCampusColors.GrayscaleWhite,
    background = OpenCampusColors.GrayscaleBgLightGrey,
    onBackground = OpenCampusColors.GrayscaleBlack,
    surface = OpenCampusColors.GrayscaleWhite,
    onSurface = OpenCampusColors.GrayscaleBlack,
    error = OpenCampusColors.InformingError,
    onError = OpenCampusColors.GrayscaleWhite,
    outline = OpenCampusColors.GrayscaleBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = OpenCampusColors.CorporatePurple,
    onPrimary = OpenCampusColors.GrayscaleWhite,
    background = Color(0xFF121212),
    onBackground = OpenCampusColors.GrayscaleWhite,
    surface = Color(0xFF1E1E1E),
    onSurface = OpenCampusColors.GrayscaleWhite,
    outline = Color(0xFF3E3E3E)
)

@Composable
fun OpenCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
