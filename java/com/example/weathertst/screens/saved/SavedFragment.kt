package com.example.weathertst.screens.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapters.SavedAdapter
import com.example.weathertst.R
import com.example.weathertst.databinding.FragmentSavedBinding
import com.example.weathertst.repositroty.WeatherMvvmRepo
import kotlinx.android.synthetic.main.fragment_saved.*

class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val mBinding get() = _binding!!
    lateinit var adapterList: SavedAdapter
    private lateinit var mViewModel: SavedFragmentViewModel
    private val repositoryWeather = WeatherMvvmRepo()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedBinding.inflate(layoutInflater, container, false)
        mViewModel = ViewModelProvider(this)[SavedFragmentViewModel::class.java]

        mViewModel.getLocationsLive().observe(viewLifecycleOwner, Observer {
            adapterList = SavedAdapter(it)
            mBinding.rvSavedLocations.adapter = adapterList
            mBinding.rvSavedLocations.layoutManager = LinearLayoutManager(context)
            adapterList.setOnItemClickListener { location ->
                mViewModel.selectedLocation = MutableLiveData(location)
                repositoryWeather.setSelectedLocation(mViewModel.selectedLocation)
                findNavController().navigate(R.id.action_savedFragment_to_mainFragment)
            }
            adapterList.setOnItemClickListenerDelete { location ->  mViewModel.deleteLocation(location) }
        })
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        go_search_locations_in_search_fragment.setOnClickListener {
            findNavController().navigate(R.id.action_savedFragment_to_searchFragment)
        }
        back_to_current_weather.setOnClickListener {
            findNavController().navigate(R.id.action_savedFragment_to_mainFragment)
        }
    }
}