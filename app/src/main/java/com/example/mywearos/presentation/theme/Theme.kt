package com.example.mywearos.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

@Composable
fun MyWearOSTheme(
    colors: Colors = wearColorPalette,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = colors,
        typography = Typography,
        // For shapes, we generally recommend using the default Material Wear shapes which are
        // optimized for round and non-round devices.
        content = content
    )
}