package com.javier.repsrex.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.javier.repsrex.databinding.ActivityRoutineDetailBinding

class RoutineDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityRoutineDetailBinding

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
        val routineId = intent.getIntExtra("ROUTINE_ID", -1)

        // FAB para añadir ejercicio
        binding.fabAddExercise.setOnClickListener {
            Toast.makeText(this, "Add exercise", Toast.LENGTH_SHORT).show()
        }
    }

    fun onExerciseClick(position: Int) {
        // TODO: Abrir detalle del ejercicio NO HAY OPCION DE EDITAR
        Toast.makeText(this, "Exercise clicked: $position", Toast.LENGTH_SHORT).show()
    }

}