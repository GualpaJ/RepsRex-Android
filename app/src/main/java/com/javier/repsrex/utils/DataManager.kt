package com.javier.repsrex.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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
            for (statement in statements) {
                val trimmed = statement.trim()
                if (trimmed.isNotEmpty()) {
                    db.execSQL(trimmed)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun insertSampleRoutines(db: SQLiteDatabase) {
        // Limpiar por si acaso
        db.delete(Routine.TABLE_NAME, null, null)

        val sampleRoutines = listOf(
            Routine(id = -1, name = "Hypertrophy A", type = "strength", frequency = 3),
            Routine(id = -1, name = "Core Stability", type = "stretching", frequency = 5),
            Routine(id = -1, name = "Powerlifting Max", type = "strength", frequency = 3),
            Routine(id = -1, name = "Active Recovery", type = "cardio", frequency = 2)
        )

        sampleRoutines.forEach { routine ->
            val values = android.content.ContentValues().apply {
                put(Routine.COLUMN_NAME, routine.name)
                put(Routine.COLUMN_TYPE, routine.type)
                put(Routine.COLUMN_FREQUENCY, routine.frequency)
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