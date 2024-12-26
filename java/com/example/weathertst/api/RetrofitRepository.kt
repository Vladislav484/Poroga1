package com.example.weathertst.api

import com.example.weathertst.utils.BaseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitRepository {

    private fun retrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val retrofitService: ApiRequest by lazy {
        retrofit().create(ApiRequest::class.java)
    }


}