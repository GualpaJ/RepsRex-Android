package com.javier.repsrex.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.javier.repsrex.utils.DatabaseManager

class RoutineDAO(val context: Context) {

    private lateinit var db: SQLiteDatabase

    fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    fun close() {
        db.close()
    }

    fun save(routine: Routine) {
        if (routine.id != -1) {
            update(routine)
        } else {
            insert(routine)
        }
    }

    fun getContentValues(routine: Routine): ContentValues {
        val values = ContentValues()
        values.put(Routine.COLUMN_NAME, routine.name)
        values.put(Routine.COLUMN_TYPE, routine.type)
        values.put(Routine.COLUMN_FREQUENCY, routine.frequency)
        values.put(Routine.COLUMN_DAYS, routine.days)
        values.put(Routine.COLUMN_ICON_RES, routine.iconRes)
        return values
    }

    fun cursorToEntity(cursor: Cursor): Routine {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(Routine.COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(Routine.COLUMN_NAME))
        val type = cursor.getString(cursor.getColumnIndexOrThrow(Routine.COLUMN_TYPE))
        val frequency = cursor.getInt(cursor.getColumnIndexOrThrow(Routine.COLUMN_FREQUENCY))
        val days = cursor.getString(cursor.getColumnIndexOrThrow(Routine.COLUMN_DAYS))
        val iconRes = cursor.getInt(cursor.getColumnIndexOrThrow(Routine.COLUMN_ICON_RES))

        return Routine(id, name, type, frequency, days, iconRes)
    }

    fun insert(routine: Routine) {
        open()
        val values = getContentValues(routine)

        try {
            val newRowId = db.insert(Routine.TABLE_NAME, null, values)
            Log.i("DATABASE", "Inserted routine with id $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun deleteAll() {
        open()
        try {
            val deletedRows = db.delete(Routine.TABLE_NAME, null, null)
            Log.i("DATABASE", "Deleted $deletedRows routines")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun update(routine: Routine) {
        open()
        val values = getContentValues(routine)

        try {
            val updatedRows = db.update(
                Routine.TABLE_NAME,
                values,
                "${Routine.COLUMN_ID} = ${routine.id}",
                null
            )
            Log.i("DATABASE", "Updated $updatedRows rows")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun delete(routine: Routine) {
        open()
        try {
            val deletedRows = db.delete(
                Routine.TABLE_NAME,
                "${Routine.COLUMN_ID} = ${routine.id}",
                null
            )
            Log.i("DATABASE", "Deleted $deletedRows rows")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun getById(id: Int): Routine? {
        open()
        var result: Routine? = null

        try {
            val cursor = db.query(
                Routine.TABLE_NAME,
                null,
                "${Routine.COLUMN_ID} = $id",
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

    fun getAll(): List<Routine> {
        open()
        val resultList: MutableList<Routine> = mutableListOf()

        try {
            val cursor = db.query(
                Routine.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "${Routine.COLUMN_ID} ASC"
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