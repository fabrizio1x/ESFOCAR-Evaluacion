package com.duoc.esfocar.viewmodel

import com.duoc.esfocar.model.EscalaNotas
import java.math.BigDecimal
import java.math.RoundingMode

object CalculadoraNotas {

    fun validarNota(nota: Double): String? {
        return if (EscalaNotas.esNotaValida(nota)) null
        else "La nota debe estar entre 1.00 y 7.00"
    }

    fun truncarDosDecimales(valor: Double): Double {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.DOWN).toDouble()
    }

    fun calcularPromedioPonderado(notasYPonderaciones: List<Pair<Double, Int>>): Double {
        if (notasYPonderaciones.isEmpty()) return EscalaNotas.NOTA_MINIMA

        var suma = BigDecimal.ZERO
        for ((nota, ponderacion) in notasYPonderaciones) {
            val notaValida = nota.coerceIn(EscalaNotas.NOTA_MINIMA, EscalaNotas.NOTA_MAXIMA)
            suma += BigDecimal.valueOf(notaValida) * BigDecimal(ponderacion)
        }
        val promedio = suma.divide(BigDecimal(100), 2, RoundingMode.DOWN).toDouble()
        return promedio.coerceIn(EscalaNotas.NOTA_MINIMA, EscalaNotas.NOTA_MAXIMA)
    }

    fun estaAprobado(nota: Double): Boolean {
        return nota >= EscalaNotas.NOTA_APROBACION
    }
}
