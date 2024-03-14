package com.example.electriccarapp.data

import com.example.electriccarapp.domain.Car

object CarFactory {
    val list = listOf(
        Car(
            id = 1,
            price = "$ 300,000",
            battery = "300 kWh",
            power = "1000 hp",
            recharge = "40 min",
            urlPhoto = "www.google.com"
        ),
        Car(
            id = 2,
            price = "$ 350,000",
            battery = "400 kWh",
            power = "1200 hp",
            recharge = "50 min",
            urlPhoto = "www.google.com"
        )
    )
}