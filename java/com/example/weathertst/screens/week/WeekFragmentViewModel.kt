package com.example.weathertst.screens.week

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertst.api.RetrofitRepository
import com.example.weathertst.model.weeksWeather.WeeksWeatherResponse
import com.example.weathertst.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class WeekFragmentViewModel :ViewModel(){

    val weeksWeather: MutableLiveData<Resource<WeeksWeatherResponse>> = MutableLiveData()
    var weeksWeatherResponse: WeeksWeatherResponse? = null

    fun getWeeksWeather(lat: Double, lon: Double) = viewModelScope.launch {
        weeksWeather.postValue(Resource.Loading())
        try {
            val response = RetrofitRepository().retrofitService.getWeeksWeather(lat, lon)
            weeksWeather.postValue(handleWeeksWeatherResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> weeksWeather.postValue(Resource.Error("Network Failure"))
                else -> weeksWeather.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private fun handleWeeksWeatherResponse(response: Response<WeeksWeatherResponse>): Resource<WeeksWeatherResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                weeksWeatherResponse = resultResponse
                return Resource.Success(weeksWeatherResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}