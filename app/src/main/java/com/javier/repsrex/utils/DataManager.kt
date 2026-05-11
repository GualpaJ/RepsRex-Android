package com.javier.repsrex.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.javier.repsrex.data.Routine
import java.io.BufferedReader
import java.io.InputStreamReader

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val appContext = context.applicationContext

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys = ON;")

        // Crear tabla de rutinas
        db.execSQL(Routine.SQL_CREATE)

        // Ejecutar el SQL completo de ejercicios desde assets
        executeSqlFromAssets(db, "create_exercises_table.sql")

        // Insertar rutinas de ejemplo
        insertSampleRoutines(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }

    fun onDestroy(db: SQLiteDatabase) {
        db.execSQL(Routine.SQL_DELETE)
        db.execSQL("DROP TABLE IF EXISTS exercises")
    }

    /**
     * Lee un archivo SQL desde la carpeta assets y ejecuta TODAS las sentencias
     * Si una sentencia falla, la saltamos y seguimos con la siguiente
     */
    private fun executeSqlFromAssets(db: SQLiteDatabase, fileName: String) {
        try {
            val inputStream = appContext.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val sql = StringBuilder()

            var line: String? = reader.readLine()
            while (line != null) {
                sql.append(line)
                line = reader.readLine()
            }
            reader.close()
            inputStream.close()

            // Dividir por ";" y ejecutar cada sentencia individualmente
            val statements = sql.toString().split(";")
            var sentenciasEjecutadas = 0
            var sentenciasFallidas = 0

            for (statement in statements) {
                val trimmed = statement.trim()
                if (trimmed.isNotEmpty()) {
                    try {
                        db.execSQL(trimmed)
                        sentenciasEjecutadas++
                    } catch (e: Exception) {
                        sentenciasFallidas++
                        // Solo logueamos el error para debug, pero no paramos la ejecución
                        Log.e("SQL_ERROR", "Fallo al ejecutar sentencia #${sentenciasEjecutadas + sentenciasFallidas}", e)
                    }
                }
            }

            Log.i("DATABASE", "SQL ejecutado: $sentenciasEjecutadas OK, $sentenciasFallidas fallaron")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun insertSampleRoutines(db: SQLiteDatabase) {
        // Limpiar por si acaso
        db.delete(Routine.TABLE_NAME, null, null)

        val sampleRoutines = listOf(
            Routine(id = -1, name = "Pull Day", type = "strength", frequency = 2, days = "Mon, Tue"),
            Routine(id = -1, name = "Push Day", type = "strength", frequency = 2, days = "Tue, Fri"),
            Routine(id = -1, name = "Legs", type = "strength", frequency = 2, days = "Wed, Sat"),
            Routine(id = -1, name = "Cardio", type = "cardio", frequency = 2, days = "Mon, Thu")
        )

        sampleRoutines.forEach { routine ->
            val values = android.content.ContentValues().apply {
                put(Routine.COLUMN_NAME, routine.name)
                put(Routine.COLUMN_TYPE, routine.type)
                put(Routine.COLUMN_FREQUENCY, routine.frequency)
                put(Routine.COLUMN_DAYS, routine.days)
                put(Routine.COLUMN_ICON_RES, routine.iconRes)
            }
            db.insert(Routine.TABLE_NAME, null, values)
        }
    }

    companion object {
        // FIXME: Cambiar si tocamos base de datos, sino desinstalar app entero y listo !
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "RepsRex.db"
    }
}