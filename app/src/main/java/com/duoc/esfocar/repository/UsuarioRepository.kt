package com.duoc.esfocar.repository

import com.duoc.esfocar.model.Usuario

class UsuarioRepository {

    private val usuarios = listOf(
        Usuario("instructor@esfocar.cl", "instructor123", "Instructor Demo"),
        Usuario("docente@esfocar.cl", "docente123", "Docente Demo"),
        Usuario("evaluador@duocuc.cl", "evaluador123", "Evaluador Demo")
    )

    fun buscarUsuario(correo: String, contrasena: String): Usuario? {
        return usuarios.find {
            it.correo.equals(correo.trim(), ignoreCase = true) && it.contrasena == contrasena
        }
    }

    val repo = RubricaRepository()
    val rubricas = repo.obtenerRubricas()
    val criterios = repo.obtenerCriteriosPorRubrica(1) // criterios de RUB-PP-01
}
