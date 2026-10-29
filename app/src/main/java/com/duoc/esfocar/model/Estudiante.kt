package com.duoc.esfocar.model

data class Estudiante(
    val id: Int,
    val rut: String,
    val nombre: String,
    val apellido: String,
    val seccion: String
) {
    init {
        require(nombre.isNotBlank() && apellido.isNotBlank()) { "El estudiante debe tener nombre y apellido" }
    }

    val nombreCompleto: String
        get() = "$nombre $apellido"
}
