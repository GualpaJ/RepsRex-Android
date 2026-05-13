package com.javier.repsrex.utils

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.javier.repsrex.R
import com.javier.repsrex.activities.ExerciseSelectorActivity
import com.javier.repsrex.databinding.DialogSetsRepsBinding
import com.javier.repsrex.data.RoutineExercise
import com.javier.repsrex.data.RoutineExerciseDAO

class SetsRepsDialog(var onButtonSelectedListener: (which: Int) -> Unit) : DialogFragment() {

    private lateinit var binding: DialogSetsRepsBinding
    private var routineId: Int = -1
    private var exerciseId: String = ""
    private var exerciseName: String = ""
    private var sets = 3
    private var reps = 10

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSetsRepsBinding.inflate(layoutInflater)

        // Recibir argumentos
        routineId = arguments?.getInt("ROUTINE_ID") ?: -1
        exerciseId = arguments?.getString("EXERCISE_ID") ?: ""
        exerciseName = arguments?.getString("EXERCISE_NAME") ?: ""

        binding.exerciseNameTextView.text = exerciseName
        binding.setsValue.text = sets.toString()
        binding.repsValue.text = reps.toString()

        // Stepper SETS
        binding.btnMinusSets.setOnClickListener {
            if (sets > 1) {
                sets--
                binding.setsValue.text = sets.toString()
            }
        }

        binding.btnPlusSets.setOnClickListener {
            if (sets < 99) {
                sets++
                binding.setsValue.text = sets.toString()
            }
        }

        // Stepper REPS
        binding.btnMinusReps.setOnClickListener {
            if (reps > 1) {
                reps--
                binding.repsValue.text = reps.toString()
            }
        }

        binding.btnPlusReps.setOnClickListener {
            if (reps < 99) {
                reps++
                binding.repsValue.text = reps.toString()
            }
        }

        val dialog = AlertDialog.Builder(requireContext(), R.style.ThemeOverlay_RepsRex_AlertDialog)
            .setView(binding.root)
            .setPositiveButton("Add") { _, _ ->
                guardarEjercicio()
            }
            .setNegativeButton("Cancel", null)
            .create()

        return dialog
    }

    private fun guardarEjercicio() {
        val routineExercise = RoutineExercise(
            routineId = routineId,
            exerciseId = exerciseId,
            sets = sets,
            reps = reps.toString()
        )

        val dao = RoutineExerciseDAO(requireContext())
        dao.insert(routineExercise)

        Toast.makeText(requireContext(), "$exerciseName added with $sets sets x $reps reps", Toast.LENGTH_SHORT).show()

        // Preguntar si quiere añadir otro
        mostrarDialogAñadirOtro()
    }

    private fun mostrarDialogAñadirOtro() {
        AlertDialog.Builder(requireContext(), R.style.ThemeOverlay_RepsRex_AlertDialog)
            .setTitle("Add another exercise?")
            .setMessage("Do you want to add another exercise to this routine?")
            .setPositiveButton("Yes") { _, _ ->
                dismiss()
                //(activity as? ExerciseSelectorActivity)?.reiniciarSelector()
                onButtonSelectedListener(DialogInterface.BUTTON_POSITIVE)
            }
            .setNegativeButton("No") { _, _ ->
                println("Cierra ventana")
                onButtonSelectedListener(DialogInterface.BUTTON_NEGATIVE)
            }
            .show()
    }



}