package com.example.helloandroidstudio.Modelos

class ResponseLoguin {

    var state:String = "" //success ó error
    var env:String = "" //TEST ó DEV
    var user:Usuario = Usuario()

    //Cuando hay un error
    var msg:String = "" //Error en los parámetros enviados

    //Ambiente DEV
    var token:String = ""

}