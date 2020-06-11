package com.example.helloandroidstudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_home.*
import android.widget.Toast
import android.view.KeyEvent.KEYCODE_BACK
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.KeyEvent


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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() === 0) {
            Toast.makeText(
                applicationContext, "La sesión ha finalizado",
                Toast.LENGTH_SHORT
            ).show()
            val intentActual = intent
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", intentActual.getStringExtra("email"))
            startActivity(intent)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }
}
