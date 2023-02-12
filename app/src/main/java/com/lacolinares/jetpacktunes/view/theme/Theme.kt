package com.lacolinares.jetpacktunes.view.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = VampireBlack,
    secondary = MetallicYellow,
    tertiary = MetallicYellow
)

@Composable
fun JetpackTunesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}