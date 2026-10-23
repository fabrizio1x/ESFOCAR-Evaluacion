package com.duoc.esfocar.ui.rubricas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme

class RubricasActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppEsfocar_Grupo15Theme {
                RubricasScreen(
                    onBackClick = {
                        finish()
                    },
                    onIniciarEvaluacion = { rubrica ->
                        Toast.makeText(
                            this,
                            "Iniciando: ${rubrica.titulo}",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Enlace para la tarea ESF-12 (Formulario de Evaluación por criterios)
                    }
                )
            }
        }
    }
}
