package com.duoc.esfocar.ui.register

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import com.duoc.esfocar.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.esfocar.data.UsuarioDaoEnMemoria
import com.duoc.esfocar.repository.UsuarioRepository
import com.duoc.esfocar.ui.theme.AppEsfocar_Grupo15Theme
import com.duoc.esfocar.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterClick: () -> Unit = {},
    onBackToLoginClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_esfocar),
                contentDescription = "Logo Oficial ESFOCAR",
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(72.dp)
            )

            Text(
                text = "Registro de Docente / Instructor",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Crear Cuenta Nueva",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = viewModel.nombre,
                        onValueChange = { viewModel.onNombreChange(it) },
                        label = { Text("Nombre") },
                        placeholder = { Text("Ej. Juan") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null
                            )
                        },
                        isError = viewModel.errorNombre != null,
                        supportingText = viewModel.errorNombre?.let { { Text(it) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = viewModel.apellido,
                        onValueChange = { viewModel.onApellidoChange(it) },
                        label = { Text("Apellido") },
                        placeholder = { Text("Ej. Pérez") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Badge,
                                contentDescription = null
                            )
                        },
                        isError = viewModel.errorApellido != null,
                        supportingText = viewModel.errorApellido?.let { { Text(it) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = viewModel.correo,
                        onValueChange = { viewModel.onCorreoChange(it) },
                        label = { Text("Correo institucional") },
                        placeholder = { Text("docente@esfocar.cl") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = null
                            )
                        },
                        isError = viewModel.errorCorreo != null,
                        supportingText = viewModel.errorCorreo?.let { { Text(it) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = viewModel.contrasena,
                        onValueChange = { viewModel.onContrasenaChange(it) },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null
                            )
                        },
                        isError = viewModel.errorContrasena != null,
                        supportingText = viewModel.errorContrasena?.let { { Text(it) } },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = viewModel.confirmarContrasena,
                        onValueChange = { viewModel.onConfirmarContrasenaChange(it) },
                        label = { Text("Confirmar contraseña") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null
                            )
                        },
                        isError = viewModel.errorConfirmacion != null,
                        supportingText = viewModel.errorConfirmacion?.let { { Text(it) } },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { viewModel.seleccionarDocente() }
                        ) {
                            Checkbox(
                                checked = !viewModel.esInstructor,
                                onCheckedChange = { viewModel.seleccionarDocente() },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Rol Docente",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (!viewModel.esInstructor) FontWeight.SemiBold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { viewModel.seleccionarInstructor() }
                        ) {
                            Checkbox(
                                checked = viewModel.esInstructor,
                                onCheckedChange = { viewModel.seleccionarInstructor() },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Rol Instructor",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (viewModel.esInstructor) FontWeight.SemiBold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    viewModel.errorRegistro?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = onRegisterClick,
                        enabled = viewModel.botonHabilitado,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Registrarse",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    TextButton(
                        onClick = onBackToLoginClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "¿Ya tienes cuenta? Inicia sesión aquí",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Registro exclusivo para personal docente e instructores de ESFOCAR",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "Registro Modo Claro", showBackground = true)
@Composable
fun RegisterScreenPreviewClaro() {
    AppEsfocar_Grupo15Theme(darkTheme = false) {
        RegisterScreen(viewModel = RegisterViewModel(UsuarioRepository(UsuarioDaoEnMemoria())))
    }
}

@Preview(name = "Registro Modo Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RegisterScreenPreviewOscuro() {
    AppEsfocar_Grupo15Theme(darkTheme = true) {
        RegisterScreen(viewModel = RegisterViewModel(UsuarioRepository(UsuarioDaoEnMemoria())))
    }
}
