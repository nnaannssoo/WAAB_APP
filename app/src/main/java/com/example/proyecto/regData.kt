package com.example.proyecto

import java.time.LocalDateTime
import java.util.*

class regData (
    var coordinates: String,
    var date:String,
    var id_route:String,
    var type:String,
    var id_sample:Int
)
{
    companion object{
         final val TYPE_FIRST_AMBIGUITY= "First After Ambiguity"
         final val TYPE_CONTRIBUTTION= "Contibution"
         final val TYPE_FISRT_NO_AMBIGUITY="First no Ambiguous"
    }
}
