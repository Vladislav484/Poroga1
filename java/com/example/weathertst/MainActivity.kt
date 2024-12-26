package com.example.weathertst

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weatherapp.models.geocoding.LocationResponseItem
import com.example.weathertst.databinding.ActivityMainBinding
import com.example.weathertst.screens.main.MainFragmentViewModel
import com.example.weathertst.utils.APP_ACTIVITY
import java.io.FileInputStream
import java.io.IOException
import java.io.ObjectInputStream

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: MainFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {


        mViewModel = ViewModelProvider(this)[MainFragmentViewModel::class.java]


        val locationFile = readFromFile(baseContext)
        if (locationFile != null) {
            Log.e("file","not null")
            mViewModel.selectedLocation = MutableLiveData(locationFile)
            Log.e("file", mViewModel.selectedLocation!!.value.toString())
        }else{
            Log.e("file","null")
        }

        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun readFromFile(context: Context): LocationResponseItem? {
        var location: LocationResponseItem? = null

        try {

            val fileInputStream: FileInputStream = context.openFileInput("fileName")
            val objectInputStream = ObjectInputStream(fileInputStream)

            location = objectInputStream.readObject() as LocationResponseItem
            objectInputStream.close()
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return location
    }
}