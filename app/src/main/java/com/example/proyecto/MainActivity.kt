package com.example.proyecto

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.proyecto.google.PolyUtil
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_ruta.*
import java.nio.file.Files.size
import android.widget.TextView



class MainActivity : AppCompatActivity(), opcionesSheetEx.BottomListener{

    override fun redirigir(ruta: Int) {

    }
    companion object
    {
        public var ruta: Int?=null
        public val Ruta1_0 = Ruta("Ruta 1","Cereso - La escondida",1)
            const val GPS_REQUEST_CODE = 999

    }

    private var arrayRutas: Array<String> ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ruta1 = Ruta("Ruta 1", sentido= "Cieneguillas → Huertas → Centro → La escondida → Cieneguillas", id = R.drawable.bus1)
       val ruta1b = Ruta("Ruta 1", sentido= "Cieneguillas → La escondida → Centro → Huertas → Cieneguillas   ", id = R.drawable.bus1)
        val ruta2 = Ruta("Ruta 2", sentido = "", id = R.drawable.bus2)
        val ruta3 = Ruta("Ruta 3", sentido = "Norte-Sur", id = R.drawable.bus3)
        val ruta4 = Ruta("Ruta 4", sentido = "Norte-Sur", id = R.drawable.bus4)
        val ruta5 = Ruta("Ruta 5", sentido = "Norte-Sur", id = R.drawable.bus5)
        val ruta6 = Ruta("Ruta 6", sentido = "Norte-Sur", id = R.drawable.bus6)
        val ruta7 = Ruta("Ruta 7", sentido = "Norte-Sur", id = R.drawable.bus7)
        val ruta8 = Ruta("Ruta 8", sentido = "Norte-Sur", id = R.drawable.bus8)
        val ruta9 = Ruta("Ruta 9", sentido = "Norte-Sur", id = R.drawable.bus9)
        val ruta11 = Ruta("Ruta 11", sentido = "Norte-Sur", id = R.drawable.bus11)
        val ruta13 = Ruta("Ruta 13", sentido = "Norte-Sur", id = R.drawable.bus13)
        val ruta14 = Ruta("Ruta 14", sentido = "Norte-Sur", id = R.drawable.bus14)
        val ruta15 = Ruta("Ruta 15", sentido = "Norte-Sur", id = R.drawable.bus15)
        val ruta16 = Ruta("Ruta 16", sentido = "Norte-Sur", id = R.drawable.bus16)
        val ruta17 = Ruta("Ruta 17", sentido = "Norte-Sur", id = R.drawable.bus17)
        val rutatg = Ruta("Ruta Transportes de Guadalupe", sentido = "Norte-Sur", id = R.drawable.bustg)
        val rutatyl = Ruta("Ruta Transportes de Guadalupe - Tierra y Libertad", sentido = "Norte-Sur", id = R.drawable.bustyl)

        val listaRutas=listOf(ruta1,ruta1b,ruta2,ruta3,ruta4,ruta5,ruta6,ruta7,ruta8, ruta9, ruta11, ruta13, ruta14,ruta15, ruta16, ruta17, rutatg, rutatyl)
        val adapter= RutasAdapter(mContext= this, listaRutas = listaRutas)
        listViewRutas.adapter=adapter
        listViewRutas.setOnItemClickListener { parent, view, position, id ->
            val fragmentoOpciones = opcionesSheetEx()
            fragmentoOpciones.setPosition(position);
            ruta=position;
            fragmentoOpciones.show(supportFragmentManager, "opcionesSheetEx")
        }
    }
    fun Toolbar.changeToolbarFont(){
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is TextView && view.text == title) {
                view.typeface = Typeface.createFromAsset(view.context.assets, "font/everything")
                break
            }
        }
    }

}
