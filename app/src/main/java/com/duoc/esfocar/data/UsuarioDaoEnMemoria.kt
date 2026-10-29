package com.duoc.esfocar.data

class UsuarioDaoEnMemoria : UsuarioDao {

    private val usuarios = UsuariosDemo.entidades().associateBy { it.correo }.toMutableMap()

    override suspend fun insertar(usuario: UsuarioEntity) {
        check(usuario.correo.lowercase() !in usuarios) { "El correo ya está registrado" }
        usuarios[usuario.correo.lowercase()] = usuario
    }

    override suspend fun buscarPorCorreo(correo: String): UsuarioEntity? {
        return usuarios[correo.lowercase()]
    }
}
