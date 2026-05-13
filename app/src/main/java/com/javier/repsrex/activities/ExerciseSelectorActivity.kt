package com.javier.repsrex.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.javier.repsrex.adapters.ExerciseSelectorAdapter
import com.javier.repsrex.adapters.ExerciseSelectorItem
import com.javier.repsrex.data.ExerciseDAO
import com.javier.repsrex.databinding.ActivityExerciseSelectorBinding
import com.javier.repsrex.utils.SetsRepsDialog

class ExerciseSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseSelectorBinding
    private lateinit var adapter: ExerciseSelectorAdapter
    private var routineId: Int = -1
    private var routineCategory: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityExerciseSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recibir datos
        routineId = intent.getIntExtra("ROUTINE_ID", -1)
        routineCategory = intent.getStringExtra("ROUTINE_CATEGORY") ?: ""

        if (routineId == -1 || routineCategory.isEmpty()) {
            Toast.makeText(this, "Error loading exercises", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.toolbarTitle.text = "Add Exercise"
        binding.categoryHint.text = "Showing exercises for: ${routineCategory.uppercase()}"

        setupRecyclerView()
        cargarEjercicios()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = ExerciseSelectorAdapter(
            items = emptyList(),
            onClick = { position -> onExerciseClick(position) }
        )
        binding.exercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.exercisesRecyclerView.adapter = adapter
    }

    private fun cargarEjercicios() {
        val exerciseDAO = ExerciseDAO(this)
        val exercises = exerciseDAO.getExercisesByCategory(routineCategory)

        adapter.updateData(exercises)
        binding.exercisesCountTextView.text = "${exercises.size} exercises available"
    }

    private fun onExerciseClick(position: Int) {
        val exercise = adapter.items[position]
        mostrarDialogSetsReps(exercise)
    }

    private fun mostrarDialogSetsReps(exercise: ExerciseSelectorItem) {
        val dialog = SetsRepsDialog() { which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    cargarEjercicios()
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    finish()
                }
            }
        }
        val args = Bundle()
        args.putInt("ROUTINE_ID", routineId)
        args.putString("EXERCISE_ID", exercise.id)
        args.putString("EXERCISE_NAME", exercise.name)
        dialog.arguments = args
        dialog.show(supportFragmentManager, "SetsRepsDialog")
    }

    /*fun reiniciarSelector() {
        val intent = Intent(this, ExerciseSelectorActivity::class.java)
        intent.putExtra("ROUTINE_ID", routineId)
        intent.putExtra("ROUTINE_CATEGORY", routineCategory)
        startActivity(intent)
        finish()
    }*/



}