package com.example.helloandroidstudio.ServicioAPI

import com.example.helloandroidstudio.Modelos.Evento
import com.example.helloandroidstudio.Modelos.ResponseEvento
import com.example.helloandroidstudio.Modelos.ResponseLoguin
import com.example.helloandroidstudio.Modelos.Usuario
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register/")
    fun registrarUsuario(@Body post: Usuario?): Call<ResponseLoguin>

    @POST("login/")
    fun loginUsuario(@Body post: Usuario?): Call<ResponseLoguin>

    @POST("event/")
    fun registrarEvento(@Header("token") header:String, @Body post: Evento?): Call<ResponseEvento>

}