package com.duoc.esfocar.repository

import com.duoc.esfocar.data.EvaluacionDao
import com.duoc.esfocar.data.EvaluacionEntity
import kotlinx.coroutines.flow.Flow

class EvaluacionRepository(private val dao: EvaluacionDao) {

    val evaluaciones: Flow<List<EvaluacionEntity>> = dao.obtenerTodas()

    fun evaluacionesDe(aspirante: String): Flow<List<EvaluacionEntity>> {
        return dao.obtenerPorAspirante(aspirante.trim())
    }

    suspend fun guardar(evaluacion: EvaluacionEntity): Long {
        return dao.insertar(evaluacion)
    }
}
