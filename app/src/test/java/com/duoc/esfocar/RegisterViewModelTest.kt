package com.duoc.esfocar

import com.duoc.esfocar.viewmodel.LoginViewModel
import com.duoc.esfocar.viewmodel.RegisterViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterViewModelTest {

    private fun completarFormulario(viewModel: RegisterViewModel, correo: String) {
        viewModel.onNombreChange("Camila")
        viewModel.onApellidoChange("Soto")
        viewModel.onCorreoChange(correo)
        viewModel.onContrasenaChange("clave2026")
        viewModel.onConfirmarContrasenaChange("clave2026")
    }

    @Test
    fun registraDocenteYLuegoPuedeIniciarSesion() {
        val registro = RegisterViewModel()
        completarFormulario(registro, "Camila.Soto@esfocar.cl")
        assertTrue(registro.botonHabilitado)
        val usuario = registro.registrar()
        assertNotNull(usuario)
        assertEquals("camila.soto@esfocar.cl", usuario!!.correo)
        assertEquals("Camila Soto", usuario.nombre)

        val login = LoginViewModel()
        login.onCorreoChange("camila.soto@esfocar.cl")
        login.onContrasenaChange("clave2026")
        assertNotNull(login.iniciarSesion())
    }

    @Test
    fun noPermiteCorreoRepetido() {
        val registro = RegisterViewModel()
        completarFormulario(registro, "docente@esfocar.cl")
        assertNull(registro.registrar())
        assertEquals("Ya existe una cuenta con este correo", registro.errorRegistro)
    }

    @Test
    fun noRegistraSiLasContrasenasNoCoinciden() {
        val registro = RegisterViewModel()
        completarFormulario(registro, "pedro.rojas@esfocar.cl")
        registro.onConfirmarContrasenaChange("clave2027")
        assertFalse(registro.botonHabilitado)
        assertEquals("Las contraseñas no coinciden", registro.errorConfirmacion)
        assertNull(registro.registrar())
    }

    @Test
    fun soloAceptaDominioEsfocarAlRegistrar() {
        val registro = RegisterViewModel()
        completarFormulario(registro, "profe@duocuc.cl")
        assertFalse(registro.botonHabilitado)
        assertNull(registro.registrar())
    }
}
