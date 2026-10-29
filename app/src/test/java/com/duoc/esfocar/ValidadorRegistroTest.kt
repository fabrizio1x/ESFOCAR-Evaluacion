package com.duoc.esfocar

import com.duoc.esfocar.viewmodel.ValidadorRegistro
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadorRegistroTest {

    @Test
    fun aceptaSoloCorreoEsfocar() {
        assertNull(ValidadorRegistro.validarCorreo("juan.perez@esfocar.cl"))
        assertNotNull(ValidadorRegistro.validarCorreo("juan.perez@duocuc.cl"))
        assertNotNull(ValidadorRegistro.validarCorreo("juan.perez@gmail.com"))
        assertNotNull(ValidadorRegistro.validarCorreo("juan.perez"))
        assertNotNull(ValidadorRegistro.validarCorreo(""))
    }

    @Test
    fun contrasenaConLetrasYNumeros() {
        assertNull(ValidadorRegistro.validarContrasena("clave123"))
        assertNotNull(ValidadorRegistro.validarContrasena("abc12"))
        assertNotNull(ValidadorRegistro.validarContrasena("solotexto"))
        assertNotNull(ValidadorRegistro.validarContrasena("12345678"))
    }

    @Test
    fun confirmacionDebeCoincidir() {
        assertNull(ValidadorRegistro.validarConfirmacion("clave123", "clave123"))
        assertNotNull(ValidadorRegistro.validarConfirmacion("clave123", "clave124"))
        assertNotNull(ValidadorRegistro.validarConfirmacion("clave123", ""))
    }

    @Test
    fun nombreSoloLetras() {
        assertNull(ValidadorRegistro.validarNombre("José"))
        assertNull(ValidadorRegistro.validarNombre("María José"))
        assertNotNull(ValidadorRegistro.validarNombre("J"))
        assertNotNull(ValidadorRegistro.validarNombre("Juan2"))
        assertNotNull(ValidadorRegistro.validarNombre("   "))
    }
}
