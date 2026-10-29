package com.duoc.esfocar

import com.duoc.esfocar.data.CifradoContrasena
import com.duoc.esfocar.data.UsuarioEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CifradoContrasenaTest {

    @Test
    fun mismaContrasenaYSalDanElMismoResultado() {
        assertEquals(
            CifradoContrasena.cifrar("clave2026", "abc123"),
            CifradoContrasena.cifrar("clave2026", "abc123")
        )
    }

    @Test
    fun salDistintaDaResultadoDistinto() {
        assertNotEquals(
            CifradoContrasena.cifrar("clave2026", CifradoContrasena.generarSal()),
            CifradoContrasena.cifrar("clave2026", CifradoContrasena.generarSal())
        )
    }

    @Test
    fun laEntidadNoGuardaLaContrasenaEnTextoPlano() {
        val usuario = UsuarioEntity.crear(" Camila.Soto@ESFOCAR.cl ", "Camila Soto", "clave2026")
        assertEquals("camila.soto@esfocar.cl", usuario.correo)
        assertFalse(usuario.hashContrasena.contains("clave2026"))
        assertEquals(64, usuario.hashContrasena.length)
        assertTrue(usuario.contrasenaCorrecta("clave2026"))
        assertFalse(usuario.contrasenaCorrecta("Clave2026"))
    }
}
