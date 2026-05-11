package com.javier.repsrex.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.javier.repsrex.adapters.CategoryChipAdapter
import com.javier.repsrex.data.ExerciseDAO
import com.javier.repsrex.data.Routine
import com.javier.repsrex.data.RoutineDAO
import com.javier.repsrex.databinding.ActivityCreateRoutineBinding

class CreateRoutineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateRoutineBinding

    // Variables importantes
    private var selectedCategory = ""
    private val selectedDays = mutableSetOf<String>()

    // Para saber si estamos editando o creando
    private var routineId = -1
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Veo si me pasaron un ID (vengo de editar una rutina)
        routineId = intent.getIntExtra("ROUTINE_ID", -1)
        isEditing = routineId != -1

        // Cargar datos si es edición
        if (isEditing) {
            cargarDatosParaEditar()
        }

        // Cargar las categorías desde la tabla exercises
        cargarCategorias()

        // Configurar los chips de días de la semana
        configurarDiasSemana()

        // Botón guardar
        binding.btnSaveRoutine.setOnClickListener {
            guardarRutina()
        }

        // Botón cancelar
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    // Cargo las categorías desde la tabla exercises
    private fun cargarCategorias() {
        val exerciseDAO = ExerciseDAO(this)
        val categories = exerciseDAO.getDistinctCategories()

        if (categories.isEmpty()) {
            Toast.makeText(this, "No categories found. Please wait.", Toast.LENGTH_SHORT).show()
            return
        }

        selectedCategory = categories[0]

        val adapter = CategoryChipAdapter(categories) { category ->
            selectedCategory = category
        }
        binding.categoriesRecyclerView.adapter = adapter
    }

    // Configuro los chips de los días de la semana
    private fun configurarDiasSemana() {
        val chips = listOf(
            binding.chipMon to "Mon",
            binding.chipTue to "Tue",
            binding.chipWed to "Wed",
            binding.chipThu to "Thu",
            binding.chipFri to "Fri",
            binding.chipSat to "Sat",
            binding.chipSun to "Sun"
        )

        chips.forEach { (chip, dayName) ->
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedDays.add(dayName)
                } else {
                    selectedDays.remove(dayName)
                }
                actualizarResumenDias()
            }
        }
    }

    // Actualizo el texto que dice "X days selected"
    private fun actualizarResumenDias() {
        val count = selectedDays.size
        binding.selectedDaysSummary.text = when (count) {
            0 -> "No days selected"
            1 -> "1 day per week"
            else -> "$count days per week"
        }
    }

    // Guardo la rutina en la base de datos
    private fun guardarRutina() {
        val name = binding.routineNameInput.text.toString().trim()
        val frequency = selectedDays.size

        if (name.isEmpty()) {
            binding.routineNameInput.error = "Give your routine a name"
            return
        }

        if (selectedCategory.isEmpty()) {
            Toast.makeText(this, "Select a training category", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedDays.isEmpty()) {
            Toast.makeText(this, "Select at least one training day", Toast.LENGTH_SHORT).show()
            return
        }

        val daysString = selectedDays.joinToString(", ")

        val routine = Routine(
            id = routineId,
            name = name,
            type = selectedCategory,
            frequency = frequency,
            days = daysString,
            iconRes = -1
        )

        val dao = RoutineDAO(this)
        dao.save(routine)

        Toast.makeText(this, if (isEditing) "Routine updated!" else "Routine created!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun cargarDatosParaEditar() {
        val dao = RoutineDAO(this)
        val routine = dao.getById(routineId)

        routine?.let {
            binding.routineNameInput.setText(it.name)

            val daysList = it.days.split(", ")

            selectedDays.clear()

            daysList.forEach { day ->
                when (day) {
                    "Mon" -> {
                        binding.chipMon.isChecked = true
                        selectedDays.add("Mon")
                    }
                    "Tue" -> {
                        binding.chipTue.isChecked = true
                        selectedDays.add("Tue")
                    }
                    "Wed" -> {
                        binding.chipWed.isChecked = true
                        selectedDays.add("Wed")
                    }
                    "Thu" -> {
                        binding.chipThu.isChecked = true
                        selectedDays.add("Thu")
                    }
                    "Fri" -> {
                        binding.chipFri.isChecked = true
                        selectedDays.add("Fri")
                    }
                    "Sat" -> {
                        binding.chipSat.isChecked = true
                        selectedDays.add("Sat")
                    }
                    "Sun" -> {
                        binding.chipSun.isChecked = true
                        selectedDays.add("Sun")
                    }
                }
            }

            actualizarResumenDias()
            selectedCategory = it.type
        }
    }
}