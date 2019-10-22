package com.example.proyecto

import com.google.android.gms.maps.model.LatLng

data class Muestra(var point: LatLng?, var index: Int)
{

        constructor() : this(point = null, index = -1)


}
