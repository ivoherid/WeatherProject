package com.example.weatherapplication

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.weatherapplication.ui.theme.WeatherApplicationTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder

class MainActivity : ComponentActivity() {
    val baseurl = "https://api.openweathermap.org/data/2.5/"
    var lat: Double = 0.0
    var long: Double = 0.0

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.activity_main)
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        Location Button
//        findViewById<Button>(R.id.btn_location).setOnClickListener{
//                fetchLocation()
//        }
        getCurrentWeather()
        getSydneyWeather()
        getMelbourneWeather()
        getNewYorkWeather()
        getSingaporeWeather()
        getDelhiWeather()
        getMumbaiWeather()
    }

    private fun getCurrentWeather() {
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101)
            return
        }
        task.addOnSuccessListener {
            if(it!=null){
                lat = it.latitude
                long = it.longitude
//                Toast.makeText(applicationContext,"${lat},${long}",Toast.LENGTH_SHORT).show()
                val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
                val retrofitBuilder = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseurl)
                    .build()
                    .create(ApiInterface::class.java)
                val retrofitData= retrofitBuilder.getData(lat,long, appid = key )

                retrofitData.enqueue(object : Callback<WeatherData>{
                    override fun onResponse(
                        call: Call<WeatherData>,
                        response: Response<WeatherData>
                    ) {
                        val responseBody = response.body()!!
                        val location = findViewById<TextView>(R.id.current_location) as TextView
                        location.text = responseBody.name.toString()
                        for(weather in responseBody.weather){
                            Toast.makeText(applicationContext,"${lat},${long}",Toast.LENGTH_SHORT).show()
                            val weathercond = findViewById<TextView>(R.id.current_weather) as TextView
                            val weatherdetail = findViewById<TextView>(R.id.weather_detail) as TextView
                            weathercond.text= weather.main
                            weatherdetail.text = weather.description
                        }

                        val temp = findViewById<TextView>(R.id.current_temp)
                        temp.text = responseBody.main.temp.toString() + "°F"
                        val max = findViewById<TextView>(R.id.temp_max)
                        max.text = "max: " + responseBody.main.temp_max.toString() + "°F"
                        val min = findViewById<TextView>(R.id.temp_min)
                        min.text = "min: " + responseBody.main.temp_min.toString() + "°F"
                        val humidity = findViewById<TextView>(R.id.current_humid)
                        humidity.text= responseBody.main.humidity.toString() + "%"
                        val windspeed = findViewById<TextView>(R.id.current_wind_speed)
                        windspeed.text = responseBody.wind.speed.toString() + "Km/H"
                        val pressure = findViewById<TextView>(R.id.current_pressure)
                        pressure.text = responseBody.main.pressure.toString()

                    }

                    override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                        Log.d("MainActivity","OnFailure:" +t.message)
                        Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                    }
                })
            }
        }



    }

    private fun getMumbaiWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "Mumbai"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!
                val location = findViewById<TextView>(R.id.current_location) as TextView
                for(weather in responseBody.weather){
                    Toast.makeText(applicationContext,"${q}",Toast.LENGTH_SHORT).show()
                    val weatherdetail = findViewById<TextView>(R.id.MumbaiWeather) as TextView
                    val temp = findViewById<TextView>(R.id.MumbaiTemp) as TextView
                    weatherdetail.text = weather.main
                    temp.text = responseBody.main.temp.toString() + "°F"
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun getDelhiWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "Delhi"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!
                val location = findViewById<TextView>(R.id.current_location) as TextView
                for(weather in responseBody.weather){
                    Toast.makeText(applicationContext,"${q}",Toast.LENGTH_SHORT).show()
                    val weatherdetail = findViewById<TextView>(R.id.DelhiWeather) as TextView
                    val temp = findViewById<TextView>(R.id.DelhiTemp) as TextView
                    weatherdetail.text = weather.main
                    temp.text = responseBody.main.temp.toString() + "°F"
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun getSydneyWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "Sydney"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!
                val location = findViewById<TextView>(R.id.current_location) as TextView
                for(weather in responseBody.weather){
                    Toast.makeText(applicationContext,"${q}",Toast.LENGTH_SHORT).show()
                    val weatherdetail = findViewById<TextView>(R.id.SydneyWeather) as TextView
                    val temp = findViewById<TextView>(R.id.SydneyTemp) as TextView
                    weatherdetail.text = weather.main
                    temp.text = responseBody.main.temp.toString() + "°F"
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getMelbourneWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "Melbourne"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!
                val location = findViewById<TextView>(R.id.current_location) as TextView
                for(weather in responseBody.weather){
                    Toast.makeText(applicationContext,"${q}",Toast.LENGTH_SHORT).show()
                    val weatherdetail = findViewById<TextView>(R.id.MelbourneWeather) as TextView
                    val temp = findViewById<TextView>(R.id.MelbourneTemp) as TextView
                    weatherdetail.text = weather.main
                    temp.text = responseBody.main.temp.toString() + "°F"
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getSingaporeWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "Singapore"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!
                val location = findViewById<TextView>(R.id.current_location) as TextView
                for(weather in responseBody.weather){
                    Toast.makeText(applicationContext,"${q}",Toast.LENGTH_SHORT).show()
                    val weatherdetail = findViewById<TextView>(R.id.SingaporeWeather) as TextView
                    val temp = findViewById<TextView>(R.id.SingaporeTemp) as TextView
                    weatherdetail.text = weather.main
                    temp.text = responseBody.main.temp.toString() + "°F"
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getNewYorkWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "New York"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!
                val location = findViewById<TextView>(R.id.current_location) as TextView
                for(weather in responseBody.weather){
                    Toast.makeText(applicationContext,"${q}",Toast.LENGTH_SHORT).show()
                    val weatherdetail = findViewById<TextView>(R.id.NewYorkWeather) as TextView
                    val temp = findViewById<TextView>(R.id.NewYorkTemp) as TextView
                    weatherdetail.text = weather.main
                    temp.text = responseBody.main.temp.toString() + "°F"
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
            }
        })
    }


}