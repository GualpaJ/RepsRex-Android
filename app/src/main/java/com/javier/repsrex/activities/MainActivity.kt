package com.javier.repsrex.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.javier.repsrex.R
import com.javier.repsrex.adapters.RoutineAdapter
import com.javier.repsrex.data.Routine
import com.javier.repsrex.data.RoutineDAO
import com.javier.repsrex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: RoutineAdapter
    var routineList: List<Routine> = emptyList()
    lateinit var routineDAO: RoutineDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar DAO
        routineDAO = RoutineDAO(this)
        routineList = routineDAO.getAll()

        // Configurar adapter
        adapter = RoutineAdapter(routineList, ::onRoutineClick, ::onRoutineLongClick, ::onRoutineDelete)
        binding.routineRecyclerView.adapter = adapter

        // Swipe para eliminar
        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        deleteRoutine(position)
                    }
                }
            }
        )
        itemTouchHelper.attachToRecyclerView(binding.routineRecyclerView)

        // FAB (por ahora solo un Toast)
        binding.addRoutineFAB.setOnClickListener {
            //Toast.makeText(this, "Añadir rutina - Próximamente", Toast.LENGTH_SHORT).show() -- previo a abri la pantalla nueva para debug
            val intent = Intent(this, CreateRoutineActivity::class.java)
            startActivity(intent)
        }
    }

    // Click normal: abre la rutina
    fun onRoutineClick(position: Int) {
        val routine = routineList[position]
        Toast.makeText(this, "Abriendo: ${routine.name}", Toast.LENGTH_SHORT).show()
        // TODO: Luego abrir detalle
    }

    // Long click: edita (por ahora solo Toast)
    fun onRoutineLongClick(position: Int) {
        val routine = routineList[position]
        Toast.makeText(this, "Editar: ${routine.name} - Próximamente", Toast.LENGTH_SHORT).show()
        // TODO: Luego abrir diálogo de edición
    }

    // Eliminar con confirmación
    fun onRoutineDelete(position: Int) {
        deleteRoutine(position)
    }

    private fun deleteRoutine(position: Int) {
        val routine = routineList[position]

        MaterialAlertDialogBuilder(this)
            .setTitle("Borrar rutina")
            .setMessage("¿Seguro que quieres borrar \"${routine.name}\"?")
            .setPositiveButton("Sí") { _, _ ->
                routineDAO.delete(routine)
                refreshList()
                Toast.makeText(this, "Rutina eliminada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar") { _, _ ->
                refreshList()
            }
            .show()
    }

    private fun refreshList() {
        routineList = routineDAO.getAll()
        adapter.updateData(routineList)
    }
}