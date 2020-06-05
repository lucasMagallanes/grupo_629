package com.example.helloandroidstudio.Utilidades

import android.app.Application

class IniciadorSharedPreferences: Application() {

    companion object {
        lateinit var gestorSP: GestorSharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        gestorSP = GestorSharedPreferences(applicationContext)
    }

}