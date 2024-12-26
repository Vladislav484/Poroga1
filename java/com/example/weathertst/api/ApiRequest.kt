package com.example.weathertst.api

import com.example.weathertst.model.currentWeather.CurrentWeatherResponse
import com.example.weathertst.model.geocoding.LocationResponse
import com.example.weathertst.model.weeksWeather.WeeksWeatherResponse
import com.example.weathertst.utils.AppId
import com.example.weathertst.utils.lang
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double,
        @Query("appid")
        apiKey: String = AppId,
        @Query("units")
        units: String = com.example.weathertst.utils.units,
        @Query("lang")
        language: String = lang
    ): Response<CurrentWeatherResponse>

    @GET("geo/1.0/direct")
    suspend fun getLocationUsingCity(
        @Query("q")
        city: String?,
        @Query("appid")
        apiKey: String? = AppId,
        @Query("limit")
        limit: Int = 5,
        @Query("lang")
        language: String = lang
    ): Response<LocationResponse>

    @GET("/data/2.5/forecast/daily")
    suspend fun getWeeksWeather(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double,
        @Query("units")
        units: String = com.example.weathertst.utils.units,
        @Query("cnt")
        cnt:Int = 7,
        @Query("lang")
        lang: String = com.example.weathertst.utils.lang,
        @Query("appid")
        app_id: String = AppId,
    ):Response<WeeksWeatherResponse>

}