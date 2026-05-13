package com.javier.repsrex.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.javier.repsrex.R
import com.javier.repsrex.databinding.ItemExerciseBinding

// Clase simple para los datos del ejercicio en la rutina
data class ExerciseItem(
    val id: Int,
    val name: String,
    val muscles: String,
    val sets: Int,
    val reps: String
)

class ExerciseAdapter(
    var items: List<ExerciseItem>,
    val onClick: (Int) -> Unit,
    val onLongClick: (Int) -> Unit,
) : RecyclerView.Adapter<ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val item = items[position]
        holder.render(item)

        holder.itemView.setOnClickListener {
            onClick(position)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(dataSet: List<ExerciseItem>) {
        items = dataSet
        notifyDataSetChanged()
    }
}

class ExerciseViewHolder(val binding: ItemExerciseBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(item: ExerciseItem) {
        binding.exerciseNameTextView.text = item.name
        binding.exerciseMusclesTextView.text = item.muscles
        binding.setsTextView.text = item.sets.toString()
        binding.repsTextView.text = item.reps
        binding.exerciseIconImageView.setImageResource(R.drawable.ic_gym)
    }
}