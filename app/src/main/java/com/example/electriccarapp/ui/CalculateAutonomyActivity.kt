package com.example.electriccarapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.electriccarapp.R

class CalculateAutonomyActivity : AppCompatActivity() {
    lateinit var returnArrow: ImageView
    lateinit var kWhPrice: EditText
    lateinit var traveledKm: EditText
    lateinit var btnCalculate: Button
    lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_autonomy)

        setUpViews()
        setUpListeners()
    }

    private fun setUpViews(){
        kWhPrice = findViewById(R.id.et_kwh_price)
        traveledKm = findViewById(R.id.et_traveled_km)
        btnCalculate = findViewById(R.id.btn_calculate)
        result = findViewById(R.id.tv_result)
        returnArrow = findViewById(R.id.iv_return_arrow)
    }

    private fun setUpListeners() {
        btnCalculate.setOnClickListener{
            val typedPrice = kWhPrice.text.toString().toFloat()
            val typedKm = traveledKm.text.toString().toFloat()

            result.text = calculate(typedPrice, typedKm).toString()
        }

        returnArrow.setOnClickListener{
            finish()
        }
    }

    private fun calculate(x: Float, y: Float): Float {
        return x / y
    }
}