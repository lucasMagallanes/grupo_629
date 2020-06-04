package com.example.helloandroidstudio.Modelos

class ResponseEvento {

    var state:String = "" //success ó error
    var env:String = "" //TEST ó DEV
    var event:Evento = Evento()

    //Cuando hay un error
    var msg:String = "" //Error en los parámetros enviados ó Error de autenticación

}