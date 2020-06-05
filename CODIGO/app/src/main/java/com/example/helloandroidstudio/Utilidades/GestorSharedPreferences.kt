package com.example.helloandroidstudio.Utilidades

import android.content.Context
import android.content.SharedPreferences

class GestorSharedPreferences (context: Context) {

    private val preferenciaprueba:String = "PREFSPrueba"
    private val llave:String = "eventosPruebas"
    val prefe1: SharedPreferences = context.getSharedPreferences(preferenciaprueba, 0)

    fun guardarEvento(evento:String){
        var valoresGuardados = prefe1.getString(llave,"")
        if (valoresGuardados.isNullOrEmpty()) {
            valoresGuardados = evento
        }else{
            valoresGuardados += ", $evento"
        }
        var editor: SharedPreferences.Editor = prefe1.edit()
        editor.putString(llave, valoresGuardados)
        editor.commit()
    }

    fun obtenerListaEventos():List<String>{
        var palabras2 =  prefe1.getString(llave,"")
        if (palabras2.isNullOrEmpty()) {
            return listOf<String>()
        }
        return palabras2!!.split(", ")
    }
}