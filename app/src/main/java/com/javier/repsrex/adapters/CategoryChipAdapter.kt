package com.javier.repsrex.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.javier.repsrex.R
import com.javier.repsrex.databinding.ItemCategoryChipBinding

// Este adapter maneja los chips de categorías
// Solo una categoría puede estar seleccionada a la vez
class CategoryChipAdapter(
    private val categories: List<String>,               // Las categorías que vienen de la BD
    private val onCategorySelected: (String) -> Unit
) : RecyclerView.Adapter<CategoryChipAdapter.ChipViewHolder>() {

    // Arrancamos con la primera categoría seleccionada (posición 0)
    private var selectedPosition = 0

    // inflo el layout de cada chip (creo la vista física)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = ItemCategoryChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChipViewHolder(binding)
    }

    // Pongo los datos a cada chip según su posición
    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val category = categories[position]
        val isSelected = position == selectedPosition
        holder.bind(category, isSelected)

        // Cuando el usuario toca un chip...
        holder.itemView.setOnClickListener {
            val oldPosition = selectedPosition
            selectedPosition = position                // Marco esta como seleccionada
            notifyItemChanged(oldPosition)             // Refresco la que perdió selección
            notifyItemChanged(selectedPosition)        // Refresco la nueva seleccionada
            onCategorySelected(category)               // Aviso al Activity
        }
    }

    override fun getItemCount() = categories.size

    // El ViewHolder que contiene el chip
    class ChipViewHolder(private val binding: ItemCategoryChipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: String, isSelected: Boolean) {
            // Pongo el nombre de la categoría con la primera letra mayúscula (más bonito)
            binding.categoryChip.text = category.replaceFirstChar { it.uppercase() }

            // Si está seleccionado, lo pinto de azul con letras blancas
            if (isSelected) {
                binding.categoryChip.setChipBackgroundColorResource(com.google.android.material.R.color.design_default_color_primary)
                binding.categoryChip.setTextColor(binding.root.context.getColor(R.color.on_primary))
            } else {
                // Si no, fondo transparente con borde gris
                binding.categoryChip.setChipBackgroundColorResource(android.R.color.transparent)
                binding.categoryChip.setTextColor(binding.root.context.getColor(R.color.on_surface_variant))
            }
        }
    }
}