package com.example.eletriccarapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.eletriccarapp.R

class MainActivity : AppCompatActivity() {
    lateinit var btnCalculate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViews()
        setUpListeners()
    }

    private fun setUpViews(){
        btnCalculate = findViewById(R.id.btn_calculate)
    }

    private fun setUpListeners() {
        btnCalculate.setOnClickListener{
           startActivity(Intent(this , CalculateAutonomyActivity::class.java))
        }
    }
}

