package com.duoc.esfocar

import com.duoc.esfocar.viewmodel.CalculadoraNotas
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculadoraNotasTest {

    @Test
    fun truncaSinRedondear() {
        assertEquals(4.45, CalculadoraNotas.truncarDosDecimales(4.459), 0.0)
        assertEquals(6.99, CalculadoraNotas.truncarDosDecimales(6.999), 0.0)
        assertEquals(4.0, CalculadoraNotas.truncarDosDecimales(4.0), 0.0)
    }

    @Test
    fun promedioPonderadoSinErrorDeDecimales() {
        val notas = listOf(Pair(7.0, 30), Pair(5.0, 40), Pair(2.0, 30))
        assertEquals(4.7, CalculadoraNotas.calcularPromedioPonderado(notas), 0.0)
    }

    @Test
    fun promedioPonderadoSeTrunca() {
        assertEquals(4.8, CalculadoraNotas.calcularPromedioPonderado(listOf(Pair(5.0, 35), Pair(7.0, 35), Pair(2.0, 30))), 0.0)
        assertEquals(4.75, CalculadoraNotas.calcularPromedioPonderado(listOf(Pair(6.3, 33), Pair(4.0, 67))), 0.0)
    }

    @Test
    fun validaRangoDeNotas() {
        assertNull(CalculadoraNotas.validarNota(1.0))
        assertNull(CalculadoraNotas.validarNota(7.0))
        assertNotNull(CalculadoraNotas.validarNota(0.9))
        assertNotNull(CalculadoraNotas.validarNota(7.1))
    }

    @Test
    fun aprobacionConCuatro() {
        assertTrue(CalculadoraNotas.estaAprobado(4.0))
        assertFalse(CalculadoraNotas.estaAprobado(3.99))
    }
}
