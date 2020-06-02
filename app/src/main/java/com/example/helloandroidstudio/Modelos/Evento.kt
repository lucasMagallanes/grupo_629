package com.example.helloandroidstudio.Modelos

class Evento() {
    var env: String = "DEV"
    var type_events: String = ""
    var state: String = "ACTIVO"
    var description: String = ""
    var group: Int = 629

    /**
     * Este constructor se utiliza cuando se registra un evento.
     */
    constructor(type_events:String, description:String):this(){
        this.type_events = type_events
        this.description = description
    }
}