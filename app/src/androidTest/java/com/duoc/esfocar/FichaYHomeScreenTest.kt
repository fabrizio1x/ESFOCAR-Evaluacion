package com.duoc.esfocar

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.esfocar.data.EvaluacionEntity
import com.duoc.esfocar.ui.aspirante.FichaAspiranteScreen
import com.duoc.esfocar.ui.home.TabEvaluaciones
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FichaYHomeScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val evaluaciones = listOf(
        EvaluacionEntity(
            id = 2,
            aspirante = "ana rojas",
            rubricaId = "rub-2",
            rubricaTitulo = "Procedimientos de Seguridad y Registro Preventivo",
            nota = 3.25,
            observaciones = "Omitió zonas críticas durante la palpación.",
            fecha = 2000L
        ),
        EvaluacionEntity(
            id = 1,
            aspirante = "Ana Rojas",
            rubricaId = "rub-1",
            rubricaTitulo = "Control y Fiscalización Vehicular",
            nota = 6.5,
            observaciones = "Buen posicionamiento y trato respetuoso.",
            fecha = 1000L
        )
    )

    @Test
    fun fichaMuestraPromedioTruncadoYResumen() {
        composeRule.setContent {
            AppEsfocar_Grupo15Theme(darkTheme = false) {
                FichaAspiranteScreen(aspirante = "Ana Rojas", evaluaciones = evaluaciones)
            }
        }
        composeRule.onNodeWithText("4.87").assertIsDisplayed()
        composeRule.onNodeWithText("Condición: Aprobado").assertIsDisplayed()
        composeRule.onNodeWithText("1 de 2").assertIsDisplayed()
        composeRule.onAllNodesWithText("6.50").assertCountEquals(2)
        composeRule.onNodeWithText("Historial de Evaluaciones (2)").assertIsDisplayed()
    }

    @Test
    fun fichaSinEvaluaciones() {
        composeRule.setContent {
            AppEsfocar_Grupo15Theme(darkTheme = false) {
                FichaAspiranteScreen(aspirante = "Luis Pérez", evaluaciones = emptyList())
            }
        }
        composeRule.onNodeWithText("Sin evaluaciones").assertIsDisplayed()
        composeRule.onNodeWithText("Este aspirante aún no tiene evaluaciones guardadas.").assertIsDisplayed()
    }

    @Test
    fun inicioMuestraModoTerrenoSinConexion() {
        composeRule.setContent {
            AppEsfocar_Grupo15Theme(darkTheme = false) {
                TabEvaluaciones(nombre = "Instructor Demo", evaluaciones = evaluaciones.take(1), hayConexion = false)
            }
        }
        composeRule.onNodeWithText("Modo Terreno sin conexión: 1 evaluación guardada en el teléfono").assertIsDisplayed()
        composeRule.onNodeWithText("Evaluaciones Recientes (1)").assertIsDisplayed()
    }

    @Test
    fun inicioMuestraConexionYAbreLaFicha() {
        var abierta: EvaluacionEntity? = null
        composeRule.setContent {
            AppEsfocar_Grupo15Theme(darkTheme = false) {
                TabEvaluaciones(
                    nombre = "Instructor Demo",
                    evaluaciones = evaluaciones,
                    hayConexion = true,
                    onEvaluacionClick = { abierta = it }
                )
            }
        }
        composeRule.onNodeWithText("Conectado a la red").assertIsDisplayed()
        composeRule.onNodeWithText("Bienvenido, Instructor Demo").assertIsDisplayed()
        composeRule.onNodeWithText("Control y Fiscalización Vehicular", substring = true).performClick()
        composeRule.waitForIdle()
        assertEquals("Ana Rojas", abierta?.aspirante)
    }
}
