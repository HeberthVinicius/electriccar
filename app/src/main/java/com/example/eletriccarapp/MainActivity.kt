package com.example.eletriccarapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.yield

class MainActivity : AppCompatActivity() {
    lateinit var kWhPrice: EditText
    lateinit var traveledkm: EditText
    lateinit var btnCalculate: Button
    lateinit var result: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViews()
        setUpListeners()
    }

    private fun setUpViews(){
        kWhPrice = findViewById(R.id.et_kwh_price)
        traveledkm = findViewById(R.id.et_traveled_km)
        btnCalculate = findViewById(R.id.btn_calculate)
        result = findViewById(R.id.tv_result)
    }

    private fun setUpListeners() {
        btnCalculate.setOnClickListener{
            val typedPrice = kWhPrice.text.toString().toFloat()
//            Log.d("Typed Text:", typedPrice.toString())
            val typedKm = traveledkm.text.toString().toFloat()

            result.text = calculate(typedPrice, typedKm).toString()
        }
    }

    fun calculate(x: Float, y: Float) : Float {
        val calcResult = x / y
        return calcResult
    }
}
