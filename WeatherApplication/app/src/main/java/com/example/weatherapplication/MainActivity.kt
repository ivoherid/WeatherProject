package com.example.weatherapplication

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.JsonWriter
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.serialization.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.sql.Timestamp

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
        val filename="current.txt"
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
                    val stringBuilder = StringBuilder()
                    override fun onResponse(
                        call: Call<WeatherData>,
                        response: Response<WeatherData>
                    ) {
                        val currentTimeMillis = System.currentTimeMillis()
                        val timeStamp = Timestamp(currentTimeMillis)
                        val timeview = findViewById<TextView>(R.id.timestamp)
                        timeview.text="Last Updated at: " + timeStamp.toString()

                        stringBuilder.append(timeview.text)
                        stringBuilder.append(System.getProperty("line.separator"))
                        val responseBody = response.body()!!
                        val location = findViewById<TextView>(R.id.current_location) as TextView
                        location.text = responseBody.name.toString()
                        stringBuilder.append(location.text)
                        stringBuilder.append(System.getProperty("line.separator"))
                        for(weather in responseBody.weather){
                            val weathercond = findViewById<TextView>(R.id.current_weather) as TextView
                            val weatherdetail = findViewById<TextView>(R.id.weather_detail) as TextView
                            weathercond.text= weather.main
                            stringBuilder.append(weathercond.text)
                            stringBuilder.append(System.getProperty("line.separator"))
                            weatherdetail.text = weather.description
                            stringBuilder.append(weatherdetail.text)
                            stringBuilder.append(System.getProperty("line.separator"))
                        }

                        val temp = findViewById<TextView>(R.id.current_temp)
                        temp.text = responseBody.main.temp.toString() + "°F"
                        stringBuilder.append(temp.text)
                        stringBuilder.append(System.getProperty("line.separator"))
                        val max = findViewById<TextView>(R.id.temp_max)
                        max.text = "max: " + responseBody.main.temp_max.toString() + "°F"
                        stringBuilder.append(max.text)
                        stringBuilder.append(System.getProperty("line.separator"))
                        val min = findViewById<TextView>(R.id.temp_min)
                        min.text = "min: " + responseBody.main.temp_min.toString() + "°F"
                        stringBuilder.append(min.text)
                        stringBuilder.append(System.getProperty("line.separator"))
                        val humidity = findViewById<TextView>(R.id.current_humid)
                        humidity.text= responseBody.main.humidity.toString() + "%"
                        stringBuilder.append(humidity.text)
                        stringBuilder.append(System.getProperty("line.separator"))
                        val windspeed = findViewById<TextView>(R.id.current_wind_speed)
                        windspeed.text = responseBody.wind.speed.toString() + "Km/H"
                        stringBuilder.append(windspeed.text)
                        stringBuilder.append(System.getProperty("line.separator"))
                        val pressure = findViewById<TextView>(R.id.current_pressure)
                        pressure.text = responseBody.main.pressure.toString()
                        stringBuilder.append(pressure.text)

                        saveDataLocally(filename,stringBuilder.toString())

                    }

                    override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                        Log.d("MainActivity","OnFailure:" +t.message)
                    loadDataLocallyCurrent(filename)
                    }
                })
            }
        }



    }

    private fun getMumbaiWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "Mumbai"
        val filename = q + ".txt"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            val stringBuilder = StringBuilder()
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!
                for(weather in responseBody.weather){
                    Toast.makeText(applicationContext,"${q}",Toast.LENGTH_SHORT).show()
                    val weatherdetail = findViewById<TextView>(R.id.MumbaiWeather) as TextView
                    val temp = findViewById<TextView>(R.id.MumbaiTemp) as TextView
                    weatherdetail.text = weather.main
                    stringBuilder.append(weatherdetail.text)
                    stringBuilder.append(System.getProperty("line.separator"))
                    temp.text = responseBody.main.temp.toString() + "°F"
                    stringBuilder.append(temp.text)
                }
                saveDataLocally(filename,stringBuilder.toString())
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                var line = ""
                val file: File = File(cacheDir,filename)
                val fin = FileReader(file.absoluteFile)
                val bufferedReader = BufferedReader(fin)
                val str = bufferedReader.readLine()
                val str2 = bufferedReader.readLine()
                val weatherdetail = findViewById<TextView>(R.id.MumbaiWeather) as TextView
                val temp = findViewById<TextView>(R.id.MumbaiTemp) as TextView
                weatherdetail.text=str
                weatherdetail.text=str2
            }
        })
    }
    private fun getDelhiWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "Delhi"
        val filename = q + ".txt"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            val stringBuilder = StringBuilder()
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!
                for(weather in responseBody.weather){
                    Toast.makeText(applicationContext,"${q}",Toast.LENGTH_SHORT).show()
                    val weatherdetail = findViewById<TextView>(R.id.DelhiWeather) as TextView
                    val temp = findViewById<TextView>(R.id.DelhiTemp) as TextView
                    weatherdetail.text = weather.main
                    stringBuilder.append(weatherdetail.text)
                    stringBuilder.append(System.getProperty("line.separator"))
                    temp.text = responseBody.main.temp.toString() + "°F"
                    stringBuilder.append(temp.text)
                }
                saveDataLocally(filename,stringBuilder.toString())
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                var line = ""
                val file: File = File(cacheDir,filename)
                val fin = FileReader(file.absoluteFile)
                val bufferedReader = BufferedReader(fin)
                val str = bufferedReader.readLine()
                val str2 = bufferedReader.readLine()
                val weatherdetail = findViewById<TextView>(R.id.DelhiWeather) as TextView
                val temp = findViewById<TextView>(R.id.DelhiTemp) as TextView
                weatherdetail.text=str
                weatherdetail.text=str2
            }
        })
    }


    private fun getSydneyWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "Sydney"
        val filename = q + ".txt"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            val stringBuilder=StringBuilder()
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!
                for(weather in responseBody.weather){
                    Toast.makeText(applicationContext,"${q}",Toast.LENGTH_SHORT).show()
                    val weatherdetail = findViewById<TextView>(R.id.SydneyWeather) as TextView
                    val temp = findViewById<TextView>(R.id.SydneyTemp) as TextView
                    weatherdetail.text = weather.main
                    stringBuilder.append(weatherdetail.text)
                    stringBuilder.append(System.getProperty("line.separator"))
                    temp.text = responseBody.main.temp.toString() + "°F"
                    stringBuilder.append(weatherdetail.text)
                    stringBuilder.append(System.getProperty("line.separator"))
                }
                saveDataLocally(filename,stringBuilder.toString())
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                var line = ""
                val file: File = File(cacheDir,filename)
                val fin = FileReader(file.absoluteFile)
                val bufferedReader = BufferedReader(fin)
                val str = bufferedReader.readLine()
                val str2 = bufferedReader.readLine()
                val weatherdetail = findViewById<TextView>(R.id.SydneyWeather) as TextView
                val temp = findViewById<TextView>(R.id.SydneyTemp) as TextView
                weatherdetail.text=str
                weatherdetail.text=str2
            }
        })
    }

    private fun getMelbourneWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "Melbourne"
        val filename = q + ".txt"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            val stringBuilder = StringBuilder()
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!
                for(weather in responseBody.weather){
                    Toast.makeText(applicationContext,"${q}",Toast.LENGTH_SHORT).show()
                    val weatherdetail = findViewById<TextView>(R.id.MelbourneWeather) as TextView
                    val temp = findViewById<TextView>(R.id.MelbourneTemp) as TextView
                    weatherdetail.text = weather.main
                    stringBuilder.append(System.getProperty("line.separator"))
                    temp.text = responseBody.main.temp.toString() + "°F"
                    stringBuilder.append(temp.text)
                }
                saveDataLocally(filename,stringBuilder.toString())
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                var line = ""
                val file: File = File(cacheDir,filename)
                val fin = FileReader(file.absoluteFile)
                val bufferedReader = BufferedReader(fin)
                val str = bufferedReader.readLine()
                val str2 = bufferedReader.readLine()
                val weatherdetail = findViewById<TextView>(R.id.MelbourneWeather) as TextView
                val temp = findViewById<TextView>(R.id.MelbourneTemp) as TextView
                weatherdetail.text=str
                weatherdetail.text=str2
            }
        })
    }

    private fun getSingaporeWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "Singapore"
        val filename = q + ".txt"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            val stringBuilder= StringBuilder()
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!
                for(weather in responseBody.weather){
                    val weatherdetail = findViewById<TextView>(R.id.SingaporeWeather) as TextView
                    val temp = findViewById<TextView>(R.id.SingaporeTemp) as TextView
                    weatherdetail.text = weather.main
                    stringBuilder.append(weatherdetail.text)
                    stringBuilder.append(System.getProperty("line.separator"))
                    temp.text = responseBody.main.temp.toString() + "°F"
                    stringBuilder.append(temp.text)
                }
                saveDataLocally(filename,stringBuilder.toString())
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                var line = ""
                val file: File = File(cacheDir,filename)
                val fin = FileReader(file.absoluteFile)
                val bufferedReader = BufferedReader(fin)
                val str = bufferedReader.readLine()
                val str2 = bufferedReader.readLine()
                val weatherdetail = findViewById<TextView>(R.id.SingaporeWeather) as TextView
                val temp = findViewById<TextView>(R.id.SingaporeTemp) as TextView
                weatherdetail.text=str
                weatherdetail.text=str2
            }
        })
    }

    private fun getNewYorkWeather(){
        val urls = "https://api.openweathermap.org/data/2.5/"
        val key = "a179b85b772a4e0de1649f9a0a8e6ef0"
        val q = "New York"
        val filename = "NewYork.txt"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urls)
            .build()
            .create(CountryApiInterface::class.java)
        val retrofitData= retrofitBuilder.getData(q, appid = key )

        retrofitData.enqueue(object : Callback<WeatherData>{
            val stringBuilder = StringBuilder()
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val responseBody = response.body()!!


                for(weather in responseBody.weather){
                    val weatherdetail = findViewById<TextView>(R.id.NewYorkWeather) as TextView
                    val temp = findViewById<TextView>(R.id.NewYorkTemp) as TextView
                    weatherdetail.text = weather.main
                    stringBuilder.append(weatherdetail.text)
                    stringBuilder.append(System.getProperty("line.separator"))
                    temp.text = responseBody.main.temp.toString() + "°F"
                    stringBuilder.append(temp.text)
                }
                saveDataLocally(filename,stringBuilder.toString())
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("MainActivity","OnFailure:" +t.message)
                try{
                    var line = ""
                    val file: File = File(cacheDir,filename)
                    val fin = FileReader(file.absoluteFile)
                    val bufferedReader = BufferedReader(fin)
                    val str = bufferedReader.readLine()
                    val str2 = bufferedReader.readLine()
                    val weatherdetail = findViewById<TextView>(R.id.NewYorkWeather) as TextView
                    val temp = findViewById<TextView>(R.id.NewYorkTemp) as TextView
                    weatherdetail.text=str
                    weatherdetail.text=str2
                }
                catch (e :Exception){
                    Toast.makeText(applicationContext,"Failure",Toast.LENGTH_SHORT).show()
                    false
                }

            }
        })
    }


    private fun saveDataLocally(filename: String, stringArray : String){
        val file: File = File(cacheDir, filename)
        var fin = FileWriter(file.absoluteFile)
        fin.write(stringArray)
        fin.close()
        Toast.makeText(applicationContext,stringArray,Toast.LENGTH_LONG).show()
    }

    private fun loadDataLocallyCurrent(filename: String){
        var line = ""
        val file: File = File(cacheDir,filename)
        val fin = FileReader(file.absoluteFile)
        val bufferedReader = BufferedReader(fin)
        val time = bufferedReader.readLine()
        val city = bufferedReader.readLine()
        val weather = bufferedReader.readLine()
        val detail = bufferedReader.readLine()
        val temperatr = bufferedReader.readLine()
        val mintemp = bufferedReader.readLine()
        val maxtemp = bufferedReader.readLine()
        val humid = bufferedReader.readLine()
        val speed = bufferedReader.readLine()
        val press = bufferedReader.readLine()

        val timeview = findViewById<TextView>(R.id.timestamp) as TextView
        val location = findViewById<TextView>(R.id.current_location) as TextView
        val weathercond = findViewById<TextView>(R.id.current_weather) as TextView
        val weatherdetail = findViewById<TextView>(R.id.weather_detail) as TextView
        val temp = findViewById<TextView>(R.id.current_temp)
        val max = findViewById<TextView>(R.id.temp_max)
        val min = findViewById<TextView>(R.id.temp_min)
        val humidity = findViewById<TextView>(R.id.current_humid)
        val windspeed = findViewById<TextView>(R.id.current_wind_speed)
        val pressure = findViewById<TextView>(R.id.current_pressure)

        timeview.text=time
        location.text=city
        weathercond.text=weather
        weatherdetail.text=detail
        temp.text=temperatr
        max.text=maxtemp
        min.text=mintemp
        humidity.text=humid
        windspeed.text=speed
        pressure.text=press
    }


}


