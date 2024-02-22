package com.example.weatherapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather?")
    fun getData(
        @Query("lat",encoded=true) lat:Double?,
        @Query("lon",encoded=true) lon:Double?,
        @Query("appid",encoded=true) appid:String
        ) : Call<WeatherData>
}