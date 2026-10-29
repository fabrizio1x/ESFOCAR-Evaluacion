package com.duoc.esfocar

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.esfocar.data.EsfocarDatabase
import com.duoc.esfocar.model.Usuario
import com.duoc.esfocar.repository.UsuarioRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsuarioDaoTest {

    private lateinit var database: EsfocarDatabase
    private lateinit var repository: UsuarioRepository

    @Before
    fun crearBase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EsfocarDatabase::class.java
        ).addCallback(EsfocarDatabase.CUENTAS_DEMO).build()
        repository = UsuarioRepository(database.usuarioDao())
    }

    @After
    fun cerrarBase() {
        database.close()
    }

    @Test
    fun lasCuentasDemoQuedanDisponibles() = runBlocking {
        val usuario = repository.buscarUsuario("instructor@esfocar.cl", "instructor123")
        assertEquals(Usuario("instructor@esfocar.cl", "Instructor Demo", "Instructor"), usuario)
        assertEquals("Docente", repository.buscarUsuario("docente@esfocar.cl", "docente123")?.cargo)
        assertTrue(repository.existeCorreo("EVALUADOR@duocuc.cl"))
    }

    @Test
    fun registraYEncuentraSinImportarMayusculas() = runBlocking {
        repository.registrarUsuario(Usuario("camila.soto@esfocar.cl", "Camila Soto"), "clave2026")
        assertTrue(repository.existeCorreo("Camila.Soto@esfocar.cl"))
        assertNotNull(repository.buscarUsuario("CAMILA.SOTO@esfocar.cl", "clave2026"))
        assertNull(repository.buscarUsuario("camila.soto@esfocar.cl", "otraClave1"))
    }

    @Test
    fun guardaLaContrasenaCifrada() = runBlocking {
        repository.registrarUsuario(Usuario("pedro.rojas@esfocar.cl", "Pedro Rojas"), "clave2026")
        val guardado = database.usuarioDao().buscarPorCorreo("pedro.rojas@esfocar.cl")!!
        assertFalse(guardado.hashContrasena.contains("clave2026"))
        assertTrue(guardado.contrasenaCorrecta("clave2026"))
    }

    @Test
    fun noRegistraDosVecesElMismoCorreo() = runBlocking {
        repository.registrarUsuario(Usuario("luis.diaz@esfocar.cl", "Luis Díaz"), "clave2026")
        val repetido = runCatching {
            repository.registrarUsuario(Usuario("luis.diaz@esfocar.cl", "Luis Díaz"), "otraClave1")
        }
        assertTrue(repetido.isFailure)
        assertNotNull(repository.buscarUsuario("luis.diaz@esfocar.cl", "clave2026"))
    }
}
