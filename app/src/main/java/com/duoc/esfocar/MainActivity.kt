package com.duoc.esfocar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duoc.esfocar.ui.login.LoginScreen
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppEsfocar_Grupo15Theme {
                LoginScreen()
            }
        }
    }
}