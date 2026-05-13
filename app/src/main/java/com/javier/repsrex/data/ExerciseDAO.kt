package com.javier.repsrex.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.javier.repsrex.adapters.ExerciseSelectorItem
import com.javier.repsrex.utils.DatabaseManager

class ExerciseDAO(val context: Context) {

    private lateinit var db: SQLiteDatabase

    companion object {
        const val TABLE_NAME = "exercises"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_PRIMARY_MUSCLES = "primary_muscles"
    }

    fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    fun close() {
        db.close()
    }

    fun getDistinctCategories(): List<String> {
        open()
        val categories = mutableListOf<String>()

        try {
            val cursor = db.rawQuery("SELECT DISTINCT category FROM exercises ORDER BY category", null)
            while (cursor.moveToNext()) {
                categories.add(cursor.getString(0))
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
        return categories
    }

    fun getExercisesByCategory(category: String): List<ExerciseSelectorItem> {
        open()
        val resultList = mutableListOf<ExerciseSelectorItem>()

        try {
            val cursor = db.rawQuery(
                "SELECT id, name, primary_muscles FROM exercises WHERE category = ? ORDER BY name",
                arrayOf(category)
            )
            while (cursor.moveToNext()) {
                resultList.add(
                    ExerciseSelectorItem(
                        id = cursor.getString(0),
                        name = cursor.getString(1),
                        muscles = cursor.getString(2) ?: ""
                    )
                )
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
        return resultList
    }
}