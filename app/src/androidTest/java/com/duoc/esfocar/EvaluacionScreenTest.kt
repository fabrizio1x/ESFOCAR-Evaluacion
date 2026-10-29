package com.duoc.esfocar

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.esfocar.ui.evaluacion.EvaluacionScreen
import com.duoc.esfocar.ui.rubricas.RubricasDemoData
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EvaluacionScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val rubrica = RubricasDemoData.obtenerRubricasBase().first()

    private var guardado: List<Any?>? = null

    private fun mostrarPantalla(hayConexion: Boolean = true) {
        composeRule.setContent {
            AppEsfocar_Grupo15Theme(darkTheme = false) {
                EvaluacionScreen(
                    rubrica = rubrica,
                    hayConexion = hayConexion,
                    onGuardarEvaluacion = { aspirante, nota, observaciones, foto ->
                        guardado = listOf(aspirante, nota, observaciones, foto)
                    }
                )
            }
        }
    }

    @Test
    fun listaTodosLosCriteriosDeLaRubrica() {
        mostrarPantalla()
        composeRule.onNodeWithText("Criterios de Desempeño (${rubrica.criterios.size})").assertIsDisplayed()
        rubrica.criterios.forEach { criterio ->
            composeRule.onNodeWithText(criterio.nombre).performScrollTo().assertIsDisplayed()
        }
    }

    @Test
    fun notaSeRecalculaAlCambiarElNivelDeLogro() {
        mostrarPantalla()
        composeRule.onNodeWithText("7.00").performScrollTo().assertIsDisplayed()
        composeRule.onAllNodesWithText("Insuficiente (2.0)")[0].performScrollTo().performClick()
        composeRule.onNodeWithText("5.75").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Condición: Aprobado").assertExists()

        for (indice in 1 until rubrica.criterios.size) {
            composeRule.onAllNodesWithText("Insuficiente (2.0)")[indice].performScrollTo().performClick()
        }
        composeRule.onNodeWithText("2.00").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Condición: No Aprobado").assertExists()
    }

    @Test
    fun noGuardaSinNombreNiRetroalimentacion() {
        mostrarPantalla()
        composeRule.onNodeWithText("Finalizar y Guardar Evaluación").performScrollTo().performClick()
        composeRule.onNodeWithText("Complete el nombre del aspirante y las observaciones requeridas.").assertExists()
        composeRule.onNodeWithText("Evaluación Guardada").assertDoesNotExist()
        assertNull(guardado)
    }

    @Test
    fun noGuardaConRetroalimentacionDemasiadoCorta() {
        mostrarPantalla()
        composeRule.onNodeWithText("Nombre del Aspirante / Alumno").performScrollTo().performTextInput("Ana Rojas")
        composeRule.onNodeWithText("Observaciones y recomendaciones").performScrollTo().performTextInput("Bien")
        composeRule.onNodeWithText("Finalizar y Guardar Evaluación").performScrollTo().performClick()
        composeRule.onNodeWithText("Evaluación Guardada").assertDoesNotExist()
        assertNull(guardado)
    }

    @Test
    fun guardaConDatosCompletos() {
        mostrarPantalla()
        composeRule.onNodeWithText("Nombre del Aspirante / Alumno").performScrollTo().performTextInput("Ana Rojas")
        composeRule.onNodeWithText("Observaciones y recomendaciones").performScrollTo()
            .performTextInput("Mantuvo el resguardo y comunicó con claridad cada instrucción.")
        composeRule.onNodeWithText("Finalizar y Guardar Evaluación").performScrollTo().performClick()

        composeRule.onNodeWithText("Evaluación Guardada").assertIsDisplayed()
        composeRule.onNodeWithText("Nota Final: 7.00").assertIsDisplayed()
        composeRule.onNodeWithText("Registrada en el almacenamiento local del dispositivo.").assertIsDisplayed()
        composeRule.onNodeWithText("Aceptar y Salir").performClick()

        assertEquals(
            listOf("Ana Rojas", 7.0, "Mantuvo el resguardo y comunicó con claridad cada instrucción.", null),
            guardado
        )
    }

    @Test
    fun avisaCuandoNoHayConexion() {
        mostrarPantalla(hayConexion = false)
        composeRule.onNodeWithText("Sin conexión: la evaluación se guardará en el teléfono y no se perderá.").assertIsDisplayed()
    }

    @Test
    fun sinAvisoCuandoHayConexion() {
        mostrarPantalla(hayConexion = true)
        composeRule.onNodeWithText("Sin conexión: la evaluación se guardará en el teléfono y no se perderá.").assertDoesNotExist()
    }

    @Test
    fun conservaLoEscritoAlGirarElTelefono() {
        val restauracion = StateRestorationTester(composeRule)
        restauracion.setContent {
            AppEsfocar_Grupo15Theme(darkTheme = false) {
                EvaluacionScreen(rubrica = rubrica)
            }
        }
        composeRule.onNodeWithText("Nombre del Aspirante / Alumno").performScrollTo().performTextInput("Ana Rojas")
        composeRule.onNodeWithText("Observaciones y recomendaciones").performScrollTo()
            .performTextInput("Mantuvo el resguardo durante todo el control.")
        composeRule.onAllNodesWithText("Insuficiente (2.0)")[0].performScrollTo().performClick()

        restauracion.emulateSavedInstanceStateRestore()

        composeRule.onNodeWithText("Ana Rojas").assertExists()
        composeRule.onNodeWithText("Mantuvo el resguardo durante todo el control.").assertExists()
        composeRule.onNodeWithText("5.75").performScrollTo().assertIsDisplayed()
    }
}
