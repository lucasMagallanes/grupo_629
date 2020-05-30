package com.example.helloandroidstudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_visualizacion_eventos.*

class VisualizacionEventos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizacion_eventos)

        btnVisIrAHome.setOnClickListener{
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}
