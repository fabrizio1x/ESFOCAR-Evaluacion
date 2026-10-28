package com.duoc.esfocar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EvaluacionDao {

    @Insert
    suspend fun insertar(evaluacion: EvaluacionEntity): Long

    @Query("SELECT * FROM evaluaciones ORDER BY fecha DESC")
    fun obtenerTodas(): Flow<List<EvaluacionEntity>>

    @Query("SELECT * FROM evaluaciones WHERE aspirante = :aspirante COLLATE NOCASE ORDER BY fecha DESC")
    fun obtenerPorAspirante(aspirante: String): Flow<List<EvaluacionEntity>>
}
