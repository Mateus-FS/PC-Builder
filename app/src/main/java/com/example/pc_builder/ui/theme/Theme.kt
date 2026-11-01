package com.example.pc_builder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EscuroScheme = darkColorScheme(
    primary = Color(0xFF3B82F6),
    onPrimary = Color.White,
    background = Color(0xFF0B1424),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF16233A),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF1E2D47),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF334155)
)

private val ClaroScheme = lightColorScheme(
    primary = Color(0xFF2563EB)
)

@Composable
fun PcBuilderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EscuroScheme else ClaroScheme,
        typography = Typography,   // já existe em Type.kt
        content = content
    )
}