package com.duoc.esfocar.model

data class Rubrica(

    val id: Int,

    val codigo: String,

    val nombre: String,

    val materia: String,

    val descripcion: String,

    val version: String,

    val activa: Boolean = true
)