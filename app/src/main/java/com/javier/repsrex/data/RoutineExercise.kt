package com.javier.repsrex.data

data class RoutineExercise(
    val id: Int = 0,
    val routineId: Int,
    val exerciseId: String,
    val sets: Int,
    val reps: String
) {
    companion object {
        const val TABLE_NAME = "routine_exercises"

        const val COLUMN_ID = "id"
        const val COLUMN_ROUTINE_ID = "routine_id"
        const val COLUMN_EXERCISE_ID = "exercise_id"
        const val COLUMN_SETS = "sets"
        const val COLUMN_REPS = "reps"

        const val SQL_CREATE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_ROUTINE_ID INTEGER NOT NULL," +
                    "$COLUMN_EXERCISE_ID TEXT NOT NULL," +
                    "$COLUMN_SETS INTEGER DEFAULT 3," +
                    "$COLUMN_REPS TEXT DEFAULT '10')"

        const val SQL_DELETE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}