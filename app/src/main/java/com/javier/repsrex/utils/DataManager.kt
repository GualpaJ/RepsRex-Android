package com.javier.repsrex.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.javier.repsrex.data.Routine

// NOTA: Por ahora solo tenemos Routine, luego añadiremos más tablas si es necesario

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys = ON;")
        db.execSQL(Routine.SQL_CREATE)
        // TODO FALTA AÑADIR LA TABLA EXERCISES
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }

    fun onDestroy(db: SQLiteDatabase) {
        db.execSQL(Routine.SQL_DELETE)
        // Aquí luego borrarás otras tablas
    }

    companion object {
        // FIXME :Cambiar si tocamos base de datos, sino desintalar app entero y listo !
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "RepsRex.db"
    }
}