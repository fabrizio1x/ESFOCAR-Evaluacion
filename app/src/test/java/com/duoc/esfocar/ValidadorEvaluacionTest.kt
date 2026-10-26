package com.duoc.esfocar

import com.duoc.esfocar.viewmodel.ValidadorEvaluacion
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidadorEvaluacionTest {

    @Test
    fun retroalimentacionObligatoria() {
        assertNotNull(ValidadorEvaluacion.validarRetroalimentacion(""))
        assertNotNull(ValidadorEvaluacion.validarRetroalimentacion("     "))
    }

    @Test
    fun retroalimentacionDebeTenerTextoReal() {
        assertNotNull(ValidadorEvaluacion.validarRetroalimentacion("bien"))
        assertNotNull(ValidadorEvaluacion.validarRetroalimentacion("..............."))
        assertNotNull(ValidadorEvaluacion.validarRetroalimentacion("123456789012345"))
        assertNull(ValidadorEvaluacion.validarRetroalimentacion("Aplicó correctamente el protocolo de aproximación"))
    }

    @Test
    fun noSePuedeGuardarSinRetroalimentacion() {
        assertFalse(ValidadorEvaluacion.puedeGuardar("Aspirante Morales", ""))
        assertFalse(ValidadorEvaluacion.puedeGuardar("", "Aplicó correctamente el protocolo de aproximación"))
        assertTrue(ValidadorEvaluacion.puedeGuardar("Aspirante Morales", "Aplicó correctamente el protocolo de aproximación"))
    }
}
