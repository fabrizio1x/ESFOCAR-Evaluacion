package com.duoc.esfocar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EvaluacionEntity::class], version = 1, exportSchema = false)
abstract class EsfocarDatabase : RoomDatabase() {

    abstract fun evaluacionDao(): EvaluacionDao

    companion object {
        @Volatile
        private var instancia: EsfocarDatabase? = null

        fun obtener(context: Context): EsfocarDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    EsfocarDatabase::class.java,
                    "esfocar.db"
                ).build().also { instancia = it }
            }
        }
    }
}
