package com.duoc.esfocar

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.esfocar.data.DocumentoDao
import com.duoc.esfocar.data.DocumentoEntity
import com.duoc.esfocar.data.EsfocarDatabase
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
class DocumentoDaoTest {

    private lateinit var database: EsfocarDatabase
    private lateinit var dao: DocumentoDao

    @Before
    fun crearBase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EsfocarDatabase::class.java
        ).build()
        dao = database.documentoDao()
    }

    @After
    fun cerrarBase() {
        database.close()
    }

    private fun documento(nombre: String, tipoMime: String, fecha: Long) =
        DocumentoEntity(nombre = nombre, ruta = "/data/documentos/$nombre", tipoMime = tipoMime, fecha = fecha)

    @Test
    fun guardaYOrdenaDelMasRecienteAlMasAntiguo() = runBlocking {
        dao.insertar(documento("Pauta_Tiro.pdf", "application/pdf", 1000L))
        dao.insertar(documento("Rubrica_Defensa.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", 2000L))
        val documentos = dao.obtenerTodos().first()
        assertEquals(listOf("Rubrica_Defensa.docx", "Pauta_Tiro.pdf"), documentos.map { it.nombre })
        assertFalse(documentos[0].esPdf)
        assertTrue(documentos[1].esPdf)
    }

    @Test
    fun eliminaSoloElDocumentoElegido() = runBlocking {
        val idPdf = dao.insertar(documento("Pauta_Tiro.pdf", "application/pdf", 1000L))
        dao.insertar(documento("Manual.doc", "application/msword", 2000L))
        val pdf = dao.obtenerTodos().first().first { it.id == idPdf }
        dao.eliminar(pdf)
        assertEquals(listOf("Manual.doc"), dao.obtenerTodos().first().map { it.nombre })
    }
}
