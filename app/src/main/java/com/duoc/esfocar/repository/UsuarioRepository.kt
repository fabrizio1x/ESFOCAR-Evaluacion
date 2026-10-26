package com.duoc.esfocar.repository

import com.duoc.esfocar.model.Usuario

class UsuarioRepository {

    companion object {
        private val usuarios = mutableListOf(
            Usuario("instructor@esfocar.cl", "instructor123", "Instructor Demo"),
            Usuario("docente@esfocar.cl", "docente123", "Docente Demo"),
            Usuario("evaluador@duocuc.cl", "evaluador123", "Evaluador Demo")
        )
    }

    fun existeCorreo(correo: String): Boolean {
        return usuarios.any { it.correo.equals(correo.trim(), ignoreCase = true) }
    }

    fun registrarUsuario(usuario: Usuario) {
        usuarios.add(usuario)
    }

    fun buscarUsuario(correo: String, contrasena: String): Usuario? {
        return usuarios.find {
            it.correo.equals(correo.trim(), ignoreCase = true) && it.contrasena == contrasena
        }
    }
}
