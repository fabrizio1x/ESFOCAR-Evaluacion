package com.duoc.esfocar.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documentos")
data class DocumentoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val ruta: String,
    val tipoMime: String,
    val fecha: Long = System.currentTimeMillis()
) {
    val esPdf: Boolean
        get() = tipoMime == "application/pdf" || nombre.lowercase().endsWith(".pdf")
}
