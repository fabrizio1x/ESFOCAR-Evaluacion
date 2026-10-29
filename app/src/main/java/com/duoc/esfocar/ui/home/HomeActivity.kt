package com.duoc.esfocar.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import com.duoc.esfocar.R
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
import com.duoc.esfocar.data.DocumentoEntity
import com.duoc.esfocar.data.EvaluacionEntity
import com.duoc.esfocar.recursos.AlmacenDocumentos
import com.duoc.esfocar.ui.aspirante.FichaAspiranteActivity
import com.duoc.esfocar.ui.rubricas.RubricasActivity
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import com.duoc.esfocar.viewmodel.DocumentosViewModel
import com.duoc.esfocar.viewmodel.EvaluacionesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : ComponentActivity() {

    companion object {
        const val EXTRA_NOMBRE = "nombre"
        const val EXTRA_CORREO = "correo"
        const val EXTRA_CARGO = "cargo"
    }

    private val evaluacionesViewModel: EvaluacionesViewModel by viewModels()
    private val documentosViewModel: DocumentosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val nombre = intent.getStringExtra(EXTRA_NOMBRE).orEmpty()
        val correo = intent.getStringExtra(EXTRA_CORREO).orEmpty()
        val cargo = intent.getStringExtra(EXTRA_CARGO).orEmpty().ifBlank { "Docente" }

        setContent {
            val evaluaciones by evaluacionesViewModel.evaluaciones.collectAsState(initial = emptyList())
            val hayConexion by evaluacionesViewModel.hayConexion.collectAsState(initial = true)
            val documentos by documentosViewModel.documentos.collectAsState(initial = emptyList())

            AppEsfocar_Grupo15Theme {
                HomeScreen(
                    nombre = nombre,
                    correo = correo,
                    cargo = cargo,
                    evaluaciones = evaluaciones,
                    hayConexion = hayConexion,
                    documentos = documentos,
                    onCargarDocumento = { uri ->
                        documentosViewModel.cargar(uri) { mensaje ->
                            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                        }
                    },
                    onVisualizarDocumento = { documento ->
                        if (!AlmacenDocumentos.abrir(this, documento)) {
                            Toast.makeText(this, "No hay una aplicación para abrir ${documento.nombre}", Toast.LENGTH_LONG).show()
                        }
                    },
                    onEliminarDocumento = { documento ->
                        documentosViewModel.eliminar(documento) {
                            Toast.makeText(this, "Documento eliminado", Toast.LENGTH_SHORT).show()
                        }
                    },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    nombre: String = "",
    correo: String = "",
    cargo: String = "Docente",
    evaluaciones: List<EvaluacionEntity> = emptyList(),
    hayConexion: Boolean = true,
    documentos: List<DocumentoEntity> = emptyList(),
    onCargarDocumento: (Uri) -> Unit = {},
    onVisualizarDocumento: (DocumentoEntity) -> Unit = {},
    onEliminarDocumento: (DocumentoEntity) -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    val context = LocalContext.current
    var tabSeleccionada by rememberSaveable { mutableStateOf(HomeTab.EVALUACIONES) }
    var menuDesplegado by remember { mutableStateOf(false) }
    var nombreUsuario by rememberSaveable { mutableStateOf(if (nombre.isNotBlank()) nombre else "Docente") }
    var sedeUsuario by rememberSaveable { mutableStateOf("Santiago") }
    var cargoUsuario by rememberSaveable { mutableStateOf(if (cargo.isNotBlank()) cargo else "Docente") }

    val launcherDocumento = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let(onCargarDocumento)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_esfocar),
                        contentDescription = "Logo ESFOCAR",
                        modifier = Modifier.height(34.dp)
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
                        hayConexion = hayConexion,
                        onEvaluacionClick = { evaluacion ->
                            val intent = Intent(context, FichaAspiranteActivity::class.java)
                            intent.putExtra(FichaAspiranteActivity.EXTRA_ASPIRANTE, evaluacion.aspirante)
                            context.startActivity(intent)
                        }
                    )
                    HomeTab.DOCUMENTOS -> TabDocumentos(
                        documentos = documentos,
                        onCargarClick = {
                            launcherDocumento.launch(AlmacenDocumentos.TIPOS_PERMITIDOS)
                        },
                        onVisualizarClick = onVisualizarDocumento,
                        onEliminarClick = onEliminarDocumento
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
    hayConexion: Boolean,
    onEvaluacionClick: (EvaluacionEntity) -> Unit = {}
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
                estado = if (evaluacion.aprobada) "Aprobado" else "No aprobado",
                onClick = { onEvaluacionClick(evaluacion) }
            )
        }
    }
}

@Composable
fun TarjetaEvaluacion(
    aspirante: String,
    rubrica: String,
    calificacion: String,
    estado: String,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
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
    documentos: List<DocumentoEntity>,
    onCargarClick: () -> Unit,
    onVisualizarClick: (DocumentoEntity) -> Unit,
    onEliminarClick: (DocumentoEntity) -> Unit
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
                                    text = "Cargado el: ${formatearFechaDocumento(doc.fecha)}",
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
            correo = "instructor@esfocar.cl",
            cargo = "Instructor"
        )
    }
}

fun formatearFechaDocumento(fecha: Long): String {
    return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(fecha))
}
