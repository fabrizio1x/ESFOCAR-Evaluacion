package com.duoc.esfocar.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import com.duoc.esfocar.viewmodel.RegisterViewModel

class RegisterActivity : ComponentActivity() {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppEsfocar_Grupo15Theme {
                RegisterScreen(
                    viewModel = registerViewModel,
                    onRegisterClick = {
                        val usuario = registerViewModel.registrar()
                        if (usuario != null) {
                            Toast.makeText(
                                this,
                                "Cuenta creada para ${usuario.nombre}. Ya puedes iniciar sesión",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    },
                    onBackToLoginClick = {
                        finish()
                    }
                )
            }
        }
    }
}
