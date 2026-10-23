package com.duoc.esfocar.ui.rubricas

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duoc.esfocar.ui.evaluacion.EvaluacionActivity
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
                        val intent = Intent(this, EvaluacionActivity::class.java)
                        intent.putExtra(EvaluacionActivity.EXTRA_RUBRICA_ID, rubrica.id)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
