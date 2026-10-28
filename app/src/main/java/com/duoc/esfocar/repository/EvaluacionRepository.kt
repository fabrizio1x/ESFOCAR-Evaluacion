package com.duoc.esfocar.repository

import com.duoc.esfocar.data.EvaluacionDao
import com.duoc.esfocar.data.EvaluacionEntity
import kotlinx.coroutines.flow.Flow

class EvaluacionRepository(private val dao: EvaluacionDao) {

    val evaluaciones: Flow<List<EvaluacionEntity>> = dao.obtenerTodas()

    suspend fun guardar(evaluacion: EvaluacionEntity): Long {
        return dao.insertar(evaluacion)
    }
}
