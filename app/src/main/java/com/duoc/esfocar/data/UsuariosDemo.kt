package com.duoc.esfocar.data

object UsuariosDemo {

    private val cuentas = listOf(
        Triple("instructor@esfocar.cl", "Instructor Demo", "instructor123"),
        Triple("docente@esfocar.cl", "Docente Demo", "docente123"),
        Triple("evaluador@duocuc.cl", "Evaluador Demo", "evaluador123")
    )

    fun entidades(): List<UsuarioEntity> {
        return cuentas.map { (correo, nombre, contrasena) ->
            UsuarioEntity.crear(correo, nombre, contrasena)
        }
    }
}
