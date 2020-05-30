package com.example.helloandroidstudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_contacto.*
import kotlinx.android.synthetic.main.activity_home.*

class Contacto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacto)

        btnConIrAHome.setOnClickListener{
            //Hacer las validaciones acá
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}
