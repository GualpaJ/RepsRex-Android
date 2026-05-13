package com.javier.repsrex.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.javier.repsrex.R
import com.javier.repsrex.databinding.ItemExerciseSelectorBinding

data class ExerciseSelectorItem(
    val id: String,
    val name: String,
    val muscles: String
)

class ExerciseSelectorAdapter(
    var items: List<ExerciseSelectorItem>,
    val onClick: (Int) -> Unit
) : RecyclerView.Adapter<ExerciseSelectorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseSelectorViewHolder {
        val binding = ItemExerciseSelectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseSelectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseSelectorViewHolder, position: Int) {
        val item = items[position]
        holder.render(item)

        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(dataSet: List<ExerciseSelectorItem>) {
        items = dataSet
        notifyDataSetChanged()
    }
}

class ExerciseSelectorViewHolder(val binding: ItemExerciseSelectorBinding) : RecyclerView.ViewHolder(binding.root) {
    fun render(item: ExerciseSelectorItem) {
        binding.exerciseNameTextView.text = item.name
        binding.exerciseMusclesTextView.text = item.muscles
        binding.exerciseIconImageView.setImageResource(R.drawable.ic_gym)
    }
}