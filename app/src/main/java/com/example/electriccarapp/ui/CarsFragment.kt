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
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.electriccarapp.R
import com.example.electriccarapp.data.CarsApi
import com.example.electriccarapp.domain.Car
import com.example.electriccarapp.ui.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    lateinit var noInternetConnectionImage: ImageView
    lateinit var noInternetConnectionText: TextView
    lateinit var carsApi: CarsApi


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
        setupRetroFit()
        setUpViews(view)
        setUpListeners()
    }

    override fun onResume() {
        super.onResume()
        if (checkForInternet(context)) {
            //callService() //Tratamamento de chamadas HTTP substituído pela implementação do Retrofit
            getAllCars()
        } else {
            emptyState()
        }
    }

    private fun setupRetroFit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://heberthvinicius.github.io/APITestElectricCar/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        carsApi = retrofit.create(CarsApi::class.java)
    }

    fun getAllCars() {
        carsApi.getAllCars().enqueue(object : Callback<List<Car>>{
            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                if (response.isSuccessful) {
                    progressBar.isVisible = false
                    noInternetConnectionImage.isVisible = false
                    noInternetConnectionText.isVisible = false
                    response.body()?.let {
                        setUpList(it)
                    }
                } else {
                    Toast.makeText(context,  R.string.response_error, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Car>>, t: Throwable) {
                Toast.makeText(context,  R.string.response_error, Toast.LENGTH_LONG).show()
                //emptyState()
            }
        })
    }

    private fun emptyState() {
        progressBar.isVisible = false
        carList.isVisible = false
        noInternetConnectionImage.isVisible = true
        noInternetConnectionText.isVisible = true
    }

    private fun setUpViews(view: View){
        view.apply {
            carList = findViewById(R.id.rv_information)
            fabCalculate = findViewById(R.id.fab_calculate)
            progressBar = findViewById(R.id.pb_loader)
            noInternetConnectionImage = findViewById(R.id.iv_empty_state)
            noInternetConnectionText = findViewById(R.id.tv_empty_state)
        }
    }

    private fun setUpList(lista: List<Car>) {
        val carAdapter = CarAdapter(lista)
        carList.apply {
            isVisible = true
            adapter = carAdapter
        }
    }

    private fun setUpListeners() {
        fabCalculate.setOnClickListener{
            startActivity(Intent(context , CalculateAutonomyActivity::class.java))
        }
    }

    //Substituído pela implementação do Retrofit
//    private fun callService() {
//        val urlBase = "https://heberthvinicius.github.io/APITestElectricCar/cars.json"
//        MyTask().execute(urlBase)
//    }

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

    // Utilizar o retrofit como abstração do AsyncTask
    inner class MyTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask", "Iniciando...")
            progressBar.isVisible = true
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
                progressBar.isVisible = false
                noInternetConnectionImage.isVisible = false
                noInternetConnectionText.isVisible = false
                //setUpList()

            }catch (ex: Exception) {
                Log.e("Erro ->", ex.message.toString())
            }
        }
    }
}
