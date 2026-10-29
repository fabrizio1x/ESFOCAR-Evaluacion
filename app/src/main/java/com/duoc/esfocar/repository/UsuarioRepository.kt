package com.duoc.esfocar.repository

import com.duoc.esfocar.data.UsuarioDao
import com.duoc.esfocar.data.UsuarioEntity
import com.duoc.esfocar.model.Usuario

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun existeCorreo(correo: String): Boolean {
        return dao.buscarPorCorreo(correo.trim()) != null
    }

    suspend fun registrarUsuario(usuario: Usuario, contrasena: String) {
        dao.insertar(UsuarioEntity.crear(usuario.correo, usuario.nombre, contrasena))
    }

    suspend fun buscarUsuario(correo: String, contrasena: String): Usuario? {
        val encontrado = dao.buscarPorCorreo(correo.trim()) ?: return null
        if (!encontrado.contrasenaCorrecta(contrasena)) return null
        return Usuario(correo = encontrado.correo, nombre = encontrado.nombre)
    }
}
