package com.duoc.esfocar.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duoc.esfocar.model.EscalaNotas

@Entity(tableName = "evaluaciones")
data class EvaluacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val aspirante: String,
    val rubricaId: String,
    val rubricaTitulo: String,
    val nota: Double,
    val observaciones: String,
    val fotoRuta: String? = null,
    val fecha: Long = System.currentTimeMillis()
) {
    val aprobada: Boolean
        get() = nota >= EscalaNotas.NOTA_APROBACION
}
