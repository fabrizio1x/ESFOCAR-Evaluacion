package com.duoc.esfocar.ui.aspirante

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import com.duoc.esfocar.viewmodel.EvaluacionesViewModel

class FichaAspiranteActivity : ComponentActivity() {

    companion object {
        const val EXTRA_ASPIRANTE = "aspirante"
    }

    private val evaluacionesViewModel: EvaluacionesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val aspirante = intent.getStringExtra(EXTRA_ASPIRANTE).orEmpty()
        val evaluacionesDelAspirante = evaluacionesViewModel.evaluacionesDe(aspirante)

        setContent {
            val evaluaciones by evaluacionesDelAspirante.collectAsState(initial = emptyList())

            AppEsfocar_Grupo15Theme {
                FichaAspiranteScreen(
                    aspirante = aspirante,
                    evaluaciones = evaluaciones,
                    onBackClick = {
                        finish()
                    }
                )
            }
        }
    }
}
