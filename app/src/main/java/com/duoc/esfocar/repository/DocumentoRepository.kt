package com.duoc.esfocar.repository

import com.duoc.esfocar.data.DocumentoDao
import com.duoc.esfocar.data.DocumentoEntity
import com.duoc.esfocar.recursos.AlmacenDocumentos
import kotlinx.coroutines.flow.Flow

class DocumentoRepository(private val dao: DocumentoDao) {

    val documentos: Flow<List<DocumentoEntity>> = dao.obtenerTodos()

    suspend fun guardar(documento: DocumentoEntity): Long {
        return dao.insertar(documento)
    }

    suspend fun eliminar(documento: DocumentoEntity) {
        dao.eliminar(documento)
        AlmacenDocumentos.borrarArchivo(documento.ruta)
    }
}
