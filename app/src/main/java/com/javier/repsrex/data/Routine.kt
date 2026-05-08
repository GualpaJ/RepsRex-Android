package com.javier.repsrex.data

import com.javier.repsrex.R

data class Routine(
    val id: Int,
    var name: String,
    var type: String,           // ej: "strength", "cardio"
    var frequency: Int,         // número de días a la semana
    var days: String = "",      // "Mon, Wed, Fri"
    var iconRes: Int = R.drawable.ic_gym  // icono por defecto
) {
    companion object {
        const val TABLE_NAME = "routines"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_TYPE = "type"
        const val COLUMN_FREQUENCY = "frequency"
        const val COLUMN_DAYS = "days"
        const val COLUMN_ICON_RES = "icon_res"

        const val SQL_CREATE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME TEXT NOT NULL," +
                    "$COLUMN_TYPE TEXT," +
                    "$COLUMN_FREQUENCY INTEGER DEFAULT 3," +
                    "$COLUMN_DAYS TEXT," +
                    "$COLUMN_ICON_RES INTEGER DEFAULT -1)"

        const val SQL_DELETE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}