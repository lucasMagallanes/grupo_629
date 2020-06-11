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
import android.widget.TextView
import android.widget.Toast
import com.example.helloandroidstudio.Modelos.Evento
import com.example.helloandroidstudio.Modelos.ResponseEvento
import com.example.helloandroidstudio.Modelos.ResponseLoguin
import com.example.helloandroidstudio.Modelos.Usuario
import com.example.helloandroidstudio.ServicioAPI.ObjetoRetrofit
import com.example.helloandroidstudio.Utilidades.*
import com.example.helloandroidstudio.Utilidades.IniciadorSharedPreferences.Companion.gestorSP
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {//, LoadingImplementation

    private val objetoRetrofit = ObjetoRetrofit("http://so-unlam.net.ar/api/api/")
    val TAG_LOGS = "Log personalizado"
    private lateinit var tvProceso:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        simpleProgressBar.setVisibility(View.INVISIBLE);
        tvProceso = findViewById(R.id.tvProceso)
        tvProceso.setVisibility(View.INVISIBLE)
        inicializarPrueba()
        //gestorSP.prefe1.edit().clear().commit()
        //preferences.edit().remove("text").commit();
        btnLoguearse.setOnClickListener {
            if (!verificarConectividad()) {
                Toast.makeText(
                    applicationContext,
                    "No hay internet, verifique la conexión de su dispositivo",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (validarCampos()) {
                val simpleProgressBar: ProgressBar = findViewById(R.id.simpleProgressBar)
                simpleProgressBar.setVisibility(View.VISIBLE)
                loguear()
            }
        }
        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }
    }
    private fun verificarConectividad(): Boolean {
        tvProceso.setVisibility(View.VISIBLE)
        tvProceso.text = "Verificando conectividad..."
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
        tvProceso.text = "Validando campos..."
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
        tvProceso.text = "Iniciando proceso de logueo..."
        objetoRetrofit.instanciarRetrofit()
        loguearUsuario(obtenerUsuario())
    }

    private fun obtenerUsuario():Usuario{
        tvProceso.text = "Obteniendo usuario..."
        val campoMail:EditText = findViewById(R.id.etLogEmail)
        val campoContrasenia:EditText= findViewById(R.id.etLogContrasenia)
        var usuario = Usuario(campoMail.text.toString(), campoContrasenia.text.toString())
        Log.i(TAG_LOGS, usuario.email + " " + usuario.password + " " + usuario.env)
        return usuario
    }

    fun loguearUsuario(usuario:Usuario) {
        tvProceso.text = "Logueando usuario..."
        objetoRetrofit.service.loginUsuario(usuario).enqueue(object: Callback<ResponseLoguin>{
            override fun onResponse(call: Call<ResponseLoguin>?, response: Response<ResponseLoguin>?) {
                Log.i(TAG_LOGS, "Response: " + Gson().toJson(response))
                if(response?.code() == 200){
                    val token:String = response!!.body()?.token.toString()
                    loguearEventoLogin(usuario.email, token)
                }else if(response?.code() == 400){
                    if (!response.isSuccessful) {
                        var jsonObject: JSONObject? = null
                        try {
                            jsonObject = JSONObject(response.errorBody()!!.string())
                            val userMessage = jsonObject.getString("msg")
                            //val internalMessage = jsonObject.getString("state")
                            Toast.makeText(applicationContext, userMessage , Toast.LENGTH_SHORT).show()
                            simpleProgressBar.setVisibility(View.INVISIBLE)
                            tvProceso = findViewById(R.id.tvProceso)
                            tvProceso.setVisibility(View.INVISIBLE)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }else{
                        Log.i(TAG_LOGS, "TEST: Entró al else")
                    }
                    //var mError = ErrorPojoClass()
                    //mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass::class.java)
                    //Toast.makeText(applicationContext, gson.fromJson(response.errorBody().string()) , Toast.LENGTH_SHORT).show()
                }else if(!response!!.isSuccessful()){
                    Utilidades.mostrarAlerta(this@MainActivity,"Error", "Hubo un error en la conexión con el servidor. errorBody: ${response.errorBody()}  headers: ${response.headers()} estado: ${response?.body()?.state} env: ${response?.body()?.env}: Codigo: " + response?.message() + ' ' + response?.code())
                    if(response.body() != null){
                        Utilidades.mostrarAlerta(this@MainActivity,"Error 2 ${response!!.body()?.msg}", "")
                    }
                }else {
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
        tvProceso.text = "Logueando evento de usuario..."
        objetoRetrofit.service.registrarEvento(token, Evento( "Login","El usuario con mail ${email}")).enqueue(object: Callback<ResponseEvento>{
            override fun onResponse(call: Call<ResponseEvento>?, response: Response<ResponseEvento>?) {
                Log.i(TAG_LOGS, Gson().toJson(response))
                if(!response!!.isSuccessful()){
                    Utilidades.mostrarAlerta(this@MainActivity,"Error", "Hubo un error en la conexión con el servidor. errorBody: ${response.errorBody()}  headers: ${response.headers()} estado: ${response?.body()?.state} env: ${response?.body()?.env}: Codigo: " + response?.message() + ' ' + response?.code())
                }else if(response?.code() == 201){
                    tvProceso.text = "Logueo de evento exitoso..."
                    //Toast.makeText(applicationContext, "Registro exitoso de evento" , Toast.LENGTH_SHORT).show()
                    gestorSP.guardarEvento("Login - $email")
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
        tvProceso.text = "Dirigiéndose al home..."
        val intent = Intent(this@MainActivity, Home::class.java)
        intent.putExtra("email", email)
        intent.putExtra("token", token)
        startActivity(intent)
    }

}