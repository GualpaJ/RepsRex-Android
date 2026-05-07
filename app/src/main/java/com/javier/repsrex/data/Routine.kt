package com.javier.repsrex.data

import com.javier.repsrex.R

data class Routine(
    val id: Int,
    var name: String,
    var type: String,        // TODO "strength", "cardio", "stretching", "plyometrics", depende de la tabla exercises, REVISAR MAS ADELANTE !!!
    var frequency: Int,      // veces por semana (1, 2, 3, 4, 5, 6, 7)
    var iconRes: Int = R.drawable.ic_gym  // icono por defecto
) {
    companion object {
        const val TABLE_NAME = "routines"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_TYPE = "type"
        const val COLUMN_FREQUENCY = "frequency"
        const val COLUMN_ICON_RES = "icon_res"

        const val SQL_CREATE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME TEXT NOT NULL," +
                    "$COLUMN_TYPE TEXT," +
                    "$COLUMN_FREQUENCY INTEGER DEFAULT 3," +
                    "$COLUMN_ICON_RES INTEGER DEFAULT -1)"

        const val SQL_DELETE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}