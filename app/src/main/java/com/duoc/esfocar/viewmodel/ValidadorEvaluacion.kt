package com.duoc.esfocar.viewmodel

object ValidadorEvaluacion {

    const val LARGO_MINIMO_RETROALIMENTACION = 15

    fun validarAspirante(nombre: String): String? {
        return if (nombre.isBlank()) "Debe ingresar el nombre del aspirante" else null
    }

    fun validarRetroalimentacion(texto: String): String? {
        val limpio = texto.trim()
        val letras = limpio.count { it.isLetter() }
        return when {
            limpio.isEmpty() -> "La retroalimentación del docente es obligatoria"
            letras < LARGO_MINIMO_RETROALIMENTACION -> "Escribe una observación de al menos $LARGO_MINIMO_RETROALIMENTACION letras"
            else -> null
        }
    }

    fun puedeGuardar(nombreAspirante: String, retroalimentacion: String): Boolean {
        return validarAspirante(nombreAspirante) == null &&
                validarRetroalimentacion(retroalimentacion) == null
    }
}
