package com.duoc.esfocar.data

data class CuentaDemo(
    val correo: String,
    val nombre: String,
    val contrasena: String,
    val cargo: String
)

object UsuariosDemo {

    private val cuentas = listOf(
        CuentaDemo("instructor@esfocar.cl", "Instructor Demo", "instructor123", "Instructor"),
        CuentaDemo("docente@esfocar.cl", "Docente Demo", "docente123", "Docente"),
        CuentaDemo("evaluador@duocuc.cl", "Evaluador Demo", "evaluador123", "Docente")
    )

    fun entidades(): List<UsuarioEntity> {
        return cuentas.map { cuenta ->
            UsuarioEntity.crear(
                correo = cuenta.correo,
                nombre = cuenta.nombre,
                contrasena = cuenta.contrasena,
                cargo = cuenta.cargo
            )
        }
    }
}
