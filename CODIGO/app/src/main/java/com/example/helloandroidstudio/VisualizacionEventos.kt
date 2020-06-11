package com.example.helloandroidstudio

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.helloandroidstudio.ServicioAPI.ObjetoRetrofit
import com.example.helloandroidstudio.Utilidades.IniciadorSharedPreferences.Companion.gestorSP
import kotlinx.android.synthetic.main.activity_visualizacion_eventos.*

class VisualizacionEventos : AppCompatActivity(), AdapterView.OnItemSelectedListener  {//

    val TAG_LOGS = "Log personalizado"
    private lateinit var mSensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizacion_eventos)
        val intentActual = intent
        val tvEmail: TextView = findViewById(R.id.tvEmail)
        tvEmail.text = intentActual.getStringExtra("email")
        llenarComboSensores()

        btnVisIrAHome.setOnClickListener{
            val intent = Intent(this, Home::class.java)
            intent.putExtra("email", intentActual.getStringExtra("email"))
            intent.putExtra("token", intentActual.getStringExtra("token"))
            startActivity(intent)
        }

        btnIrARegistroEventos.setOnClickListener{
            val intent = Intent(this, RealizarEventos::class.java)
            intent.putExtra("email", intentActual.getStringExtra("email"))
            intent.putExtra("token", intentActual.getStringExtra("token"))
            startActivity(intent)
        }
        listarEventos()
    }

    private fun listarEventos(){
        val lvEventos:ListView = findViewById(R.id.lvEventos)
        val tvExisteEventos:TextView = findViewById(R.id.tvExisteEventos)
        val lista:List<String> = gestorSP.obtenerListaEventos()

        if(lista.count() > 0){
            lvEventos.setVisibility(View.VISIBLE)
            tvExisteEventos.setVisibility(View.INVISIBLE)
            val aa= ArrayAdapter(this, android.R.layout.simple_list_item_1, lista)
            lvEventos!!.setAdapter(aa)
        }else{
            lvEventos.setVisibility(View.INVISIBLE)
            tvExisteEventos.setVisibility(View.VISIBLE)
        }
    }

    private fun llenarComboSensores(){
        spnTipoEvento!!.setOnItemSelectedListener(this)
        val spnprueba:Spinner = findViewById(R.id.spnTipoEvento)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val deviceSensors: List<Sensor> = mSensorManager.getSensorList(Sensor.TYPE_ALL)
        Log.v("Sensores totales",""+deviceSensors.size)
        val listaSensores = arrayListOf<String>()
        deviceSensors.forEach{
            listaSensores.add(it.toString())
        }
        val aa= ArrayAdapter(this, android.R.layout.simple_spinner_item, listaSensores);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnTipoEvento!!.setAdapter(aa)
    }

    //Los 2 métodos de abajo están para que funcione bien el adaptador
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        //textView_msg!!.text = "Selected : "+languages[position]
    }
    override fun onNothingSelected(arg0: AdapterView<*>) {
    }

}