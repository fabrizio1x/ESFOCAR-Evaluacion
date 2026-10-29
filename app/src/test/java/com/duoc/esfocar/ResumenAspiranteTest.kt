package com.duoc.esfocar

import com.duoc.esfocar.viewmodel.ResumenAspirante
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResumenAspiranteTest {

    @Test
    fun sinEvaluacionesNoHayResumen() {
        assertNull(ResumenAspirante.desdeNotas(emptyList()))
    }

    @Test
    fun promedioTruncadoADosDecimales() {
        val resumen = ResumenAspirante.desdeNotas(listOf(5.0, 4.0, 4.0))
        assertNotNull(resumen)
        assertEquals(4.33, resumen!!.promedio, 0.0)
    }

    @Test
    fun cuentaAprobadasYReprobadas() {
        val resumen = ResumenAspirante.desdeNotas(listOf(3.9, 6.5, 4.0, 2.1))!!
        assertEquals(4, resumen.cantidad)
        assertEquals(2, resumen.aprobadas)
        assertEquals(2, resumen.reprobadas)
        assertEquals(6.5, resumen.mejorNota, 0.0)
        assertEquals(3.9, resumen.ultimaNota, 0.0)
    }

    @Test
    fun condicionSegunPromedio() {
        assertTrue(ResumenAspirante.desdeNotas(listOf(4.0, 4.0))!!.aprobado)
        assertFalse(ResumenAspirante.desdeNotas(listOf(3.99, 3.99))!!.aprobado)
    }
}
