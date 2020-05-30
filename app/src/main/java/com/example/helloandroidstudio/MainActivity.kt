package com.example.helloandroidstudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLoguearse.setOnClickListener{
            if(validarCampos()){
                loguear()
            }
        }
        btnRegistrarse.setOnClickListener{
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }
    }

    private fun validarCampos() : Boolean{
        var validacion:Boolean

        val campoEmail:EditText = findViewById(R.id.etLogEmail)
        val campoContrasenia: EditText = findViewById(R.id.etLogContrasenia)

        if(campoEmail.text.isNullOrEmpty()) {
            campoEmail.setError("Falta completar el campo \"email\"")
            validacion = false
        }else if(campoContrasenia.text.isNullOrEmpty()) {
            campoContrasenia.setError("Falta completar el campo \"contraseña\"")
            validacion =  false
        }else{
            validacion =  true
        }
        return validacion
    }

    private fun loguear() {
        if(validarLogueo()){
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }

    private fun validarLogueo():Boolean {
        return true
    }
}