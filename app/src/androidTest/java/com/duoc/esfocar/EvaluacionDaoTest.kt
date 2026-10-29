package com.duoc.esfocar

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.esfocar.data.EsfocarDatabase
import com.duoc.esfocar.data.EvaluacionDao
import com.duoc.esfocar.data.EvaluacionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EvaluacionDaoTest {

    private lateinit var database: EsfocarDatabase
    private lateinit var dao: EvaluacionDao

    @Before
    fun crearBase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EsfocarDatabase::class.java
        ).build()
        dao = database.evaluacionDao()
    }

    @After
    fun cerrarBase() {
        database.close()
    }

    private fun evaluacion(aspirante: String, nota: Double, fecha: Long, fotoRuta: String? = null) =
        EvaluacionEntity(
            aspirante = aspirante,
            rubricaId = "rub-1",
            rubricaTitulo = "Control y Fiscalización Vehicular",
            nota = nota,
            observaciones = "Buen control del entorno durante el procedimiento.",
            fotoRuta = fotoRuta,
            fecha = fecha
        )

    @Test
    fun guardaYRecuperaUnaEvaluacionCompleta() = runBlocking {
        val id = dao.insertar(evaluacion("Ana Rojas", 4.45, 1000L, "/data/evidencia.jpg"))
        val guardadas = dao.obtenerTodas().first()
        assertEquals(1, guardadas.size)
        val guardada = guardadas.first()
        assertEquals(id, guardada.id)
        assertEquals(4.45, guardada.nota, 0.0)
        assertEquals("/data/evidencia.jpg", guardada.fotoRuta)
        assertTrue(guardada.aprobada)
    }

    @Test
    fun ordenaDeLaMasRecienteALaMasAntigua() = runBlocking {
        dao.insertar(evaluacion("Ana Rojas", 6.0, 1000L))
        dao.insertar(evaluacion("Luis Pérez", 3.5, 3000L))
        dao.insertar(evaluacion("Marta Díaz", 5.0, 2000L))
        val nombres = dao.obtenerTodas().first().map { it.aspirante }
        assertEquals(listOf("Luis Pérez", "Marta Díaz", "Ana Rojas"), nombres)
    }

    @Test
    fun filtraPorAspiranteSinImportarMayusculas() = runBlocking {
        dao.insertar(evaluacion("Ana Rojas", 6.5, 1000L))
        dao.insertar(evaluacion("ana rojas", 3.25, 2000L))
        dao.insertar(evaluacion("Luis Pérez", 5.0, 3000L))
        val deAna = dao.obtenerPorAspirante("ANA ROJAS").first()
        assertEquals(2, deAna.size)
        assertFalse(deAna.first().aprobada)
    }
}
