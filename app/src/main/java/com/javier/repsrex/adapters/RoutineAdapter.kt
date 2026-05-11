package com.javier.repsrex.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.javier.repsrex.data.Routine
import com.javier.repsrex.databinding.ItemRoutineBinding

class RoutineAdapter(
    var items: List<Routine>,
    val onClick: (Int) -> Unit,
    val onEdit: (Int) -> Unit,
    val onDelete: (Int) -> Unit,
) : RecyclerView.Adapter<RoutineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRoutineBinding.inflate(layoutInflater, parent, false)
        return RoutineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = items[position]

        holder.render(routine)

        holder.itemView.setOnClickListener {
            onClick(position)
        }

        holder.itemView.setOnLongClickListener {
            onEdit(position)
            true
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(dataSet: List<Routine>) {
        items = dataSet
        notifyDataSetChanged()
    }
}

class RoutineViewHolder(val binding: ItemRoutineBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(routine: Routine) {
        binding.titleTextView.text = routine.name
        binding.subtitleText.text = "${routine.frequency}x/week · ${routine.type.uppercase()}"
        // Icono fijo por ahora, sin UX fancy
        binding.iconImageView.setImageResource(com.javier.repsrex.R.drawable.ic_gym)
    }
}