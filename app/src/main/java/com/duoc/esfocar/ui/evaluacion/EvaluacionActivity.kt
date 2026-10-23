package com.duoc.esfocar.ui.evaluacion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duoc.esfocar.ui.rubricas.RubricasDemoData
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme

class EvaluacionActivity : ComponentActivity() {

    companion object {
        const val EXTRA_RUBRICA_ID = "rubrica_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val rubricaId = intent.getStringExtra(EXTRA_RUBRICA_ID).orEmpty()
        val rubrica = RubricasDemoData.obtenerRubricasBase().find { it.id == rubricaId }
            ?: RubricasDemoData.obtenerRubricasBase().first()

        setContent {
            AppEsfocar_Grupo15Theme {
                EvaluacionScreen(
                    rubrica = rubrica,
                    onBackClick = {
                        finish()
                    },
                    onGuardarEvaluacion = { aspirante, nota, observaciones ->
                        Toast.makeText(
                            this,
                            "Evaluación de $aspirante guardada con nota $nota",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                )
            }
        }
    }
}
