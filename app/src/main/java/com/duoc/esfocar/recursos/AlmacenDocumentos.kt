package com.duoc.esfocar.recursos

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import com.duoc.esfocar.data.DocumentoEntity
import java.io.File

object AlmacenDocumentos {

    private const val CARPETA = "documentos"

    val TIPOS_PERMITIDOS = arrayOf(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    )

    fun copiar(context: Context, uri: Uri): DocumentoEntity? {
        val resolver = context.contentResolver
        val nombre = resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) else null
        } ?: "Documento_${System.currentTimeMillis()}"
        val tipoMime = resolver.getType(uri) ?: "application/octet-stream"

        val carpeta = File(File(context.filesDir, CARPETA), System.currentTimeMillis().toString()).apply { mkdirs() }
        val nombreSeguro = nombre.replace(Regex("[^A-Za-z0-9._-]"), "_")
        val destino = File(carpeta, nombreSeguro)

        val entrada = resolver.openInputStream(uri) ?: return null
        entrada.use { origen ->
            destino.outputStream().use { salida -> origen.copyTo(salida) }
        }
        return DocumentoEntity(nombre = nombre, ruta = destino.absolutePath, tipoMime = tipoMime)
    }

    fun abrir(context: Context, documento: DocumentoEntity): Boolean {
        val archivo = File(documento.ruta)
        if (!archivo.exists()) return false
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
        val intent = Intent(Intent.ACTION_VIEW)
            .setDataAndType(uri, documento.tipoMime)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return try {
            context.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }

    fun borrarArchivo(ruta: String) {
        val archivo = File(ruta)
        archivo.delete()
        archivo.parentFile?.takeIf { it.name != CARPETA && it.list().isNullOrEmpty() }?.delete()
    }
}
