package com.example.helloandroidstudio

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.helloandroidstudio.Modelos.Evento
import com.example.helloandroidstudio.Modelos.ResponseEvento
import com.example.helloandroidstudio.Modelos.ResponseLoguin
import com.example.helloandroidstudio.Modelos.Usuario
import com.example.helloandroidstudio.ServicioAPI.ObjetoRetrofit
import com.example.helloandroidstudio.Utilidades.LoadingAnimation
import com.example.helloandroidstudio.Utilidades.LoadingAsync
import com.example.helloandroidstudio.Utilidades.LoadingImplementation
import com.example.helloandroidstudio.Utilidades.Utilidades
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {//, LoadingImplementation

    //private lateinit var loadingAnimation : LoadingAnimation
    private val objetoRetrofit = ObjetoRetrofit("http://so-unlam.net.ar/api/api/")
    val TAG_LOGS = "Log personalizado"

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        simpleProgressBar.setVisibility(View.INVISIBLE);
        inicializarPrueba()
        btnLoguearse.setOnClickListener{
            if(! verificarConectividad()){
                Toast.makeText(applicationContext, "No hay internet, verifique la conexión de su dispositivo" , Toast.LENGTH_SHORT).show()
            }else if(validarCampos()){
                val simpleProgressBar:ProgressBar = findViewById(R.id.simpleProgressBar)
                simpleProgressBar.setVisibility(View.VISIBLE)
                loguear()
            }
        }
        btnRegistrarse.setOnClickListener{
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        //Cargar loader
//        loadingAnimation = LoadingAnimation(this, "./assets/loading.json")//"loading.json")//./loading.json")
//        loadingAnimation.playAnimation(true)
//        LoadingAsync(this).execute()
    }

//    override fun onFinishedLoading() {
//        //# After loading is done, stop the animation and reset the current view
//        loadingAnimation.stopAnimation(R.layout.activity_main)
//    }

    private fun verificarConectividad(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected
    }

    private fun inicializarPrueba(){
        val campoMail:EditText = findViewById(R.id.etLogEmail)
        val campoContrasenia:EditText= findViewById(R.id.etLogContrasenia)
        campoMail.setText("lmagallanes@alumno.unlam.edu.ar")
        campoContrasenia.setText("12345678")
    }

    private fun validarCampos() : Boolean{
        var validacion:Boolean =  true

        val campoEmail:EditText = findViewById(R.id.etLogEmail)
        val campoContrasenia: EditText = findViewById(R.id.etLogContrasenia)

        if(! Utilidades.esMailValido(campoEmail.text.toString())){
            campoEmail.setError("Error de formato en el campo \"email\"")
            validacion = false
        }
         if(campoContrasenia.text.isNullOrEmpty()) {
            campoContrasenia.setError("Falta completar el campo \"contraseña\"")
            validacion =  false
        }
        return validacion
    }

    private fun loguear() {
        objetoRetrofit.instanciarRetrofit()
        loguearUsuario(obtenerUsuario())
    }

    private fun obtenerUsuario():Usuario{
        val campoMail:EditText = findViewById(R.id.etLogEmail)
        val campoContrasenia:EditText= findViewById(R.id.etLogContrasenia)
        var usuario = Usuario(campoMail.text.toString(), campoContrasenia.text.toString())
        Log.i(TAG_LOGS, usuario.email + " " + usuario.password + " " + usuario.env)
        return usuario
    }

    fun loguearUsuario(usuario:Usuario) {
        objetoRetrofit.service.loginUsuario(usuario).enqueue(object: Callback<ResponseLoguin>{
            override fun onResponse(call: Call<ResponseLoguin>?, response: Response<ResponseLoguin>?) {
                Log.i(TAG_LOGS, Gson().toJson(response))
                if(!response!!.isSuccessful()){
                    Utilidades.mostrarAlerta(this@MainActivity,"Error", "Hubo un error en la conexión con el servidor. errorBody: ${response.errorBody()}  headers: ${response.headers()} estado: ${response?.body()?.state} env: ${response?.body()?.env}: Codigo: " + response?.message() + ' ' + response?.code())
                    if(response.body() != null){
                        Utilidades.mostrarAlerta(this@MainActivity,"Error 2 ${response!!.body()?.msg}", "")
                    }
                }else if(response?.code() == 200){
                    val token:String = response!!.body()?.token.toString()
                    loguearEventoLogin(usuario.email, token)
                }else{
                    Utilidades.mostrarAlerta(this@MainActivity,"Error","Hubo algún tipo de falla en el logueo.  estado: ${response?.body()?.state} env: ${response?.body()?.env} Codigo: ${response?.code()} + response?.message() +Error: ${response.body()!!.msg}")
                }
            }
            override fun onFailure(call: Call<ResponseLoguin>?, t: Throwable?) {
                Utilidades.mostrarAlerta(this@MainActivity,"Entró a onFailure de registrar usuario", "")
                t?.printStackTrace()
            }
        })
    }

    private fun loguearEventoLogin(email:String, token:String){
        objetoRetrofit.service.registrarEvento(token, Evento( "Login","El usuario con mail ${email}")).enqueue(object: Callback<ResponseEvento>{
            override fun onResponse(call: Call<ResponseEvento>?, response: Response<ResponseEvento>?) {
                Log.i(TAG_LOGS, Gson().toJson(response))
                if(!response!!.isSuccessful()){
                    Utilidades.mostrarAlerta(this@MainActivity,"Error", "Hubo un error en la conexión con el servidor. errorBody: ${response.errorBody()}  headers: ${response.headers()} estado: ${response?.body()?.state} env: ${response?.body()?.env}: Codigo: " + response?.message() + ' ' + response?.code())
                }else if(response?.code() == 201){
                    //Toast.makeText(applicationContext, "Registro exitoso de evento" , Toast.LENGTH_SHORT).show()
                    redirigirActivity(email, token)

                }else{
                    Utilidades.mostrarAlerta( this@MainActivity,"Error","Hubo algún tipo de falla en la registración del evento.  estado: ${response?.body()?.state} env: ${response?.body()?.env} Codigo: ${response?.code()} + response?.message() +Error: ${response.body()!!.msg}")
                }
            }
            override fun onFailure(call: Call<ResponseEvento>?, t: Throwable?) {
                Utilidades.mostrarAlerta(this@MainActivity,"Entró a onFailure de registrar evento - ${t.toString()}", "")
                t?.printStackTrace()
            }
        })
    }

    private fun redirigirActivity(email:String, token:String){
        val intent = Intent(this@MainActivity, Home::class.java)
        intent.putExtra("email", email)
        intent.putExtra("token", token)
        startActivity(intent)
    }

//    val loadingAnimation = LoadingAnimation(this, "loading.json")
//    loadingAnimation.playAnimation(true)
}