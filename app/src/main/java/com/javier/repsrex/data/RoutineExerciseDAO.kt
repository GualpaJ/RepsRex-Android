package com.javier.repsrex.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.javier.repsrex.utils.DatabaseManager

class RoutineExerciseDAO(val context: Context) {

    private lateinit var db: SQLiteDatabase

    fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    fun close() {
        db.close()
    }

    fun save(routineExercise: RoutineExercise) {
        if (routineExercise.id != 0) {
            update(routineExercise)
        } else {
            insert(routineExercise)
        }
    }

    fun getContentValues(routineExercise: RoutineExercise): ContentValues {
        val values = ContentValues()
        values.put(RoutineExercise.COLUMN_ROUTINE_ID, routineExercise.routineId)
        values.put(RoutineExercise.COLUMN_EXERCISE_ID, routineExercise.exerciseId)
        values.put(RoutineExercise.COLUMN_SETS, routineExercise.sets)
        values.put(RoutineExercise.COLUMN_REPS, routineExercise.reps)
        return values
    }

    fun cursorToEntity(cursor: Cursor): RoutineExercise {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(RoutineExercise.COLUMN_ID))
        val routineId = cursor.getInt(cursor.getColumnIndexOrThrow(RoutineExercise.COLUMN_ROUTINE_ID))
        val exerciseId = cursor.getString(cursor.getColumnIndexOrThrow(RoutineExercise.COLUMN_EXERCISE_ID))
        val sets = cursor.getInt(cursor.getColumnIndexOrThrow(RoutineExercise.COLUMN_SETS))
        val reps = cursor.getString(cursor.getColumnIndexOrThrow(RoutineExercise.COLUMN_REPS))

        return RoutineExercise(id, routineId, exerciseId, sets, reps)
    }

    fun insert(routineExercise: RoutineExercise) {
        open()
        val values = getContentValues(routineExercise)

        try {
            val newRowId = db.insert(RoutineExercise.TABLE_NAME, null, values)
            Log.i("DATABASE", "Inserted routine_exercise with id $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun update(routineExercise: RoutineExercise) {
        open()
        val values = getContentValues(routineExercise)

        try {
            val updatedRows = db.update(
                RoutineExercise.TABLE_NAME,
                values,
                "${RoutineExercise.COLUMN_ID} = ${routineExercise.id}",
                null
            )
            Log.i("DATABASE", "Updated $updatedRows rows in routine_exercises")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun delete(routineExercise: RoutineExercise) {
        open()
        try {
            val deletedRows = db.delete(
                RoutineExercise.TABLE_NAME,
                "${RoutineExercise.COLUMN_ID} = ${routineExercise.id}",
                null
            )
            Log.i("DATABASE", "Deleted $deletedRows rows from routine_exercises")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun deleteByRoutineId(routineId: Int) {
        open()
        try {
            val deletedRows = db.delete(
                RoutineExercise.TABLE_NAME,
                "${RoutineExercise.COLUMN_ROUTINE_ID} = $routineId",
                null
            )
            Log.i("DATABASE", "Deleted $deletedRows exercises for routine $routineId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun getById(id: Int): RoutineExercise? {
        open()
        var result: RoutineExercise? = null

        try {
            val cursor = db.query(
                RoutineExercise.TABLE_NAME,
                null,
                "${RoutineExercise.COLUMN_ID} = $id",
                null,
                null,
                null,
                null
            )

            if (cursor.moveToNext()) {
                result = cursorToEntity(cursor)
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
        return result
    }

    fun getByRoutineId(routineId: Int): List<RoutineExercise> {
        open()
        val resultList = mutableListOf<RoutineExercise>()

        try {
            val cursor = db.query(
                RoutineExercise.TABLE_NAME,
                null,
                "${RoutineExercise.COLUMN_ROUTINE_ID} = $routineId",
                null,
                null,
                null,
                "${RoutineExercise.COLUMN_ID} ASC"
            )

            while (cursor.moveToNext()) {
                resultList.add(cursorToEntity(cursor))
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
        return resultList
    }

    fun getAll(): List<RoutineExercise> {
        open()
        val resultList = mutableListOf<RoutineExercise>()

        try {
            val cursor = db.query(
                RoutineExercise.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "${RoutineExercise.COLUMN_ID} ASC"
            )

            while (cursor.moveToNext()) {
                resultList.add(cursorToEntity(cursor))
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