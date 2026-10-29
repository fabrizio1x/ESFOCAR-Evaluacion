package com.duoc.esfocar.ui.aspirante

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.esfocar.data.EvaluacionEntity
import com.duoc.esfocar.recursos.EvidenciaFotografica
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import com.duoc.esfocar.viewmodel.ResumenAspirante
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaAspiranteScreen(
    aspirante: String,
    evaluaciones: List<EvaluacionEntity>,
    onBackClick: () -> Unit = {}
) {
    val resumen = ResumenAspirante.desdeNotas(evaluaciones.map { it.nota })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Ficha del Aspirante",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                TarjetaResumenAspirante(aspirante = aspirante, resumen = resumen)
            }

            item {
                Text(
                    text = "Historial de Evaluaciones (${evaluaciones.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            if (evaluaciones.isEmpty()) {
                item {
                    Text(
                        text = "Este aspirante aún no tiene evaluaciones guardadas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            items(evaluaciones, key = { it.id }) { evaluacion ->
                TarjetaEvaluacionAspirante(evaluacion = evaluacion)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TarjetaResumenAspirante(
    aspirante: String,
    resumen: ResumenAspirante?
) {
    val colorCondicion = if (resumen?.aprobado == true) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = aspirante,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = when {
                            resumen == null -> "Sin evaluaciones"
                            resumen.aprobado -> "Condición: Aprobado"
                            else -> "Condición: No Aprobado"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (resumen == null) MaterialTheme.colorScheme.outline else colorCondicion,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Promedio",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = resumen?.let { formatearNota(it.promedio) } ?: "—",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (resumen == null) MaterialTheme.colorScheme.outline else colorCondicion
                    )
                }
            }

            if (resumen != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DatoResumen(
                        titulo = "Evaluaciones",
                        valor = resumen.cantidad.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    DatoResumen(
                        titulo = "Aprobadas",
                        valor = "${resumen.aprobadas} de ${resumen.cantidad}",
                        modifier = Modifier.weight(1f)
                    )
                    DatoResumen(
                        titulo = "Mejor nota",
                        valor = formatearNota(resumen.mejorNota),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun DatoResumen(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = valor,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TarjetaEvaluacionAspirante(evaluacion: EvaluacionEntity) {
    val miniatura = remember(evaluacion.fotoRuta) {
        evaluacion.fotoRuta?.let { EvidenciaFotografica.cargarMiniatura(it) }
    }
    val colorNota: Color = if (evaluacion.aprobada) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = evaluacion.rubricaTitulo,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatearFecha(evaluacion.fecha),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = formatearNota(evaluacion.nota),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorNota
                )
            }

            Text(
                text = evaluacion.observaciones,
                style = MaterialTheme.typography.bodySmall
            )

            if (miniatura != null) {
                Image(
                    bitmap = miniatura.asImageBitmap(),
                    contentDescription = "Evidencia fotográfica",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

fun formatearNota(nota: Double): String {
    return String.format(Locale.US, "%.2f", nota)
}

fun formatearFecha(fecha: Long): String {
    return SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(fecha))
}

@Preview(showBackground = true)
@Composable
fun FichaAspiranteScreenPreview() {
    AppEsfocar_Grupo15Theme(darkTheme = false) {
        FichaAspiranteScreen(
            aspirante = "Ana Rojas",
            evaluaciones = listOf(
                EvaluacionEntity(
                    id = 1,
                    aspirante = "Ana Rojas",
                    rubricaId = "rub-1",
                    rubricaTitulo = "Control y Fiscalización Vehicular",
                    nota = 6.25,
                    observaciones = "Buen posicionamiento y trato respetuoso con el conductor."
                ),
                EvaluacionEntity(
                    id = 2,
                    aspirante = "Ana Rojas",
                    rubricaId = "rub-2",
                    rubricaTitulo = "Procedimientos de Seguridad y Registro Preventivo",
                    nota = 3.8,
                    observaciones = "Omitió zonas críticas durante la palpación sistemática."
                )
            )
        )
    }
}
