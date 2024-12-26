package com.example.weathertst.model.weeksWeather

data class WeeksWeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ItemWeekWeather>,
    val message: Double
)