package com.duoc.esfocar

import com.duoc.esfocar.viewmodel.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginViewModelTest {

    @Test
    fun botonDeshabilitadoHastaTenerDatosValidos() {
        val viewModel = LoginViewModel()
        assertFalse(viewModel.botonHabilitado)
        viewModel.onCorreoChange("instructor@esfocar.cl")
        assertFalse(viewModel.botonHabilitado)
        viewModel.onContrasenaChange("instructor123")
        assertTrue(viewModel.botonHabilitado)
    }

    @Test
    fun muestraErrorAlEscribirCorreoExterno() {
        val viewModel = LoginViewModel()
        viewModel.onCorreoChange("docente@gmail.com")
        assertEquals("Debes usar tu correo institucional", viewModel.errorCorreo)
        viewModel.onCorreoChange("")
        assertNull(viewModel.errorCorreo)
    }

    @Test
    fun ingresaConCredencialesCorrectas() {
        val viewModel = LoginViewModel()
        viewModel.onCorreoChange("INSTRUCTOR@esfocar.cl")
        viewModel.onContrasenaChange("instructor123")
        val usuario = viewModel.iniciarSesion()
        assertNotNull(usuario)
        assertEquals("Instructor Demo", usuario!!.nombre)
        assertNull(viewModel.errorCredenciales)
    }

    @Test
    fun rechazaContrasenaIncorrecta() {
        val viewModel = LoginViewModel()
        viewModel.onCorreoChange("instructor@esfocar.cl")
        viewModel.onContrasenaChange("otraClave1")
        assertNull(viewModel.iniciarSesion())
        assertEquals("Correo o contraseña incorrectos", viewModel.errorCredenciales)
    }
}
