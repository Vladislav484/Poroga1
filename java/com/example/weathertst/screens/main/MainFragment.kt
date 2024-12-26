package com.example.weathertst.screens.main

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weathertst.MainActivity
import com.example.weathertst.R
import com.example.weathertst.databinding.FragmentMainBinding
import com.example.weathertst.repositroty.WeatherMvvmRepo
import com.example.weathertst.utils.APP_ACTIVITY
import com.example.weathertst.utils.PACKAGE_NAME
import com.example.weathertst.utils.Resource
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainFragment : Fragment() {

    private lateinit var mViewModel: MainFragmentViewModel
    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val repositoryWeather = WeatherMvvmRepo()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewModel = ViewModelProvider(this)[MainFragmentViewModel::class.java]
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        initialization()
        btn_go_saved.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_savedFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initialization() {
        repositoryWeather.getSelectedLocation().toString()
        mViewModel.selectedLocation =
            repositoryWeather.getSelectedLocation() // выбранная локация которую берем из search
        mViewModel.selectedLocation?.observe(viewLifecycleOwner, Observer { location ->
            var generatedLocation = ""
            if (location.local_names?.ru != null) {
                generatedLocation += location.local_names.ru
            } else if (location.local_names?.en != null) {
                generatedLocation += location.local_names.en
            } else if (location.name != null) {
                generatedLocation += location.name
            }
            if (location.country != null) {
                generatedLocation += ", " + location.country
            }
            nameCity.text = generatedLocation
            repositoryWeather.setLat(location.lat)
            repositoryWeather.setLon(location.lon)
            mViewModel.getCurrentWeatherLat(location.lat, location.lon)

        })

        mViewModel.location.observe(viewLifecycleOwner, Observer { it ->
            when (it) {
                is Resource.Success -> {
                    it.data?.let {
                        mViewModel.getCurrentWeatherLat(it[0].lat, it[0].lon)
                    }
                }
                is Resource.Error -> {
                    it.message?.let { message ->
                        Toast.makeText(activity, "Произошла ошибка: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {
                }
            }
        })

        mViewModel.currentWeatherLat.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Resource.Success -> {
                    it.data?.let {
                        Log.e("cur", "true")
                        weatherNow.visibility = View.VISIBLE
                        btn_weather_week.visibility = View.VISIBLE
                        val mDrawableName = "ic_" + it.weather[0].icon
                        val resID = resources.getIdentifier(mDrawableName, "drawable", PACKAGE_NAME)
                        mBinding.weatherImage.setImageResource(resID)
                        mBinding.weatherDegrees.text =
                            it.main.let { it1 -> Math.round(it1.temp) }.toString() + "°С"
                        mBinding.nameCity.text = it.name
                        mBinding.weatherCondition.text = it.weather[0].description
                        mBinding.feeling.text =
                            "Ощущается как: " + it.main.let { it1 -> Math.round(it1.feels_like) }
                                .toString() + "°С"
                        mBinding.pressure.text = "Давление: " + it.main.pressure.toString() + " гПа"
                        mBinding.humidity.text = "Влажность: " + it.main.humidity.toString() + " %"
                        mBinding.windSpeed.text =
                            "Скорость ветра: " + it.wind.speed.toString() + " м/c"


                    }
                }
                is Resource.Error -> {
                    it.message?.let { message ->
                        Toast.makeText(activity, "Произошла ошибка: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {
                }
            }
        })
    }

    private fun onButtonWeekWeatherClick() {

        mViewModel.currentWeather.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let {
                    }
                }
                is Resource.Error -> {
                    it.message?.let { message ->
                        Toast.makeText(
                            activity,
                            "Произошла ошибка отправки: $message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Resource.Loading -> {
                }
            }
        })
        APP_ACTIVITY.navController.navigate(R.id.action_mainFragment_to_dopFragment)
    }

    fun userLocationInfo() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity as MainActivity)
        isLocationPermissionGranted()

    }

    private fun setupListeners() {
        btn_my_location.setOnClickListener { userLocationInfo() }
        btn_more.setOnClickListener { onButtonWeekWeatherClick() }
    }

    private fun isLocationPermissionGranted() {
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                activity as MainActivity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity as MainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
        } else {
            task.addOnSuccessListener {
                if (it != null) {
                    repositoryWeather.setSelectedLocation(null)
                    repositoryWeather.setLat(it.latitude)
                    repositoryWeather.setLon(it.longitude)
                    mViewModel.getCurrentWeatherLat(it.latitude, it.longitude)
                }
            }
        }
    }
}



