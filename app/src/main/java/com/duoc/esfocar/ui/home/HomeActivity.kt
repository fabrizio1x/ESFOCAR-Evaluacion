package com.duoc.esfocar.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.esfocar.MainActivity
import com.duoc.esfocar.data.EvaluacionEntity
import com.duoc.esfocar.ui.rubricas.RubricasActivity
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import com.duoc.esfocar.viewmodel.EvaluacionesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : ComponentActivity() {

    companion object {
        const val EXTRA_NOMBRE = "nombre"
        const val EXTRA_CORREO = "correo"
    }

    private val evaluacionesViewModel: EvaluacionesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val nombre = intent.getStringExtra(EXTRA_NOMBRE).orEmpty()
        val correo = intent.getStringExtra(EXTRA_CORREO).orEmpty()

        setContent {
            val evaluaciones by evaluacionesViewModel.evaluaciones.collectAsState(initial = emptyList())
            val hayConexion by evaluacionesViewModel.hayConexion.collectAsState(initial = true)

            AppEsfocar_Grupo15Theme {
                HomeScreen(
                    nombre = nombre,
                    correo = correo,
                    evaluaciones = evaluaciones,
                    hayConexion = hayConexion,
                    onCerrarSesion = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

enum class HomeTab(val titulo: String, val icon: ImageVector) {
    EVALUACIONES("Evaluaciones", Icons.AutoMirrored.Filled.Assignment),
    DOCUMENTOS("Documentos", Icons.Filled.Description),
    PERFIL("Perfil", Icons.Filled.Person)
}

data class DocumentoItem(
    val id: String,
    val nombre: String,
    val fecha: String,
    val esPdf: Boolean,
    val uriString: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    nombre: String = "",
    correo: String = "",
    evaluaciones: List<EvaluacionEntity> = emptyList(),
    hayConexion: Boolean = true,
    onCerrarSesion: () -> Unit = {}
) {
    val context = LocalContext.current
    var tabSeleccionada by rememberSaveable { mutableStateOf(HomeTab.EVALUACIONES) }
    var menuDesplegado by remember { mutableStateOf(false) }
    var nombreUsuario by rememberSaveable { mutableStateOf(if (nombre.isNotBlank()) nombre else "Docente") }
    var sedeUsuario by rememberSaveable { mutableStateOf("Santiago") }
    var cargoUsuario by rememberSaveable { mutableStateOf("Instructor") }

    val documentos = remember {
        mutableStateListOf(
            DocumentoItem(
                id = "doc-1",
                nombre = "Pauta_Control_Transito_Vehicular.pdf",
                fecha = "20-10-2026",
                esPdf = true
            ),
            DocumentoItem(
                id = "doc-2",
                nombre = "Rubrica_Procedimientos_Seguridad.docx",
                fecha = "18-10-2026",
                esPdf = false
            )
        )
    }

    val launcherDocumento = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            var nombreArchivo = "Documento_${System.currentTimeMillis()}"
            context.contentResolver.query(it, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    nombreArchivo = cursor.getString(nameIndex)
                }
            }

            val esPdf = nombreArchivo.lowercase(Locale.ROOT).endsWith(".pdf")
            val formatoFecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val fechaActual = formatoFecha.format(Date())

            documentos.add(
                0,
                DocumentoItem(
                    id = "doc-${System.currentTimeMillis()}",
                    nombre = nombreArchivo,
                    fecha = fechaActual,
                    esPdf = esPdf,
                    uriString = it.toString()
                )
            )

            Toast.makeText(context, "Documento cargado: $nombreArchivo", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "ESFOCAR",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = { menuDesplegado = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Opciones",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    DropdownMenu(
                        expanded = menuDesplegado,
                        onDismissRequest = { menuDesplegado = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = nombreUsuario,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = if (correo.isNotEmpty()) correo else "docente@esfocar.cl",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = { menuDesplegado = false },
                            leadingIcon = {
                                Icon(Icons.Filled.Person, contentDescription = null)
                            }
                        )

                        HorizontalDivider()

                        DropdownMenuItem(
                            text = { Text("Cerrar sesión") },
                            onClick = {
                                menuDesplegado = false
                                onCerrarSesion()
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                HomeTab.values().forEach { tab ->
                    NavigationBarItem(
                        selected = tabSeleccionada == tab,
                        onClick = { tabSeleccionada = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.titulo) },
                        label = { Text(tab.titulo) }
                    )
                }
            }
        },
        floatingActionButton = {
            if (tabSeleccionada == HomeTab.EVALUACIONES) {
                FloatingActionButton(
                    onClick = {
                        context.startActivity(Intent(context, RubricasActivity::class.java))
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Nueva Evaluación")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = tabSeleccionada,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "CambioPestana"
            ) { pestana ->
                when (pestana) {
                    HomeTab.EVALUACIONES -> TabEvaluaciones(
                        nombre = nombreUsuario,
                        evaluaciones = evaluaciones,
                        hayConexion = hayConexion
                    )
                    HomeTab.DOCUMENTOS -> TabDocumentos(
                        documentos = documentos,
                        onCargarClick = {
                            launcherDocumento.launch(
                                arrayOf(
                                    "application/pdf",
                                    "application/msword",
                                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                )
                            )
                        },
                        onVisualizarClick = { doc ->
                            Toast.makeText(context, "Visualizando: ${doc.nombre}", Toast.LENGTH_SHORT).show()
                        },
                        onEliminarClick = { doc ->
                            documentos.remove(doc)
                            Toast.makeText(context, "Documento eliminado", Toast.LENGTH_SHORT).show()
                        }
                    )
                    HomeTab.PERFIL -> TabPerfil(
                        nombre = nombreUsuario,
                        correo = correo,
                        sede = sedeUsuario,
                        cargo = cargoUsuario,
                        onGuardarPerfil = { nuevoNombre, nuevaSede, nuevoCargo ->
                            nombreUsuario = nuevoNombre
                            sedeUsuario = nuevaSede
                            cargoUsuario = nuevoCargo
                            Toast.makeText(context, "Datos de usuario actualizados correctamente", Toast.LENGTH_SHORT).show()
                        },
                        onCerrarSesion = onCerrarSesion
                    )
                }
            }
        }
    }
}

@Composable
fun TabEvaluaciones(
    nombre: String,
    evaluaciones: List<EvaluacionEntity>,
    hayConexion: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Bienvenido, ${if (nombre.isNotEmpty()) nombre else "Docente"}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Escuela de Formación — Sistema de Evaluación Práctica",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedContent(
                    targetState = hayConexion,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "CambioEstadoRed"
                ) { conectado ->
                    val colorEstado = if (conectado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (conectado) Icons.Filled.Wifi else Icons.Filled.WifiOff,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = colorEstado
                        )
                        Text(
                            text = if (conectado) {
                                "Conectado a la red"
                            } else {
                                val guardadas = if (evaluaciones.size == 1) "1 evaluación guardada" else "${evaluaciones.size} evaluaciones guardadas"
                                "Modo Terreno sin conexión: $guardadas en el teléfono"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = colorEstado,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Text(
            text = "Evaluaciones Recientes (${evaluaciones.size})",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        if (evaluaciones.isEmpty()) {
            Text(
                text = "Aún no hay evaluaciones guardadas. Usa el botón + para evaluar a un aspirante.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }

        evaluaciones.forEach { evaluacion ->
            TarjetaEvaluacion(
                aspirante = evaluacion.aspirante,
                rubrica = evaluacion.rubricaTitulo,
                calificacion = String.format(Locale.US, "%.2f", evaluacion.nota),
                estado = if (evaluacion.aprobada) "Aprobado" else "No aprobado"
            )
        }
    }
}

@Composable
fun TarjetaEvaluacion(
    aspirante: String,
    rubrica: String,
    calificacion: String,
    estado: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = aspirante,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rubrica,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Estado: $estado",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (estado == "Aprobado") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = calificacion,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun TabDocumentos(
    documentos: List<DocumentoItem>,
    onCargarClick: () -> Unit,
    onVisualizarClick: (DocumentoItem) -> Unit,
    onEliminarClick: (DocumentoItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.FolderOpen,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Documentos y Rúbricas Oficiales",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Carga pautas de evaluación y manuales institucionales en formato PDF o Word.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onCargarClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Filled.UploadFile, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cargar documento (PDF o Word)")
                }
            }
        }

        Text(
            text = "Documentos disponibles (${documentos.size})",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        if (documentos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay documentos cargados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(documentos, key = { it.id }) { doc ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (doc.esPdf) Icons.Filled.PictureAsPdf else Icons.AutoMirrored.Filled.Article,
                                contentDescription = null,
                                tint = if (doc.esPdf) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = doc.nombre,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Cargado el: ${doc.fecha}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { onVisualizarClick(doc) }) {
                                Icon(
                                    imageVector = Icons.Filled.Visibility,
                                    contentDescription = "Visualizar",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = { onEliminarClick(doc) }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditarPerfilDialog(
    nombreActual: String,
    sedeActual: String,
    cargoActual: String,
    onDismissRequest: () -> Unit,
    onConfirmarEdicion: (nombre: String, sede: String, cargo: String) -> Unit
) {
    var nombre by remember { mutableStateOf(nombreActual) }
    var sede by remember { mutableStateOf(sedeActual) }
    var cargo by remember { mutableStateOf(cargoActual) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Editar Perfil",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cargo,
                    onValueChange = { cargo = it },
                    label = { Text("Rol") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = sede,
                    onValueChange = { sede = it },
                    label = { Text("Sede") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotBlank() && sede.isNotBlank() && cargo.isNotBlank()) {
                        onConfirmarEdicion(nombre.trim(), sede.trim(), cargo.trim())
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun TabPerfil(
    nombre: String,
    correo: String,
    sede: String,
    cargo: String,
    onGuardarPerfil: (nombre: String, sede: String, cargo: String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    var mostrarDialogoEditar by remember { mutableStateOf(false) }

    if (mostrarDialogoEditar) {
        EditarPerfilDialog(
            nombreActual = nombre,
            sedeActual = sede,
            cargoActual = cargo,
            onDismissRequest = { mostrarDialogoEditar = false },
            onConfirmarEdicion = { nuevoNombre, nuevaSede, nuevoCargo ->
                mostrarDialogoEditar = false
                onGuardarPerfil(nuevoNombre, nuevaSede, nuevoCargo)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
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
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (nombre.isNotEmpty()) nombre else "Docente",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = if (correo.isNotEmpty()) correo else "docente@esfocar.cl",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Institución: ESFOCAR",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Rol: $cargo",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Sede: $sede",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { mostrarDialogoEditar = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Editar perfil",
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cerrar sesión",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppEsfocar_Grupo15Theme(darkTheme = false) {
        HomeScreen(
            nombre = "Instructor Demo",
            correo = "instructor@esfocar.cl"
        )
    }
}
