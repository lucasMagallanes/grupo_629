package com.example.helloandroidstudio.Utilidades

import android.content.Context
import androidx.appcompat.app.AlertDialog
import java.util.regex.Pattern

class Utilidades {

     companion object{
         fun mostrarAlerta(context:Context, titulo:String, subTitulo:String){
             val builder = AlertDialog.Builder(context)

             builder.setTitle(titulo)
             builder.setMessage(subTitulo)
             builder.setPositiveButton("Entendido"){dialog, which ->
             }
             val dialog: AlertDialog = builder.create()
             dialog.show()
         }

         fun esMailValido(email: String): Boolean {
             val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
             val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
             val matcher = pattern.matcher(email)
             return matcher.matches()
         }

//         fun verificarConectividad(): Boolean {
//             val connectivityManager =
//                 getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//             val networkInfo = connectivityManager.activeNetworkInfo
//
//             return networkInfo != null && networkInfo.isConnected
//         }
     }
}