package com.example.helloandroidstudio

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.example.helloandroidstudio.Utilidades.IniciadorSharedPreferences.Companion.gestorSP
import kotlinx.android.synthetic.main.activity_realizar_eventos.*

class RealizarEventos : AppCompatActivity() {

    private lateinit var  sensorManager:SensorManager
    private var ladoActivado:Lado = Lado.ninguno
    private enum class Lado{
        ninguno,
        verde,
        amarillo,
        azul,
        rojo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realizar_eventos)

        val intentActual = intent
        val tvEmail: TextView = findViewById(R.id.tvEmail)
        val tvActivarSensorProximidad: TextView = findViewById(R.id.tvActivarSensorProximidad)
        val tvActivarSensorGiroscopio: TextView = findViewById(R.id.tvActivarSensorGiroscopio)
        tvActivarSensorProximidad.setVisibility(View.INVISIBLE)
        tvActivarSensorGiroscopio.setVisibility(View.INVISIBLE)
        tvEmail.text = intentActual.getStringExtra("email")

        btnVisIrAGestionEventos.setOnClickListener {
            val intent = Intent(this, VisualizacionEventos::class.java)
            intent.putExtra("email", intentActual.getStringExtra("email"))
            intent.putExtra("token", intentActual.getStringExtra("token"))
            startActivity(intent)
        }
        btnEscucharEventos.setOnClickListener{
            escucharEventoProximidad()
            tvActivarSensorProximidad.setVisibility(View.VISIBLE)
        }
        btnActivarGiroscopio.setOnClickListener{
            escucharEventoGiroscopio()
            tvActivarSensorGiroscopio.setVisibility(View.VISIBLE)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() === 0) {
            val intentActual = intent
            val intent = Intent(this, VisualizacionEventos::class.java)
            intent.putExtra("email", intentActual.getStringExtra("email"))
            intent.putExtra("token", intentActual.getStringExtra("token"))
            startActivity(intent)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun escucharEventoProximidad(){
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val proximitySensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        val proximitySensorListener = object: SensorEventListener {

            override fun onSensorChanged(sensorEvent: SensorEvent){
                if(sensorEvent.values[0] < proximitySensor.getMaximumRange()){
                    getWindow().getDecorView().setBackgroundColor(Color.RED)
                    gestorSP.guardarEvento("Sensor proximidad - Se activo el background rojo")
                    //RegistrarEvento al igual que se hizo como cuando se loguea
                }else{
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE)
                    gestorSP.guardarEvento("Sensor proximidad - Se activo el background azul")
                    //RegistrarEvento al igual que se hizo como cuando se loguea
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, i:Int){
            }

        }
        sensorManager.registerListener(proximitySensorListener, proximitySensor, 2*1000*1000)
    }

    private fun escucharEventoGiroscopio(){
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val gyroscopeSensor : Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        val gyroscopeSensorListener = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent) {
                if(sensorEvent.values[2] > 0.5f) { // anticlockwise
                    if(ladoActivado != Lado.verde){
                        getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                        gestorSP.guardarEvento("Sensor giroscopio - Se activo el background verde")
                        //RegistrarEvento al igual que se hizo como cuando se loguea
                        ladoActivado = Lado.verde
                    }
                } else if(sensorEvent.values[2] < -0.5f) { // clockwise
                    if(ladoActivado != Lado.amarillo){
                        getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                        gestorSP.guardarEvento("Sensor giroscopio - Se activo el background amarillo")
                        //RegistrarEvento al igual que se hizo como cuando se loguea
                        ladoActivado = Lado.amarillo
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
        }

        // Register the listener
        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

}