package com.duoc.esfocar.viewmodel

import java.math.BigDecimal
import java.math.RoundingMode

data class ResumenAspirante(
    val cantidad: Int,
    val aprobadas: Int,
    val promedio: Double,
    val mejorNota: Double,
    val ultimaNota: Double
) {
    val aprobado: Boolean
        get() = CalculadoraNotas.estaAprobado(promedio)

    val reprobadas: Int
        get() = cantidad - aprobadas

    companion object {

        fun desdeNotas(notasMasRecientePrimero: List<Double>): ResumenAspirante? {
            if (notasMasRecientePrimero.isEmpty()) return null

            var suma = BigDecimal.ZERO
            for (nota in notasMasRecientePrimero) {
                suma += BigDecimal.valueOf(nota)
            }
            val promedio = suma
                .divide(BigDecimal(notasMasRecientePrimero.size), 2, RoundingMode.DOWN)
                .toDouble()

            return ResumenAspirante(
                cantidad = notasMasRecientePrimero.size,
                aprobadas = notasMasRecientePrimero.count { CalculadoraNotas.estaAprobado(it) },
                promedio = promedio,
                mejorNota = notasMasRecientePrimero.max(),
                ultimaNota = notasMasRecientePrimero.first()
            )
        }
    }
}
