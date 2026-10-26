package com.duoc.esfocar.viewmodel

object ValidadorRegistro {

    const val LARGO_MINIMO_NOMBRE = 2
    const val LARGO_MINIMO_CONTRASENA = 6
    const val DOMINIO_INSTITUCIONAL = "esfocar.cl"

    private val formatoCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val formatoNombre = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñÜü' ]+$")

    fun validarNombre(nombre: String, campo: String = "nombre"): String? {
        val texto = nombre.trim()
        return when {
            texto.isEmpty() -> "Ingresa tu $campo"
            texto.length < LARGO_MINIMO_NOMBRE -> "El $campo debe tener al menos $LARGO_MINIMO_NOMBRE letras"
            !formatoNombre.matches(texto) -> "El $campo solo puede contener letras"
            else -> null
        }
    }

    fun validarCorreo(correo: String): String? {
        val texto = correo.trim()
        return when {
            texto.isEmpty() -> "Ingresa tu correo institucional"
            !formatoCorreo.matches(texto) -> "El formato del correo no es válido"
            !texto.lowercase().endsWith("@$DOMINIO_INSTITUCIONAL") -> "Debes usar un correo @$DOMINIO_INSTITUCIONAL"
            else -> null
        }
    }

    fun validarContrasena(contrasena: String): String? {
        return when {
            contrasena.isEmpty() -> "Ingresa una contraseña"
            contrasena.length < LARGO_MINIMO_CONTRASENA -> "La contraseña debe tener al menos $LARGO_MINIMO_CONTRASENA caracteres"
            contrasena.none { it.isLetter() } -> "La contraseña debe incluir al menos una letra"
            contrasena.none { it.isDigit() } -> "La contraseña debe incluir al menos un número"
            else -> null
        }
    }

    fun validarConfirmacion(contrasena: String, confirmacion: String): String? {
        return when {
            confirmacion.isEmpty() -> "Repite tu contraseña"
            confirmacion != contrasena -> "Las contraseñas no coinciden"
            else -> null
        }
    }
}
