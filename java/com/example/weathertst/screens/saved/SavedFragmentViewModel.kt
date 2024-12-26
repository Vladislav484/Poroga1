package com.example.weathertst.screens.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.geocoding.LocationResponseItem
import com.example.weathertst.database.WeatherDatabase
import com.example.weathertst.model.geocoding.LocationResponse
import com.example.weathertst.repositroty.WeatherRepository
import com.example.weathertst.utils.Resource
import kotlinx.coroutines.launch

class SavedFragmentViewModel(
    app: Application
): AndroidViewModel(app) {

    var selectedLocation: MutableLiveData<LocationResponseItem>? = null
    private val weatherRepository = WeatherRepository(WeatherDatabase(app))
    var location: MutableLiveData<Resource<LocationResponse>> = MutableLiveData()

    fun deleteLocation(location: LocationResponseItem) = viewModelScope.launch {
        weatherRepository.deleteLocation(location)
    }

    fun getLocationsLive()  = weatherRepository.getLocationsLive()

}