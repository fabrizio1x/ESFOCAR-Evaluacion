package com.duoc.esfocar.model

data class CriterioEvaluacion(
    val id: Int,
    val rubricaId: Int,
    val nombre: String,
    val descripcion: String,
    val ponderacion: Int,
    val orden: Int
) {
    init {
        require(nombre.isNotBlank()) { "El criterio debe tener nombre" }
        require(ponderacion in 1..100) { "La ponderación debe estar entre 1 y 100" }
    }
}
