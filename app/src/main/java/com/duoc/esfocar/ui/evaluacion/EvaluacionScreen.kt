package com.duoc.esfocar.ui.evaluacion

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.duoc.esfocar.recursos.EvidenciaFotografica
import com.duoc.esfocar.ui.rubricas.CriterioItem
import com.duoc.esfocar.ui.rubricas.RubricaItem
import com.duoc.esfocar.ui.rubricas.RubricasDemoData
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import com.duoc.esfocar.viewmodel.CalculadoraNotas
import com.duoc.esfocar.viewmodel.ValidadorEvaluacion
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluacionScreen(
    rubrica: RubricaItem,
    hayConexion: Boolean = true,
    onBackClick: () -> Unit = {},
    onGuardarEvaluacion: (String, Double, String, String?) -> Unit = { _, _, _, _ -> }
) {
    val context = LocalContext.current
    var nombreAspirante by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var errorAspirante by remember { mutableStateOf<String?>(null) }
    var errorObservaciones by remember { mutableStateOf<String?>(null) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var fotoRuta by rememberSaveable { mutableStateOf<String?>(null) }
    var archivoPendiente by rememberSaveable { mutableStateOf<String?>(null) }
    var errorCamara by remember { mutableStateOf<String?>(null) }

    val launcherCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { fotoTomada ->
        if (fotoTomada) {
            fotoRuta = archivoPendiente
            errorCamara = null
        } else {
            archivoPendiente?.let { java.io.File(it).delete() }
            archivoPendiente = null
        }
    }

    fun abrirCamara() {
        val archivo = EvidenciaFotografica.crearArchivo(context)
        archivoPendiente = archivo.absolutePath
        launcherCamara.launch(EvidenciaFotografica.obtenerUri(context, archivo))
    }

    val launcherPermisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            abrirCamara()
        } else {
            errorCamara = "Debes permitir el uso de la cámara para adjuntar evidencia"
        }
    }

    val puntajes = remember {
        mutableStateMapOf<String, Double>().apply {
            rubrica.criterios.forEach { criterio ->
                put(criterio.id, 7.0)
            }
        }
    }

    val notaCalculada: Double = CalculadoraNotas.calcularPromedioPonderado(
        rubrica.criterios.map { crit -> Pair(puntajes[crit.id] ?: 1.0, crit.ponderacion) }
    )

    val colorContenedorNota by animateColorAsState(
        targetValue = if (CalculadoraNotas.estaAprobado(notaCalculada)) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.errorContainer
        },
        animationSpec = tween(durationMillis = 350),
        label = "AnimacionColorContenedor"
    )

    val colorTextoNota by animateColorAsState(
        targetValue = if (CalculadoraNotas.estaAprobado(notaCalculada)) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        },
        animationSpec = tween(durationMillis = 350),
        label = "AnimacionColorTexto"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Evaluación Práctica",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                visible = !hayConexion,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.WifiOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "Sin conexión: la evaluación se guardará en el teléfono y no se perderá.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = rubrica.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Categoría: ${rubrica.categoria} • Tiempo: ${rubrica.tiempoEstimadoMinutos} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Datos del Evaluado",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = nombreAspirante,
                        onValueChange = {
                            nombreAspirante = it
                            if (errorAspirante != null) errorAspirante = ValidadorEvaluacion.validarAspirante(it)
                        },
                        label = { Text("Nombre del Aspirante / Alumno") },
                        placeholder = { Text("Ej. Aspirante Morales, R.") },
                        leadingIcon = {
                            Icon(Icons.Filled.Person, contentDescription = null)
                        },
                        isError = errorAspirante != null,
                        supportingText = errorAspirante?.let { { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Text(
                text = "Criterios de Desempeño (${rubrica.criterios.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            rubrica.criterios.forEach { criterio ->
                TarjetaCriterioEvaluacion(
                    criterio = criterio,
                    puntajeSeleccionado = puntajes[criterio.id] ?: 7.0,
                    onPuntajeChange = { nuevoPuntaje ->
                        puntajes[criterio.id] = nuevoPuntaje
                    }
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = colorContenedorNota
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Calificación Práctica",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = colorTextoNota
                        )
                        Text(
                            text = if (CalculadoraNotas.estaAprobado(notaCalculada)) "Condición: Aprobado" else "Condición: No Aprobado",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorTextoNota
                        )
                    }

                    Text(
                        text = String.format(Locale.US, "%.2f", notaCalculada),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorTextoNota
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Retroalimentación Cualitativa Pedagógica",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Observación directa escrita por el instructor en terreno (sin asistencia de IA).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = observaciones,
                        onValueChange = {
                            observaciones = it
                            if (errorObservaciones != null) errorObservaciones = ValidadorEvaluacion.validarRetroalimentacion(it)
                        },
                        label = { Text("Observaciones y recomendaciones") },
                        placeholder = { Text("Ej. El aspirante aplicó correctamente el protocolo de aproximación...") },
                        isError = errorObservaciones != null,
                        supportingText = errorObservaciones?.let { { Text(it) } },
                        minLines = 3,
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            TarjetaEvidenciaFotografica(
                fotoRuta = fotoRuta,
                errorCamara = errorCamara,
                onTomarFotoClick = {
                    val permisoConcedido = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (permisoConcedido) {
                        abrirCamara()
                    } else {
                        launcherPermisoCamara.launch(Manifest.permission.CAMERA)
                    }
                }
            )

            AnimatedVisibility(
                visible = errorAspirante != null || errorObservaciones != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Complete el nombre del aspirante y las observaciones requeridas.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Button(
                onClick = {
                    errorAspirante = ValidadorEvaluacion.validarAspirante(nombreAspirante)
                    errorObservaciones = ValidadorEvaluacion.validarRetroalimentacion(observaciones)

                    if (ValidadorEvaluacion.puedeGuardar(nombreAspirante, observaciones)) {
                        mostrarDialogoExito = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Finalizar y Guardar Evaluación",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "Evaluación Guardada",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Aspirante: $nombreAspirante")
                    Text("Rúbrica: ${rubrica.titulo}")
                    Text(
                        text = "Nota Final: ${String.format(Locale.US, "%.2f", notaCalculada)}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (fotoRuta != null) {
                        Text("Evidencia fotográfica adjunta")
                    }
                    Text(
                        text = if (hayConexion) {
                            "Registrada en el almacenamiento local del dispositivo."
                        } else {
                            "Registrada en el teléfono (Modo Terreno sin conexión)."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoExito = false
                        onGuardarEvaluacion(nombreAspirante, notaCalculada, observaciones, fotoRuta)
                    }
                ) {
                    Text("Aceptar y Salir")
                }
            }
        )
    }
}

@Composable
fun TarjetaEvidenciaFotografica(
    fotoRuta: String?,
    errorCamara: String?,
    onTomarFotoClick: () -> Unit
) {
    val miniatura = remember(fotoRuta) {
        fotoRuta?.let { EvidenciaFotografica.cargarMiniatura(it) }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Evidencia Fotográfica (opcional)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Registra con la cámara el resultado del ejercicio en terreno.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (miniatura != null) {
                Image(
                    bitmap = miniatura.asImageBitmap(),
                    contentDescription = "Evidencia fotográfica",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            errorCamara?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedButton(
                onClick = onTomarFotoClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (miniatura != null) "Tomar otra foto" else "Tomar foto de evidencia")
            }
        }
    }
}

@Composable
fun TarjetaCriterioEvaluacion(
    criterio: CriterioItem,
    puntajeSeleccionado: Double,
    onPuntajeChange: (Double) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = criterio.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${criterio.ponderacion}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Text(
                text = criterio.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Nivel de Logro:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = puntajeSeleccionado == 7.0,
                    onClick = { onPuntajeChange(7.0) },
                    label = { Text("Logrado (7.0)") },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )

                FilterChip(
                    selected = puntajeSeleccionado == 5.0,
                    onClick = { onPuntajeChange(5.0) },
                    label = { Text("Aceptable (5.0)") },
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = puntajeSeleccionado == 2.0,
                    onClick = { onPuntajeChange(2.0) },
                    label = { Text("Insuficiente (2.0)") },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EvaluacionScreenPreview() {
    AppEsfocar_Grupo15Theme(darkTheme = false) {
        EvaluacionScreen(
            rubrica = RubricasDemoData.obtenerRubricasBase().first()
        )
    }
}
