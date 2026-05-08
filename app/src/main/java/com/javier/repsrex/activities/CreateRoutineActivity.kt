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
    private var selectedCategory = ""      // La categoría que elige el usuario
    private val selectedDays = mutableSetOf<String>()  // Días que marca (ej: "Mon", "Wed")

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

        // Cambio el título de la pantalla según si creo o edito
        if (isEditing) {
            supportActionBar?.title = "Edit Routine"
            cargarDatosParaEditar()
        } else {
            supportActionBar?.title = "New Routine"
        }

        // Botón de volver atrás (flecha)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 1. Cargar las categorías desde la tabla exercises
        cargarCategorias()

        // 2. Configurar los chips de días de la semana
        configurarDiasSemana()

        // 3. Botón guardar
        binding.btnSaveRoutine.setOnClickListener {
            guardarRutina()
        }

        // 4. Botón cancelar
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    // Flecha de volver atrás
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    // Cargo las categorías REALES desde la tabla exercises
    private fun cargarCategorias() {
        val exerciseDAO = ExerciseDAO(this)
        val categories = exerciseDAO.getDistinctCategories()

        if (categories.isEmpty()) {
            Toast.makeText(this, "No categories found. Please wait.", Toast.LENGTH_SHORT).show()
            return
        }

        // Selecciono la primera por defecto
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
            daysList.forEach { day ->
                when (day) {
                    "Mon" -> binding.chipMon.isChecked = true
                    "Tue" -> binding.chipTue.isChecked = true
                    "Wed" -> binding.chipWed.isChecked = true
                    "Thu" -> binding.chipThu.isChecked = true
                    "Fri" -> binding.chipFri.isChecked = true
                    "Sat" -> binding.chipSat.isChecked = true
                    "Sun" -> binding.chipSun.isChecked = true
                }
            }

            selectedCategory = it.type
        }
    }
}