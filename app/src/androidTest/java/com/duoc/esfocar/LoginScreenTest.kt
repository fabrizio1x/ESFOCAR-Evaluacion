package com.duoc.esfocar

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.esfocar.ui.login.LoginScreen
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import com.duoc.esfocar.viewmodel.LoginViewModel
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val viewModel = LoginViewModel()

    private var ingreso = false

    private fun mostrarPantalla() {
        composeRule.setContent {
            AppEsfocar_Grupo15Theme(darkTheme = false) {
                LoginScreen(viewModel = viewModel, onLoginClick = { ingreso = true })
            }
        }
    }

    @Test
    fun botonIngresarSeHabilitaSoloConDatosValidos() {
        mostrarPantalla()
        composeRule.onNodeWithText("Ingresar").assertIsNotEnabled()

        composeRule.onNodeWithText("Correo institucional").performTextInput("docente@gmail.com")
        composeRule.onNodeWithText("Debes usar tu correo institucional").assertIsDisplayed()

        composeRule.onNodeWithText("Correo institucional").performTextClearance()
        composeRule.onNodeWithText("Correo institucional").performTextInput("instructor@esfocar.cl")
        composeRule.onNodeWithText("Contraseña").performTextInput("123")
        composeRule.onNodeWithText("La contraseña debe tener al menos 6 caracteres").assertIsDisplayed()
        composeRule.onNodeWithText("Ingresar").assertIsNotEnabled()

        composeRule.onNodeWithText("Contraseña").performTextInput("456")
        composeRule.onNodeWithText("Ingresar").assertIsEnabled().performClick()
        assertTrue(ingreso)
    }

    @Test
    fun contrasenaQuedaOculta() {
        mostrarPantalla()
        composeRule.onNode(hasText("Contraseña") and SemanticsMatcher.keyIsDefined(SemanticsProperties.Password))
            .assertExists()
    }
}
