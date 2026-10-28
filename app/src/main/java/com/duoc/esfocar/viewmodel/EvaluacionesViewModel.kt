package com.duoc.esfocar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.esfocar.data.EsfocarDatabase
import com.duoc.esfocar.data.EvaluacionEntity
import com.duoc.esfocar.repository.EvaluacionRepository
import com.duoc.esfocar.ui.rubricas.RubricaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EvaluacionesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EvaluacionRepository(
        EsfocarDatabase.obtener(application).evaluacionDao()
    )

    val evaluaciones: Flow<List<EvaluacionEntity>> = repository.evaluaciones

    fun guardarEvaluacion(
        aspirante: String,
        rubrica: RubricaItem,
        nota: Double,
        observaciones: String,
        fotoRuta: String?,
        onGuardada: () -> Unit
    ) {
        viewModelScope.launch {
            repository.guardar(
                EvaluacionEntity(
                    aspirante = aspirante.trim(),
                    rubricaId = rubrica.id,
                    rubricaTitulo = rubrica.titulo,
                    nota = nota,
                    observaciones = observaciones.trim(),
                    fotoRuta = fotoRuta
                )
            )
            onGuardada()
        }
    }
}
