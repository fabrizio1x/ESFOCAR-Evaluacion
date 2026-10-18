package com.duoc.esfocar.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.duoc.esfocar.model.Usuario
import com.duoc.esfocar.repository.UsuarioRepository

class LoginViewModel : ViewModel() {

    private val repository = UsuarioRepository()

    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set
    var errorCredenciales by mutableStateOf<String?>(null)
        private set

    val botonHabilitado: Boolean
        get() = ValidadorLogin.validarCorreo(correo) == null &&
                ValidadorLogin.validarContrasena(contrasena) == null

    fun onCorreoChange(valor: String) {
        correo = valor
        errorCorreo = if (valor.isEmpty()) null else ValidadorLogin.validarCorreo(valor)
        errorCredenciales = null
    }

    fun onContrasenaChange(valor: String) {
        contrasena = valor
        errorContrasena = if (valor.isEmpty()) null else ValidadorLogin.validarContrasena(valor)
        errorCredenciales = null
    }

    fun iniciarSesion(): Usuario? {
        errorCorreo = ValidadorLogin.validarCorreo(correo)
        errorContrasena = ValidadorLogin.validarContrasena(contrasena)
        if (errorCorreo != null || errorContrasena != null) return null

        val usuario = repository.buscarUsuario(correo, contrasena)
        if (usuario == null) {
            errorCredenciales = "Correo o contraseña incorrectos"
        }
        return usuario
    }
}
