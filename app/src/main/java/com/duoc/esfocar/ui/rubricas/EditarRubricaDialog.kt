package com.duoc.esfocar.ui.rubricas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun EditarRubricaDialog(
    rubrica: RubricaItem,
    onDismissRequest: () -> Unit,
    onConfirmarEdicion: (RubricaItem) -> Unit
) {
    var titulo by rememberSaveable { mutableStateOf(rubrica.titulo) }
    var descripcion by rememberSaveable { mutableStateOf(rubrica.descripcion) }
    var tiempoTexto by rememberSaveable { mutableStateOf(rubrica.tiempoEstimadoMinutos.toString()) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Editar Rúbrica",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título de la rúbrica") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción del procedimiento") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = tiempoTexto,
                    onValueChange = { valor ->
                        if (valor.all { it.isDigit() }) {
                            tiempoTexto = valor
                        }
                    },
                    label = { Text("Tiempo estimado (minutos)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val minutos = tiempoTexto.toIntOrNull() ?: rubrica.tiempoEstimadoMinutos
                    val rubricaActualizada = rubrica.copy(
                        titulo = if (titulo.isNotBlank()) titulo.trim() else rubrica.titulo,
                        descripcion = if (descripcion.isNotBlank()) descripcion.trim() else rubrica.descripcion,
                        tiempoEstimadoMinutos = if (minutos > 0) minutos else rubrica.tiempoEstimadoMinutos
                    )
                    onConfirmarEdicion(rubricaActualizada)
                }
            ) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}
