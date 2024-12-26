package com.example.weathertst.screens.main


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.geocoding.LocationResponseItem
import com.example.weathertst.api.RetrofitRepository
import com.example.weathertst.model.currentWeather.CurrentWeatherResponse
import com.example.weathertst.model.geocoding.LocationResponse
import com.example.weathertst.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class MainFragmentViewModel(app: Application) : AndroidViewModel(app) {

    val currentWeather: MutableLiveData<Resource<CurrentWeatherResponse>> = MutableLiveData()
    var location: MutableLiveData<Resource<LocationResponse>> = MutableLiveData()
    var selectedLocation: MutableLiveData<LocationResponseItem>? = null
    val currentWeatherLat: MutableLiveData<Resource<CurrentWeatherResponse>> = MutableLiveData()
    var currentWeatherResponseLat: CurrentWeatherResponse? = null

    fun getCurrentWeatherLat(lat: Double, lon: Double) = viewModelScope.launch {

        currentWeatherLat.postValue(Resource.Loading())
        try {
            val response = RetrofitRepository().retrofitService.getCurrentWeather(lat, lon)
            currentWeatherLat.postValue(handleCurrentWeatherResponseLat(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> currentWeatherLat.postValue(Resource.Error("Network Failure"))
                else -> currentWeatherLat.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleCurrentWeatherResponseLat(response: Response<CurrentWeatherResponse>): Resource<CurrentWeatherResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                currentWeatherResponseLat = resultResponse
                return Resource.Success(currentWeatherResponseLat ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}
