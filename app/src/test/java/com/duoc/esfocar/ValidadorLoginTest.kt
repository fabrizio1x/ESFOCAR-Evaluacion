package com.duoc.esfocar

import com.duoc.esfocar.viewmodel.ValidadorLogin
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadorLoginTest {

    @Test
    fun aceptaCorreosInstitucionales() {
        assertNull(ValidadorLogin.validarCorreo("instructor@esfocar.cl"))
        assertNull(ValidadorLogin.validarCorreo("Evaluador@DuocUC.cl"))
        assertNull(ValidadorLogin.validarCorreo("  docente@esfocar.cl  "))
    }

    @Test
    fun rechazaCorreoVacioMalFormadoOExterno() {
        assertEquals("Ingresa tu correo institucional", ValidadorLogin.validarCorreo(""))
        assertEquals("El formato del correo no es válido", ValidadorLogin.validarCorreo("docente.esfocar.cl"))
        assertEquals("Debes usar tu correo institucional", ValidadorLogin.validarCorreo("docente@gmail.com"))
        assertEquals("Debes usar tu correo institucional", ValidadorLogin.validarCorreo("docente@esfocar.cl.com"))
    }

    @Test
    fun contrasenaConLargoMinimo() {
        assertEquals("Ingresa tu contraseña", ValidadorLogin.validarContrasena(""))
        assertEquals("La contraseña debe tener al menos 6 caracteres", ValidadorLogin.validarContrasena("12345"))
        assertNull(ValidadorLogin.validarContrasena("123456"))
    }
}
