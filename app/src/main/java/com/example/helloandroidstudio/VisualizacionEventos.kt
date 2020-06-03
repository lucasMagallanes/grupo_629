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
import androidx.appcompat.app.AlertDialog
import com.example.helloandroidstudio.ServicioAPI.ObjetoRetrofit
import kotlinx.android.synthetic.main.activity_visualizacion_eventos.*

class VisualizacionEventos : AppCompatActivity(), AdapterView.OnItemSelectedListener  {//

    private val objetoRetrofit = ObjetoRetrofit("http://so-unlam.net.ar/api/api/event/")
    val TAG_LOGS = "Log personalizado"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizacion_eventos)
        val intentActual = intent
        val tvEmail: TextView = findViewById(R.id.tvEmail)
        tvEmail.text = intentActual.getStringExtra("email")
        //llenarComboEventos()
        llenarComboSensores()
        btnVisIrAHome.setOnClickListener{
            val intent = Intent(this, Home::class.java)
            intent.putExtra("email", intentActual.getStringExtra("email"))
            intent.putExtra("token", intentActual.getStringExtra("token"))
            startActivity(intent)
        }
        btnEscucharEventos.setOnClickListener{
            escucharEventoProximidad()
        }
    }

   // var sensor: Sensor = null;
//    var sensorManager:SensorManager = null;
//    var sensorEventListener:SensorEventListener = null;

    private fun escucharEventoProximidad(){
//        var sensor: Sensor
//        var sensorManager:SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        //var sensorEventListener: SensorEventListener
//
//        val proximitySensor:Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) //final
//
//        var proximitySensorListener = SensorEventListener(){
//
//            @Override
//            fun onSensorChanged(sensorEvent: SensorEvent){
//                if(sensorEvent.values[0] < proximitySensor.maximumRange()){
//                    getWindow().getDecorView().setBackgroundColor(Color.RED)
//                }else{
//                    getWindow().getDecorView().setBackgroundColor(Color.BLUE)
//                }
//            }
//
//            @Override
//            fun onAccuracyChanged(sensor:Sensor, i:Int){
//            }
//
//        }
//        sensorManager.registerListener(proximitySensorListener, proximitySensor, 2*1000*1000)
    }

    private lateinit var mSensorManager: SensorManager
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

    private fun mostrarMensajeDeError(mensaje:String){
        Toast.makeText(applicationContext, mensaje , Toast.LENGTH_SHORT).show()
    }

    private fun mostrarAlerta(titulo:String, subTitulo:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(subTitulo)
        builder.setPositiveButton("Salir"){dialog, which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}