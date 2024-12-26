package com.example.weathertst.screens.search

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathertst.R
import com.example.weathertst.adapters.SearchAdapter
import com.example.weathertst.databinding.FragmentSearchBinding
import com.example.weathertst.repositroty.WeatherMvvmRepo
import com.example.weathertst.utils.Resource
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val mBinding get() = _binding!!
    lateinit var searchAdapter: SearchAdapter
    private lateinit var mViewModel: SearchFragmentViewModel
    private val repositoryWeather = WeatherMvvmRepo()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this)[SearchFragmentViewModel::class.java]
        setupRecyclerView()
        focusOnEditText()
        mViewModel.location = MutableLiveData()

        searchAdapter.setOnItemClickListener { location ->
            if(location.addButtonStatus == null){ location.addButtonStatus = false }
            if (location.addButtonStatus == false) {
                mViewModel.saveLocation(location)
                location.addButtonStatus = true
            } else if (location.addButtonStatus == true) {
                mViewModel.deleteLocation(location)
                location.addButtonStatus = false
            }
            mViewModel.selectedLocation = MutableLiveData(location)
            repositoryWeather.setSelectedLocation(mViewModel.selectedLocation)

        }

        var job: Job? = null
        search_locations.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        mViewModel.getLocationUsingCity(editable.toString())
                    }
                }
            }
        }

        back_to_saved_locations.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_savedFragment)
        }

        mViewModel.location.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { locationsResponse ->
                        searchAdapter.differ.submitList(locationsResponse.toList())
                        Log.e("search ad",searchAdapter.differ.currentList.toString())
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(activity, "Произошла ошибка: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                }
            }
        } )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater,container,false)
        return mBinding.root

    }
    private fun focusOnEditText() {
        search_locations.isFocusableInTouchMode = true
        search_locations.isFocusable = true
        search_locations.requestFocus()
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(search_locations, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter()
        rv_search_locations.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}