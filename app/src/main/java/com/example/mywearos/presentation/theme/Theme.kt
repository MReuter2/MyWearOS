package com.example.mywearos.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColors = lightColorScheme(
    primary = Black100,
    onPrimary = Color.White,
    secondary = Black100,
    onSecondary = Color.White,
    background = Black400,
    tertiary = Orange100
)


private val DarkColors = darkColorScheme(
    primary = Black100,
    onPrimary = Color.White,
    secondary = Black100,
    onSecondary = Color.White,
    background = Black400,
    tertiary = Orange100
)

@Composable
fun MyWearOSTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = MyWearOSShapes,
        content = content
    )
}