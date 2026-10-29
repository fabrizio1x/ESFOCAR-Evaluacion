package com.duoc.esfocar.model

data class Criterio(

    val id: Int,

    val rubricaId: Int,

    val descripcion: String,

    val ponderacion: Double,

    val notaMinima: Double = 1.0,

    val notaMaxima: Double = 7.0,

    val orden: Int
)