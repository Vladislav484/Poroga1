package com.example.weathertst.screens.week

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathertst.adapters.WeeksWeatherAdapters
import com.example.weathertst.databinding.FragmentWeekBinding
import com.example.weathertst.repositroty.WeatherMvvmRepo
import com.example.weathertst.utils.Resource
import kotlinx.android.synthetic.main.fragment_week.*

class WeekFragment : Fragment() {

    private var _binding: FragmentWeekBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: WeekFragmentViewModel
    lateinit var adapterWeek :WeeksWeatherAdapters
    private val repositoryWeather = WeatherMvvmRepo()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeekBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initialization(){
        mViewModel = ViewModelProvider(this)[WeekFragmentViewModel::class.java]

        repositoryWeather.getLon()?.let { mViewModel.getWeeksWeather(repositoryWeather.getLat()!!, it) }

        mViewModel.weeksWeather.observe(viewLifecycleOwner, Observer { it ->
            when(it) {
                is Resource.Success -> {
                    it.data?.let {
                        adapterWeek = context?.let { it1 -> WeeksWeatherAdapters(it.list, it1) }!!
                        week_weathers_recycler.adapter = adapterWeek
                        week_weathers_recycler.layoutManager = LinearLayoutManager(context)
                    }
                }
                is Resource.Error -> {
                    it.message?.let { message ->
                        Toast.makeText(activity, "Произошла ошибка: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                }
            }
        } )
    }
}