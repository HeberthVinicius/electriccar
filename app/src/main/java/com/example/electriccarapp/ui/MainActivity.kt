package com.example.electriccarapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.electriccarapp.R
import com.example.electriccarapp.data.CarFactory
import com.example.electriccarapp.ui.adapter.CarAdapter

class MainActivity : AppCompatActivity() {
    lateinit var carList: RecyclerView
    lateinit var btnCalculate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViews()
        setUpListeners()
        setUpList()
    }

    private fun setUpViews(){
        carList = findViewById(R.id.rv_information)
        btnCalculate = findViewById(R.id.btn_calculate)
    }

    fun setUpList() {
        val adapter = CarAdapter(CarFactory.list)
        carList.adapter = adapter
    }
    private fun setUpListeners() {
        btnCalculate.setOnClickListener{
           startActivity(Intent(this , CalculateAutonomyActivity::class.java))
        }
    }
}

