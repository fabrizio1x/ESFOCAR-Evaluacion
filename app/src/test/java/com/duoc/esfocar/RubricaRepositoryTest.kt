package com.duoc.esfocar

import com.duoc.esfocar.repository.RubricaRepository
import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RubricaRepositoryTest {

    private val repository = RubricaRepository()

    @Test
    fun entregaRubricasActivasDeLasTresMaterias() {
        val rubricas = repository.obtenerRubricas()
        assertEquals(6, rubricas.size)
        assertTrue(rubricas.all { it.activa })
        assertEquals(
            setOf("Procedimientos Policiales", "Instrucción Física", "Práctica de Tiro"),
            rubricas.map { it.materia }.toSet()
        )
    }

    @Test
    fun criteriosOrdenadosYDeLaRubricaPedida() {
        val criterios = repository.obtenerCriteriosPorRubrica(1)
        assertEquals(5, criterios.size)
        assertTrue(criterios.all { it.rubricaId == 1 })
        assertEquals(listOf(1, 2, 3, 4, 5), criterios.map { it.orden })
    }

    @Test
    fun ponderacionesSumanUnoEnCadaRubrica() {
        repository.obtenerRubricas().forEach { rubrica ->
            val suma = repository.obtenerCriteriosPorRubrica(rubrica.id)
                .fold(BigDecimal.ZERO) { acumulado, criterio -> acumulado + BigDecimal.valueOf(criterio.ponderacion) }
            assertEquals(0, suma.compareTo(BigDecimal.ONE))
        }
    }

    @Test
    fun criteriosUsanLaEscalaDeUnoASiete() {
        repository.obtenerRubricas().forEach { rubrica ->
            repository.obtenerCriteriosPorRubrica(rubrica.id).forEach { criterio ->
                assertEquals(1.0, criterio.notaMinima, 0.0)
                assertEquals(7.0, criterio.notaMaxima, 0.0)
            }
        }
    }

    @Test
    fun rubricaInexistenteNoTieneCriterios() {
        assertTrue(repository.obtenerCriteriosPorRubrica(99).isEmpty())
    }
}
