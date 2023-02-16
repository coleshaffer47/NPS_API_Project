package com.example.npsapiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.npsapiproject.databinding.ActivityNpsListBinding

class NPSActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNpsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNpsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}