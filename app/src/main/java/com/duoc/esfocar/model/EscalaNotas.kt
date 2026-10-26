package com.duoc.esfocar.model

object EscalaNotas {
    const val NOTA_MINIMA = 1.0
    const val NOTA_MAXIMA = 7.0
    const val NOTA_APROBACION = 4.0

    fun esNotaValida(nota: Double): Boolean {
        return nota in NOTA_MINIMA..NOTA_MAXIMA
    }
}
