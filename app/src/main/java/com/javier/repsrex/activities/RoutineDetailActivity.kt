package com.javier.repsrex.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.javier.repsrex.adapters.ExerciseAdapter
import com.javier.repsrex.data.RoutineDAO
import com.javier.repsrex.data.RoutineExerciseDAO
import com.javier.repsrex.databinding.ActivityRoutineDetailBinding

class RoutineDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityRoutineDetailBinding
    lateinit var exerciseAdapter: ExerciseAdapter
    private var routineId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRoutineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recibir ID de la rutina
        routineId = intent.getIntExtra("ROUTINE_ID", -1)

        if (routineId == -1) {
            Toast.makeText(this, "Error: Routine not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Cargar datos de la rutina
        cargarDatosRutina()

        // Configurar RecyclerView
        setupRecyclerView()

        // Cargar ejercicios de la rutina
        cargarEjercicios()

        // FAB para añadir ejercicio
        binding.fabAddExercise.setOnClickListener {
            val routine = RoutineDAO(this).getById(routineId)
            routine?.let {
                val intent = Intent(this, ExerciseSelectorActivity::class.java)
                intent.putExtra("ROUTINE_ID", routine.id)
                intent.putExtra("ROUTINE_CATEGORY", routine.type)
                startActivity(intent)
            }
        }
    }

    private fun cargarDatosRutina() {
        val routineDAO = RoutineDAO(this)
        val routine = routineDAO.getById(routineId)

        if (routine != null) {
            binding.routineNameTextView.text = routine.name
            binding.categoryTextView.text = routine.type.uppercase()
            binding.frequencyTextView.text = "${routine.frequency}x / week"
        } else {
            Toast.makeText(this, "Routine not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupRecyclerView() {
        exerciseAdapter = ExerciseAdapter(
            items = emptyList(),
            onClick = { position -> onExerciseClick(position) },
            onLongClick = { position -> onExerciseLongClick(position) }
        )
        binding.exercisesRecyclerView.adapter = exerciseAdapter
    }

    private fun cargarEjercicios() {
        val routineExerciseDAO = RoutineExerciseDAO(this)
        val exercises = routineExerciseDAO.getExercisesByRoutineId(routineId)

        exerciseAdapter.updateData(exercises)
        binding.exercisesCountTextView.text = "${exercises.size} exercises"
    }

    fun onExerciseClick(position: Int) {
        val exercise = exerciseAdapter.items[position]
        Toast.makeText(this, "Exercise: ${exercise.name}", Toast.LENGTH_SHORT).show()
        // TODO: No hay opción de editar, solo mostrar info
    }

    fun onExerciseLongClick(position: Int) {
        val exercise = exerciseAdapter.items[position]
        Toast.makeText(this, "Long press on ${exercise.name}", Toast.LENGTH_SHORT).show()
        // TODO: Opcional: mostrar opciones (eliminar de rutina)
    }

    override fun onResume() {
        super.onResume()
        println("RoutineDetailActivity: onResume called")
        cargarEjercicios()
    }
}