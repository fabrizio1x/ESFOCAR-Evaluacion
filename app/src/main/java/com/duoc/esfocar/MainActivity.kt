package com.duoc.esfocar

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.duoc.esfocar.ui.home.HomeActivity
import com.duoc.esfocar.ui.login.LoginScreen
import com.duoc.esfocar.ui.register.RegisterActivity
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import com.duoc.esfocar.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppEsfocar_Grupo15Theme {
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginClick = {
                        val usuario = loginViewModel.iniciarSesion()
                        if (usuario != null) {
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.putExtra(HomeActivity.EXTRA_NOMBRE, usuario.nombre)
                            intent.putExtra(HomeActivity.EXTRA_CORREO, usuario.correo)
                            startActivity(intent)
                            finish()
                        }
                    },
                    onRegisterClick = {
                        startActivity(Intent(this, RegisterActivity::class.java))
                    }
                )
            }
        }
    }
}
