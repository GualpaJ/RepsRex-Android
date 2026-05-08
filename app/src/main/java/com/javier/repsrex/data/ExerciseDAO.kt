package com.javier.repsrex.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.javier.repsrex.utils.DatabaseManager

class ExerciseDAO(val context: Context) {

    private lateinit var db: SQLiteDatabase

    // Abro la base de datos para trabajar
    fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    // Cierro la base de datos cuando termino
    fun close() {
        db.close()
    }

    // Aquí voy a buscar todas las categorías distintas de la tabla exercises
    // Esto me servirá para mostrar las opciones al usuario
    fun getDistinctCategories(): List<String> {
        open()
        val categories = mutableListOf<String>()

        try {
            // La query: "dame todas las categorías sin repetir y ordénalas"
            val cursor = db.rawQuery("SELECT DISTINCT category FROM exercises ORDER BY category", null)

            // Recorro el resultado y voy guardando cada categoría
            while (cursor.moveToNext()) {
                categories.add(cursor.getString(0))
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        // Devuelvo la lista de categorías (ej: ["cardio", "strength", "stretching"...])
        return categories
    }
}