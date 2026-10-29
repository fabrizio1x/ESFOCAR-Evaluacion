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

class RegisterViewModel(private val repository: UsuarioRepository) : ViewModel() {

    var nombre by mutableStateOf("")
        private set
    var apellido by mutableStateOf("")
        private set
    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var confirmarContrasena by mutableStateOf("")
        private set
    var esInstructor by mutableStateOf(false)
        private set

    var errorNombre by mutableStateOf<String?>(null)
        private set
    var errorApellido by mutableStateOf<String?>(null)
        private set
    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set
    var errorConfirmacion by mutableStateOf<String?>(null)
        private set
    var errorRegistro by mutableStateOf<String?>(null)
        private set
    var procesando by mutableStateOf(false)
        private set

    val botonHabilitado: Boolean
        get() = !procesando &&
                ValidadorRegistro.validarNombre(nombre) == null &&
                ValidadorRegistro.validarNombre(apellido, "apellido") == null &&
                ValidadorRegistro.validarCorreo(correo) == null &&
                ValidadorRegistro.validarContrasena(contrasena) == null &&
                ValidadorRegistro.validarConfirmacion(contrasena, confirmarContrasena) == null

    fun onNombreChange(valor: String) {
        nombre = valor
        errorNombre = if (valor.isEmpty()) null else ValidadorRegistro.validarNombre(valor)
    }

    fun onApellidoChange(valor: String) {
        apellido = valor
        errorApellido = if (valor.isEmpty()) null else ValidadorRegistro.validarNombre(valor, "apellido")
    }

    fun onCorreoChange(valor: String) {
        correo = valor
        errorCorreo = if (valor.isEmpty()) null else ValidadorRegistro.validarCorreo(valor)
        errorRegistro = null
    }

    fun onContrasenaChange(valor: String) {
        contrasena = valor
        errorContrasena = if (valor.isEmpty()) null else ValidadorRegistro.validarContrasena(valor)
        if (confirmarContrasena.isNotEmpty()) {
            errorConfirmacion = ValidadorRegistro.validarConfirmacion(valor, confirmarContrasena)
        }
    }

    fun onConfirmarContrasenaChange(valor: String) {
        confirmarContrasena = valor
        errorConfirmacion = if (valor.isEmpty()) null else ValidadorRegistro.validarConfirmacion(contrasena, valor)
    }

    fun onTipoCargoChange(valor: Boolean) {
        esInstructor = valor
    }

    suspend fun registrar(): Usuario? {
        errorNombre = ValidadorRegistro.validarNombre(nombre)
        errorApellido = ValidadorRegistro.validarNombre(apellido, "apellido")
        errorCorreo = ValidadorRegistro.validarCorreo(correo)
        errorContrasena = ValidadorRegistro.validarContrasena(contrasena)
        errorConfirmacion = ValidadorRegistro.validarConfirmacion(contrasena, confirmarContrasena)
        if (errorNombre != null || errorApellido != null || errorCorreo != null ||
            errorContrasena != null || errorConfirmacion != null
        ) return null

        procesando = true
        try {
            if (repository.existeCorreo(correo)) {
                errorRegistro = "Ya existe una cuenta con este correo"
                return null
            }

            val usuario = Usuario(
                correo = correo.trim().lowercase(),
                nombre = "${nombre.trim()} ${apellido.trim()}"
            )
            repository.registrarUsuario(usuario, contrasena)
            return usuario
        } finally {
            procesando = false
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = checkNotNull(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                RegisterViewModel(UsuarioRepository(EsfocarDatabase.obtener(aplicacion).usuarioDao()))
            }
        }
    }
}
