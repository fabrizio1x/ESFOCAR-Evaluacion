package com.duoc.esfocar

import com.duoc.esfocar.data.UsuarioDaoEnMemoria
import com.duoc.esfocar.repository.UsuarioRepository
import com.duoc.esfocar.viewmodel.LoginViewModel
import com.duoc.esfocar.viewmodel.RegisterViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterViewModelTest {

    private val repository = UsuarioRepository(UsuarioDaoEnMemoria())

    private fun completarFormulario(viewModel: RegisterViewModel, correo: String) {
        viewModel.onNombreChange("Camila")
        viewModel.onApellidoChange("Soto")
        viewModel.onCorreoChange(correo)
        viewModel.onContrasenaChange("clave2026")
        viewModel.onConfirmarContrasenaChange("clave2026")
    }

    @Test
    fun registraDocenteYLuegoPuedeIniciarSesion() = runBlocking {
        val registro = RegisterViewModel(repository)
        completarFormulario(registro, "Camila.Soto@esfocar.cl")
        assertTrue(registro.botonHabilitado)
        val usuario = registro.registrar()
        assertNotNull(usuario)
        assertEquals("camila.soto@esfocar.cl", usuario!!.correo)
        assertEquals("Camila Soto", usuario.nombre)
        assertEquals("Docente", usuario.cargo)

        val login = LoginViewModel(repository)
        login.onCorreoChange("camila.soto@esfocar.cl")
        login.onContrasenaChange("clave2026")
        val logueado = login.iniciarSesion()
        assertNotNull(logueado)
        assertEquals("Docente", logueado!!.cargo)
    }

    @Test
    fun registraInstructorCuandoSeSeleccionaElRol() = runBlocking {
        val registro = RegisterViewModel(repository)
        completarFormulario(registro, "manuel.perez@esfocar.cl")
        registro.seleccionarInstructor()
        assertTrue(registro.esInstructor)
        val usuario = registro.registrar()
        assertNotNull(usuario)
        assertEquals("Instructor", usuario!!.cargo)

        val login = LoginViewModel(repository)
        login.onCorreoChange("manuel.perez@esfocar.cl")
        login.onContrasenaChange("clave2026")
        val logueado = login.iniciarSesion()
        assertNotNull(logueado)
        assertEquals("Instructor", logueado!!.cargo)
    }

    @Test
    fun noPermiteCorreoRepetido() = runBlocking {
        val registro = RegisterViewModel(repository)
        completarFormulario(registro, "docente@esfocar.cl")
        assertNull(registro.registrar())
        assertEquals("Ya existe una cuenta con este correo", registro.errorRegistro)
    }

    @Test
    fun noRegistraSiLasContrasenasNoCoinciden() = runBlocking {
        val registro = RegisterViewModel(repository)
        completarFormulario(registro, "pedro.rojas@esfocar.cl")
        registro.onConfirmarContrasenaChange("clave2027")
        assertFalse(registro.botonHabilitado)
        assertEquals("Las contraseñas no coinciden", registro.errorConfirmacion)
        assertNull(registro.registrar())
    }

    @Test
    fun soloAceptaDominioEsfocarAlRegistrar() = runBlocking {
        val registro = RegisterViewModel(repository)
        completarFormulario(registro, "profe@duocuc.cl")
        assertFalse(registro.botonHabilitado)
        assertNull(registro.registrar())
    }
}
