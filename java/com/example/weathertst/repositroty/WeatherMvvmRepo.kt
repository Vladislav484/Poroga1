package com.example.weathertst.repositroty

import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.models.geocoding.LocationResponseItem
import com.example.weathertst.model.geocoding.LocationResponse
import com.example.weathertst.utils.Resource

class WeatherMvvmRepo {

    fun getLocation():MutableLiveData<Resource<LocationResponse>>{
        return locations
    }
    fun setLocation(location: MutableLiveData<Resource<LocationResponse>>){
        locations = location
    }

    fun getSelectedLocation():MutableLiveData<LocationResponseItem>?{
        return selectedLocation
    }
    fun setSelectedLocation(selectlocation: MutableLiveData<LocationResponseItem>?){
        selectedLocation = selectlocation
    }

    fun getLon():Double?{
        return lon
    }
    fun setLon(lonWeek: Double){
        lon = lonWeek
    }
    fun getLat():Double?{
        return lat
    }
    fun setLat(latWeek: Double){
        lat = latWeek
    }

    companion object{
        var locations: MutableLiveData<Resource<LocationResponse>> = MutableLiveData()
        var selectedLocation: MutableLiveData<LocationResponseItem>? = null
        var lat: Double? = null
        var lon: Double? = null
    }
}