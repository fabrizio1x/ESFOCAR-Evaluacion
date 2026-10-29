package com.duoc.esfocar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [EvaluacionEntity::class, UsuarioEntity::class, DocumentoEntity::class],
    version = 3,
    exportSchema = false
)
abstract class EsfocarDatabase : RoomDatabase() {

    abstract fun evaluacionDao(): EvaluacionDao

    abstract fun usuarioDao(): UsuarioDao

    abstract fun documentoDao(): DocumentoDao

    companion object {
        @Volatile
        private var instancia: EsfocarDatabase? = null

        val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `usuarios` (" +
                        "`correo` TEXT NOT NULL, " +
                        "`nombre` TEXT NOT NULL, " +
                        "`hashContrasena` TEXT NOT NULL, " +
                        "`sal` TEXT NOT NULL, " +
                        "PRIMARY KEY(`correo`))"
                )
            }
        }

        val MIGRACION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `documentos` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`nombre` TEXT NOT NULL, " +
                        "`ruta` TEXT NOT NULL, " +
                        "`tipoMime` TEXT NOT NULL, " +
                        "`fecha` INTEGER NOT NULL)"
                )
            }
        }

        val CUENTAS_DEMO = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                UsuariosDemo.entidades().forEach { usuario ->
                    db.execSQL(
                        "INSERT OR IGNORE INTO usuarios (correo, nombre, hashContrasena, sal) VALUES (?, ?, ?, ?)",
                        arrayOf(usuario.correo, usuario.nombre, usuario.hashContrasena, usuario.sal)
                    )
                }
            }
        }

        fun obtener(context: Context): EsfocarDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    EsfocarDatabase::class.java,
                    "esfocar.db"
                )
                    .addMigrations(MIGRACION_1_2, MIGRACION_2_3)
                    .addCallback(CUENTAS_DEMO)
                    .build()
                    .also { instancia = it }
            }
        }
    }
}
