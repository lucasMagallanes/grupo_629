package com.example.helloandroidstudio.Modelos

//class Usuario(var id:Int, var userId: Int, var title:String, var body:String)
/**
 * Este constructor se utiliza cuando se realiza una registración.
 */
class Usuario(){

    var env:String = "TEST" //TEST se usa sólo para probar que funcione la API en la registración.
    var name:String = ""
    var lastname: String = ""
    var dni: Int = 0
    var email: String =  ""
    var password: String = ""
    var commission: Int = 2900
    var group: Int = 629

    /**
     * Este constructor se utiliza cuando se hace un login.
     */
    constructor(email:String, password:String):this(){
        this.email =  email
        this.password = password
    }
}
