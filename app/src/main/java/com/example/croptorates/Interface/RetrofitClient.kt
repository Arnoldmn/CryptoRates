package com.example.croptorates.Interface

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private  var ourInstance: Retrofit? = null

    val instace:Retrofit
    get() {
        if (ourInstance == null){
            ourInstance = Retrofit.Builder()
                .baseUrl("https://api.coinmarketcap.com/v1/ticker/?start=0&limit=10")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        }
        return  ourInstance!!
    }


}