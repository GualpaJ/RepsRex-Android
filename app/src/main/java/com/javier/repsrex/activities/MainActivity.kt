package com.javier.repsrex.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.javier.repsrex.network.QuoteService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        // Cargo los datos de la BD
        routineList = routineDAO.getAll()

        // 🔍 LOG para ver cuántas rutinas hay
        Log.i("MAIN", "📊 Rutinas cargadas: ${routineList.size}")
        for (r in routineList) {
            Log.i("MAIN", "   - ${r.name} (id: ${r.id})")
        }

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

        // FAB - abre pantalla para crear nueva rutina
        binding.addRoutineFAB.setOnClickListener {
            val intent = Intent(this, CreateRoutineActivity::class.java)
            startActivity(intent)
        }

        // Carga la frase motivadora desde API
        fetchNewQuote()
    }

    // Click normal: abre la rutina
    fun onRoutineClick(position: Int) {
        val routine = routineList[position]
        Toast.makeText(this, "Abriendo: ${routine.name}", Toast.LENGTH_SHORT).show()
        // TODO: Luego abrir detalle
    }

    // Long click: edita
    fun onRoutineLongClick(position: Int) {
        val routine = routineList[position]
        val intent = Intent(this, CreateRoutineActivity::class.java)
        intent.putExtra("ROUTINE_ID", routine.id)
        startActivity(intent)
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

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun fetchNewQuote() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val quotes = QuoteService.getInstance().getRandomQuote()
                if (quotes.isNotEmpty()) {
                    val quote = quotes[0]
                    withContext(Dispatchers.Main) {
                        // Si el autor es unknown o está vacío, usamos frase de respaldo
                        if (quote.a.isNullOrEmpty() || quote.a == "unknown") {
                            binding.motivationalPhraseTextView.text = "Simply put, you believe that things or people make you unhappy, but this is not accurate. You make yourself unhappy"
                            binding.motivationalAuthorTextView.text = "— Wayne Dyer"
                        } else {
                            binding.motivationalPhraseTextView.text = quote.q
                            binding.motivationalAuthorTextView.text = "— ${quote.a}"
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    binding.motivationalPhraseTextView.text = "Simply put, you believe that things or people make you unhappy, but this is not accurate. You make yourself unhappy"
                    binding.motivationalAuthorTextView.text = "— Wayne Dyer"
                }
            }
        }
    }
}