package com.example.helloandroidstudio

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.helloandroidstudio.Modelos.ResponseLoguin
import com.example.helloandroidstudio.Modelos.Usuario
import com.example.helloandroidstudio.ServicioAPI.ObjetoRetrofit
import com.example.helloandroidstudio.Utilidades.IniciadorSharedPreferences.Companion.gestorSP
import com.example.helloandroidstudio.Utilidades.Utilidades
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.btnRegistrarse
import kotlinx.android.synthetic.main.activity_registro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro : AppCompatActivity() {

    val TAG_LOGS = "Log personalizado"
    private val objetoRetrofit = ObjetoRetrofit("http://so-unlam.net.ar/api/api/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        btnRegistrarse.setOnClickListener {
            if(!verificarConectividad()){
                Toast.makeText(applicationContext, "No hay internet, verifique la conexión de su dispositivo" , Toast.LENGTH_SHORT).show()
            }else if (validar()) {
                registrarUsuario()
            }
        }
        btnRegVolver.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verificarConectividad(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected
    }

    private fun validar() : Boolean{
        var validacion:Boolean = true
        val campoEmail:EditText = findViewById(R.id.etEmail)
        val campoContrania:EditText = findViewById(R.id.etContrasenia)
        val campoNombre:EditText = findViewById(R.id.etNombre)
        val campoApellido:EditText = findViewById(R.id.etApellido)
        val campoDNI:EditText = findViewById(R.id.etDNI)

        if(!Utilidades.esMailValido(campoEmail.text.toString())){//.isNullOrEmpty()) {
            campoEmail.setError("Error de formato en el campo \"email\"")
            validacion = false
        }
        if(campoContrania.text.isNullOrEmpty()) {
            campoContrania.setError("Falta completar el campo \"contraseña\"")
            validacion =  false
        }else if(campoContrania.text.length < 8) {
            campoContrania.setError("El campo \"contraseña\" debe tener como mínimo 8 caracteres")
            validacion =  false
        }
        if(campoNombre.text.isNullOrEmpty()) {
            campoNombre.setError("Falta completar el campo \"Nombre\"")
            validacion =  false
        }
        if(campoApellido.text.isNullOrEmpty()) {
            campoApellido.setError("Falta completar el campo \"Apellido\"")
            validacion =  false
        }
        if(campoDNI.text.isNullOrEmpty()) {
            campoDNI.setError("Falta completar el campo \"DNI\"")
            validacion =  false
        }

        return validacion
    }

    private fun registrarUsuario(){
        objetoRetrofit.instanciarRetrofit()
        ejecutarRequest(obtenerUsuario())
    }

    private fun obtenerUsuario() : Usuario {
        val campoEmail:EditText = findViewById(R.id.etEmail)
        val campoContrania:EditText = findViewById(R.id.etContrasenia)
        val campoNombre:EditText = findViewById(R.id.etNombre)
        val campoApellido:EditText = findViewById(R.id.etApellido)
        val campoDNI:EditText = findViewById(R.id.etDNI)
        var usuario: Usuario? = Usuario()

        usuario!!.env = "DEV" //Cambiar a DEV cuando se quiera registrar de verdad.
        usuario!!.name = campoNombre.getText().toString()
        usuario!!.lastname = campoApellido.getText().toString()
        usuario!!.dni = campoDNI.getText().toString().toInt()
        usuario!!.email = campoEmail.getText().toString()
        usuario!!.password = campoContrania.getText().toString()
        usuario!!.commission = 2900
        usuario!!.group = 629
        return usuario
    }

     private fun  ejecutarRequest(usuario:Usuario){
        objetoRetrofit.service.registrarUsuario(usuario).enqueue(object: Callback<ResponseLoguin> {
            override fun onResponse(call: Call<ResponseLoguin>?, response: Response<ResponseLoguin>?) {
                Log.i(TAG_LOGS, Gson().toJson(response))
                if(!response!!.isSuccessful()){
                    mostrarMensajeDeError("Hubo un error en la conexión con el servidor. env: ${response.body()!!.env} token: ${response.body()?.token} Codigo: " +response?.message() + ' ' + response?.code())
                }else if(response?.code() == 201){
                    gestorSP.guardarEvento("Registro - ${usuario.email}")
                    mostrarAlerta("Registro exitoso", "Ir a Home", usuario.email, response!!.body()?.token.toString())
                }else{
                    mostrarMensajeDeError("Hubo algún tipo de falla en la registración de usuario. Error: ${response.body()!!.msg}")
                }
            }
            override fun onFailure(call: Call<ResponseLoguin>?, t: Throwable?) {
                mostrarMensajeDeError("Entró a onFailure de registrar usuario")
                t?.printStackTrace()
            }
        })
    }

    private fun mostrarMensajeDeError(mensaje:String){
        Toast.makeText(applicationContext, mensaje , Toast.LENGTH_SHORT).show()
    }

    private fun mostrarAlerta(titulo:String, subTitulo:String, mail:String, token:String){
        val builder = AlertDialog.Builder(this)

        builder.setTitle(titulo)
        builder.setPositiveButton(subTitulo){dialog, which ->
            val intent = Intent(this, Home::class.java)
            intent.putExtra("email", mail)
            intent.putExtra("token", token)
            startActivity(intent)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}