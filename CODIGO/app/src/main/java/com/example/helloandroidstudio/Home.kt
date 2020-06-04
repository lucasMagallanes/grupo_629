package com.example.helloandroidstudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    private var token:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val intentActual = intent
        val tvEmail: TextView = findViewById(R.id.tvEmail)
        tvEmail.text = intentActual.getStringExtra("email")
        token= intentActual.getStringExtra("token")

        btnIraLogEventos.setOnClickListener{
            val intent = Intent(this, VisualizacionEventos::class.java)
            intent.putExtra("email", intentActual.getStringExtra("email"))
            intent.putExtra("token", intentActual.getStringExtra("token"))
            startActivity(intent)
        }
        btnHomIrAContacto.setOnClickListener{
            val intent = Intent(this, Contacto::class.java)
            intent.putExtra("email", intentActual.getStringExtra("email"))
            intent.putExtra("token", intentActual.getStringExtra("token"))
            startActivity(intent)
        }
    }
}
