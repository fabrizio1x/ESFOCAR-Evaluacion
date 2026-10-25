package com.duoc.esfocar.model

data class Criterio(

    val id: Int,

    val rubricaId: Int,

    val descripcion: String,

    val ponderacion: Double,      // suma 1.0 por rúbrica

    val notaMinima: Double = 1.0, // escala institucional

    val notaMaxima: Double = 7.0,

    val orden: Int
)