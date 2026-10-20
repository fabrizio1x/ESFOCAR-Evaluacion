package com.duoc.esfocar.ui.register

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme

class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppEsfocar_Grupo15Theme {
                RegisterScreen(
                    onRegisterClick = { nombre, apellido, correo, contrasena ->
                        // Punto de integración para la persistencia y ViewModel del compañero
                        finish()
                    },
                    onBackToLoginClick = {
                        finish()
                    }
                )
            }
        }
    }
}
