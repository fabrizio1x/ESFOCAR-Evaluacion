package com.duoc.esfocar.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.esfocar.data.DocumentoEntity
import com.duoc.esfocar.data.EsfocarDatabase
import com.duoc.esfocar.recursos.AlmacenDocumentos
import com.duoc.esfocar.repository.DocumentoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DocumentosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DocumentoRepository(
        EsfocarDatabase.obtener(application).documentoDao()
    )

    val documentos: Flow<List<DocumentoEntity>> = repository.documentos

    fun cargar(uri: Uri, onResultado: (String) -> Unit) {
        viewModelScope.launch {
            val documento = withContext(Dispatchers.IO) {
                runCatching { AlmacenDocumentos.copiar(getApplication(), uri) }.getOrNull()
            }
            if (documento == null) {
                onResultado("No se pudo cargar el documento")
                return@launch
            }
            repository.guardar(documento)
            onResultado("Documento cargado: ${documento.nombre}")
        }
    }

    fun eliminar(documento: DocumentoEntity, onEliminado: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { repository.eliminar(documento) }
            onEliminado()
        }
    }
}
