package com.karan.myrecipeeapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorPalette = darkColors(
//    primary = Color(0xffc52427),
    primary = Color(0xFFDB3D40),
    primaryVariant = Color(0xFF82C23E),
    secondary = Color(0xffe8cd15),
//    surface = Color(0xff68b02b),
    surface = Color(0xFF243D0F),
    background = Color(0xFF911A17),
    onSurface = Color(0xFFE2F5D3),
//    onSurface = Color(0xFFB2EBF2),

)

@Composable
fun RecipeeAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = DarkColorPalette
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}