package com.example.weatherapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApiInterface {
    @GET("weather?")
    fun getData(
        @Query("q",encoded=true) q:String?,
        @Query("appid",encoded=true) appid:String
    ) : Call<WeatherData>
}