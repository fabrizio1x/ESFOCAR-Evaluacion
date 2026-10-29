package com.duoc.esfocar.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import com.duoc.esfocar.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {

    private val registerViewModel: RegisterViewModel by viewModels { RegisterViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppEsfocar_Grupo15Theme {
                RegisterScreen(
                    viewModel = registerViewModel,
                    onRegisterClick = {
                        lifecycleScope.launch {
                            val usuario = registerViewModel.registrar()
                            if (usuario != null) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Cuenta creada para ${usuario.nombre}. Ya puedes iniciar sesión",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            }
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
