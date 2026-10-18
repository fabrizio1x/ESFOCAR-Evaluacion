package com.duoc.esfocar.viewmodel

object ValidadorLogin {

    const val LARGO_MINIMO_CONTRASENA = 6

    private val dominiosPermitidos = listOf("esfocar.cl", "duocuc.cl")
    private val formatoCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun validarCorreo(correo: String): String? {
        val texto = correo.trim()
        return when {
            texto.isEmpty() -> "Ingresa tu correo institucional"
            !formatoCorreo.matches(texto) -> "El formato del correo no es válido"
            dominiosPermitidos.none { texto.lowercase().endsWith("@$it") } -> "Debes usar tu correo institucional"
            else -> null
        }
    }

    fun validarContrasena(contrasena: String): String? {
        return when {
            contrasena.isEmpty() -> "Ingresa tu contraseña"
            contrasena.length < LARGO_MINIMO_CONTRASENA -> "La contraseña debe tener al menos $LARGO_MINIMO_CONTRASENA caracteres"
            else -> null
        }
    }
}
