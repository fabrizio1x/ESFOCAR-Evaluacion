package com.duoc.esfocar.ui.theme

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
    primary = VerdeInstitucionalClaro,
    onPrimary = Color.Black,
    primaryContainer = VerdeInstitucionalOscuro,
    onPrimaryContainer = VerdeContenedor,
    secondary = VerdeLaurelClaro,
    onSecondary = Color.Black,
    secondaryContainer = VerdeLaurel,
    onSecondaryContainer = LaurelContenedor,
    background = FondoOscuro,
    onBackground = TextoPrincipalOscuro,
    surface = SuperficieOscura,
    onSurface = TextoPrincipalOscuro,
    surfaceVariant = Color(0xFF2D322E),
    onSurfaceVariant = TextoSecundarioOscuro,
    outline = BordeOscuro
)

private val LightColorScheme = lightColorScheme(
    primary = VerdeInstitucional,
    onPrimary = Color.White,
    primaryContainer = VerdeContenedor,
    onPrimaryContainer = SobreVerdeContenedor,
    secondary = VerdeLaurel,
    onSecondary = Color.White,
    secondaryContainer = LaurelContenedor,
    onSecondaryContainer = SobreLaurelContenedor,
    background = FondoClaro,
    onBackground = TextoPrincipalClaro,
    surface = SuperficieClara,
    onSurface = TextoPrincipalClaro,
    surfaceVariant = Color(0xFFE0E5E0),
    onSurfaceVariant = TextoSecundarioClaro,
    outline = BordeClaro
)

@Composable
fun AppEsfocar_Grupo15Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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