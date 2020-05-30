package com.example.helloandroidstudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.btnRegistrarse
import kotlinx.android.synthetic.main.activity_registro.*

class Registro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        btnRegistrarse.setOnClickListener {
            if (validar()) {
                registrarUsuario()
            }
        }
        btnRegVolver.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validar() : Boolean{
        var validacion:Boolean

        val campoEmail:EditText = findViewById(R.id.etEmail)
        val campoContrania:EditText = findViewById(R.id.etContrasenia)
        val campoNombre:EditText = findViewById(R.id.etNombre)
        val campoApellido:EditText = findViewById(R.id.etApellido)
        val campoUsuario:EditText = findViewById(R.id.etUsuario)

        if(campoEmail.text.isNullOrEmpty()) {
            campoEmail.setError("Falta completar el campo \"email\"")
            validacion = false
        }else if(campoContrania.text.isNullOrEmpty()) {
            campoContrania.setError("Falta completar el campo \"contraseña\"")
            validacion =  false
        }else if(campoNombre.text.isNullOrEmpty()) {
            campoNombre.setError("Falta completar el campo \"Nombre\"")
            validacion =  false
        }else if(campoApellido.text.isNullOrEmpty()) {
            campoApellido.setError("Falta completar el campo \"Apellido\"")
            validacion =  false
        }else if(campoUsuario.text.isNullOrEmpty()) {
            campoUsuario.setError("Falta completar el campo \"Usuario\"")
            validacion =  false
        }else{
            validacion =  true
        }
        return validacion
    }

    private fun registrarUsuario(){
        Toast.makeText(this,  "El usuario fue registrado con éxito", Toast.LENGTH_SHORT)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}