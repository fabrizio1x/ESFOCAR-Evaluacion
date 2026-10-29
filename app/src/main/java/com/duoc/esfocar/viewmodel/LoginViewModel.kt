package com.duoc.esfocar.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.duoc.esfocar.data.EsfocarDatabase
import com.duoc.esfocar.model.Usuario
import com.duoc.esfocar.repository.UsuarioRepository

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

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
    var procesando by mutableStateOf(false)
        private set

    val botonHabilitado: Boolean
        get() = !procesando &&
                ValidadorLogin.validarCorreo(correo) == null &&
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

    suspend fun iniciarSesion(): Usuario? {
        errorCorreo = ValidadorLogin.validarCorreo(correo)
        errorContrasena = ValidadorLogin.validarContrasena(contrasena)
        if (errorCorreo != null || errorContrasena != null) return null

        procesando = true
        try {
            val usuario = repository.buscarUsuario(correo, contrasena)
            if (usuario == null) {
                errorCredenciales = "Correo o contraseña incorrectos"
            }
            return usuario
        } finally {
            procesando = false
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = checkNotNull(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                LoginViewModel(UsuarioRepository(EsfocarDatabase.obtener(aplicacion).usuarioDao()))
            }
        }
    }
}
