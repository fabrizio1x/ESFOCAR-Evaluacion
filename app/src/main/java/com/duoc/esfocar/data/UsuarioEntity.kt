package com.duoc.esfocar.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val correo: String,
    val nombre: String,
    val hashContrasena: String,
    val sal: String
) {
    fun contrasenaCorrecta(contrasena: String): Boolean {
        return CifradoContrasena.cifrar(contrasena, sal) == hashContrasena
    }

    companion object {
        fun crear(correo: String, nombre: String, contrasena: String): UsuarioEntity {
            val sal = CifradoContrasena.generarSal()
            return UsuarioEntity(
                correo = correo.trim().lowercase(),
                nombre = nombre.trim(),
                hashContrasena = CifradoContrasena.cifrar(contrasena, sal),
                sal = sal
            )
        }
    }
}
