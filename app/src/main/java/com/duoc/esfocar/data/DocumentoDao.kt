package com.duoc.esfocar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentoDao {

    @Insert
    suspend fun insertar(documento: DocumentoEntity): Long

    @Delete
    suspend fun eliminar(documento: DocumentoEntity)

    @Query("SELECT * FROM documentos ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<DocumentoEntity>>
}
