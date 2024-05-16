package com.example.electriccarapp.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.electriccarapp.R
import com.example.electriccarapp.domain.Car
import com.example.electriccarapp.ui.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

class CarsFragment : Fragment() {

    lateinit var carList: RecyclerView
    lateinit var fabCalculate: FloatingActionButton
    lateinit var progressBar: ProgressBar

    var carsArray: ArrayList<Car> = ArrayList()

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
        setUpListeners()
        checkForInternet(context)
        callService()
    }

    private fun setUpViews(view: View){
        view.apply {
            carList = findViewById(R.id.rv_information)
            fabCalculate = findViewById(R.id.fab_calculate)
            progressBar = findViewById(R.id.pb_loader)
        }
    }

    private fun setUpList() {
        carList.visibility = View.VISIBLE
        val adapter = CarAdapter(carsArray)
        carList.adapter = adapter
    }

    private fun setUpListeners() {
        fabCalculate.setOnClickListener{
            startActivity(Intent(context , CalculateAutonomyActivity::class.java))
        }
    }

    private fun callService() {
        val urlBase = "https://heberthvinicius.github.io/APITestElectricCar/cars.json"
        MyTask().execute(urlBase)
    }

    fun checkForInternet(context: Context?) : Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    inner class MyTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask", "Iniciando...")
            progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg url: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val urlBase = URL(url[0])

                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 60000
                urlConnection.readTimeout = 60000
                urlConnection.setRequestProperty(
                    "Accept",
                    "application/json"
                )

                val responseCode = urlConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    var response = urlConnection.inputStream.bufferedReader().use { it.readText() }
                    publishProgress(response)
                }
                else {
                    Log.e("Erro", "Serviço indisponível no momento...")
                }
            } catch (ex: Exception) {
                Log.e("Erro", "Erro ao executar o processamento...")
            } finally {
                urlConnection?.disconnect()
            }

            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray

                for (i in 0 until jsonArray.length()) {
                    val id = jsonArray.getJSONObject(i).getString("id")
                    Log.d("ID ->", id)

                    val price = jsonArray.getJSONObject(i).getString("price")
                    Log.d("Price ->", price)

                    val battery = jsonArray.getJSONObject(i).getString("battery")
                    Log.d("Battery ->", battery)

                    val power = jsonArray.getJSONObject(i).getString("power")
                    Log.d("Power ->", power)

                    val recharge = jsonArray.getJSONObject(i).getString("recharge")
                    Log.d("Recharge ->", recharge)

                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")
                    Log.d("URLPhoto ->", urlPhoto)

                    val model = Car(
                        id = id.toInt(),
                        price = price,
                        battery = battery,
                        power = power,
                        recharge = recharge,
                        urlPhoto = urlPhoto
                    )
                    carsArray.add(model)
                }
                progressBar.visibility = View.GONE
                setUpList()

            }catch (ex: Exception) {
                Log.e("Erro ->", ex.message.toString())
            }
        }
    }
}
