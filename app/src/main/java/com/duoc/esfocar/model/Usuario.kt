package com.duoc.esfocar.model

data class Usuario(
    val correo: String,
    val nombre: String,
    val cargo: String = "Docente"
)
