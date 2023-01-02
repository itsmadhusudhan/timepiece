package com.shreekaram.timepiece.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColors(
//    primary = Purple200,
//    primaryVariant = Purple700,
//    secondary = Teal200
    primary = Indigo500,
    primaryVariant = Indigo700,
    secondary = Teal200,
    onPrimary = White
)

private val LightColorPalette = lightColors(
//    primary = Purple500,
//    primaryVariant = Purple700,
//    secondary = Teal200
    primary = Indigo500,
    primaryVariant = Indigo700,
    secondary = Teal200,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun TimePieceTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
            window.navigationBarColor = colors.background.toArgb()

            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme

            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
