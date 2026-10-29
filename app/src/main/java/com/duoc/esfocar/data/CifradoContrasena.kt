package com.duoc.esfocar.data

import java.security.MessageDigest
import java.security.SecureRandom

object CifradoContrasena {

    private const val BYTES_SAL = 16

    fun generarSal(): String {
        val bytes = ByteArray(BYTES_SAL)
        SecureRandom().nextBytes(bytes)
        return aHexadecimal(bytes)
    }

    fun cifrar(contrasena: String, sal: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return aHexadecimal(digest.digest("$sal:$contrasena".toByteArray(Charsets.UTF_8)))
    }

    private fun aHexadecimal(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
