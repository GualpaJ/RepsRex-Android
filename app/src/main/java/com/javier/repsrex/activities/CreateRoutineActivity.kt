package com.javier.repsrex.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.javier.repsrex.databinding.ActivityCreateRoutineBinding

class CreateRoutineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateRoutineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}