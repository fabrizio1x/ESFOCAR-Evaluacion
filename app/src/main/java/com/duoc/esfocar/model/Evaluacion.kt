package com.duoc.esfocar.model

data class Evaluacion(
    val id: Int,
    val rubricaId: Int,
    val estudianteId: Int,
    val correoDocente: String,
    val notasPorCriterio: Map<Int, Double>,
    val notaFinal: Double,
    val retroalimentacion: String,
    val fecha: Long = System.currentTimeMillis()
) {
    init {
        require(notasPorCriterio.values.all { EscalaNotas.esNotaValida(it) }) { "Las notas deben estar entre 1.0 y 7.0" }
        require(EscalaNotas.esNotaValida(notaFinal)) { "La nota final debe estar entre 1.0 y 7.0" }
    }

    val aprobada: Boolean
        get() = notaFinal >= EscalaNotas.NOTA_APROBACION
}
