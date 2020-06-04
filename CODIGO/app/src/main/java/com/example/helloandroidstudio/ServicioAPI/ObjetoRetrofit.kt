package com.example.helloandroidstudio.ServicioAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ObjetoRetrofit(ruta:String) {

    lateinit var service: ApiService
    private var ruta = ruta

    fun instanciarRetrofit(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ruta) //"http://so-unlam.net.ar/api/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<ApiService>(ApiService::class.java)
    }

}