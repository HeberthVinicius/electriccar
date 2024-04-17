package com.example.electriccarapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.electriccarapp.R
import com.example.electriccarapp.data.CarFactory
import com.example.electriccarapp.ui.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CarsFragment : Fragment() {
    lateinit var carList: RecyclerView
    lateinit var fabCalculate: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews(view)
        setUpList()
        setUpListeners()
    }

    private fun setUpViews(view: View){
        view.apply {
            carList = findViewById(R.id.rv_information)
            fabCalculate = findViewById(R.id.fab_calculate)
        }
    }

    private fun setUpList() {
        val adapter = CarAdapter(CarFactory.list)
        carList.adapter = adapter
    }

    private fun setUpListeners() {

        fabCalculate.setOnClickListener{
            startActivity(Intent(context , CalculateAutonomyActivity::class.java))
        }
    }
}