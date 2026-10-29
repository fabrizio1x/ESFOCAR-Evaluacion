package com.duoc.esfocar.recursos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object EvidenciaFotografica {

    private const val CARPETA = "evidencias"

    fun crearArchivo(context: Context): File {
        val carpeta = File(context.filesDir, CARPETA)
        if (!carpeta.exists()) carpeta.mkdirs()
        return File(carpeta, "evidencia_${System.currentTimeMillis()}.jpg")
    }

    fun obtenerUri(context: Context, archivo: File): Uri {
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
    }

    fun cargarMiniatura(ruta: String, ladoMaximo: Int = 480): Bitmap? {
        val archivo = File(ruta)
        if (!archivo.exists() || archivo.length() == 0L) return null

        val medidas = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(ruta, medidas)

        var escala = 1
        while (medidas.outWidth / (escala * 2) >= ladoMaximo && medidas.outHeight / (escala * 2) >= ladoMaximo) {
            escala *= 2
        }

        val opciones = BitmapFactory.Options().apply { inSampleSize = escala }
        return BitmapFactory.decodeFile(ruta, opciones)
    }
}
