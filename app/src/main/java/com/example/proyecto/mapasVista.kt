package com.example.proyecto

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.example.proyecto.Ruta.Companion.ruta5
import com.example.proyecto.Ruta.Companion.ruta7
import com.example.proyecto.google.MathUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.JointType.ROUND
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil
import kotlinx.android.synthetic.main.fragment_item_list_dialog.*
import kotlinx.android.synthetic.main.fragment_item_list_dialog_item.*
import java.io.*
import java.lang.Exception
import java.sql.Array


class mapasVista : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {

    private lateinit var ubicacionCliente: FusedLocationProviderClient
    private lateinit var ultimaUbicacion: Location  //
    lateinit var backGroungPoly: List<LatLng>
    lateinit var foreGroungPoly: List<LatLng>
    private var ruta: Int? = null
    private lateinit var mMap: GoogleMap
    private  val earthRadius = 6378000.0

    companion object {
        //PERSMISOS EN TIEMPO DE EJECUCIÓN
        private  val LOCATION_PERMISSION_REQUEST_CODE = 1
        lateinit  var backGroungPoly: List<LatLng>
        lateinit var foreGroungPoly: List<LatLng>
    }
    public fun getEarthRadius(): Double
    {
        return this.earthRadius
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapas_vista)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        this.ruta = MainActivity.ruta
        Toast.makeText(this, "Seleccionaste ruta : " + ruta.toString(), Toast.LENGTH_SHORT).show()


    }
    private fun agregarMarcador(coordenada: LatLng) {
        val marcador = MarkerOptions().position(coordenada)
        //Cambiar el icono o color del marcador marcador.icon()
        mMap.addMarker(marcador)
    }
    private fun agregarMarcador(lat: Double, lng: Double, icono: Int) {

        val marcador = MarkerOptions().position(LatLng(lat, lng))
            .icon(BitmapDescriptorFactory.fromResource(icono))
        //Cambiar el icono o color del marcador marcador.icon()
        mMap.addMarker(marcador)

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //
        mMap.setOnMarkerClickListener(this)
        mMap.uiSettings.isZoomControlsEnabled = true
        ubicacionCliente = LocationServices.getFusedLocationProviderClient(this)
        revisarPermisosMapa()
        cargarTrayectoria(this.ruta!!)
    }

    private fun agregarmarcadores(marcadores: ArrayList<LatLng>, icono: Int) {
        for (marcador in marcadores) {
            mMap.addMarker(MarkerOptions().position(marcador).icon(
                BitmapDescriptorFactory.fromResource(icono)))
        }
    }

    private fun revisarPermisosMapa() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            ) //Pedir el permiso de acceso a la ubicación
            return
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        mMap.isMyLocationEnabled = true // Para mostrar mi ubicacion en el mapa
        //Cuando obtengamos una localizacion se va a mandar llamar

        ubicacionCliente.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                ultimaUbicacion = location // location.latitude, location.longitude
                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(ultimaUbicacion.latitude,ultimaUbicacion.longitude),18f))
            }
        }
    }

    private fun agregarPolilinea(poliliniea: String) {
        backGroungPoly = PolyUtil.decode(poliliniea)
        mMap.addPolyline(
            PolylineOptions().addAll(backGroungPoly)
                .width(8f)
                .color(Color.BLACK)
                .startCap(SquareCap())
                .endCap(SquareCap())
                .jointType(ROUND)

        )
        mMap.addPolyline(
            PolylineOptions().addAll(backGroungPoly)
                .width(8f)
                .color(Color.YELLOW)
                .startCap(SquareCap())
                .endCap(SquareCap())
                .jointType(ROUND)
        )
        //animatePolyLine()

    }

    private fun animatePolyLine() {
        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 5000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animator1 ->

            val latLngList = backGroungPoly
            val initialPointSize = latLngList.size
            val animatedValue = animator1.animatedValue as Int
            val newPoints = animatedValue * backGroungPoly.size / 100

            if (initialPointSize < newPoints) {
                latLngList.union(backGroungPoly.subList(initialPointSize, newPoints))
                backGroungPoly=latLngList
            }
        }

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            Log.d("Mensaje","Si entra a la animacioon")
            }

            override fun onAnimationEnd(animation: Animator) {
                // 4
                val blackLatLng =  backGroungPoly
                val greyLatLng = foreGroungPoly

                greyLatLng.toMutableList().clear()

                greyLatLng.toMutableList().addAll(blackLatLng)
                blackLatLng.toMutableList().clear()


                backGroungPoly=blackLatLng
                foreGroungPoly=greyLatLng

                PolylineOptions().addAll(backGroungPoly).zIndex(2f)

                animator.start()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })


            }

    private fun loadSamples(nameFile: String): ArrayList<LatLng>
    {
        var inputStreamReader = InputStreamReader(assets.open(nameFile))
        var bufferedReader : BufferedReader = BufferedReader(inputStreamReader)
        val content = StringBuilder()
        var samples : ArrayList<LatLng> = ArrayList()
        try {
            var line= bufferedReader.readLine()
            while(line!=null)
            {
                var a = line.split(",")
                samples.add(LatLng (a[0].toDouble(),a[1].toDouble()))
                line = bufferedReader.readLine()

            }

        }
        catch(e : Exception)
        {
            Log.d("Exc",e.toString())

        }
        finally {
            bufferedReader.close()
            return samples
        }
    }
    private fun cargarTrayectoria(ruta: Int) {
        when (ruta) {
            0 -> {
                //RUTA 1 Antihorario
                agregarPolilinea(Ruta.ruta1.polilinea)
                var R5 = PolyUtil.decode(Ruta.ruta5.polilinea)
                //agregarmarcadores(Paradas.getR1(), R.drawable.m_1)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR1().get(0), 13f))
                //var punto= com.example.proyecto.google.PolyUtil.locationIndexOnEdgeOrPathPoint(LatLng(22.787214, -102.570000),backGroungPoly,true,true,50.0)
                //Log.d("AAAAA",punto.toString())
                var list = loadSamples("ruta_5.txt")



                //mas cerca de casa
                //22.763673, -102.597502
                //22.763651, -102.597674 enfente
                var points=com.example.proyecto.google.PolyUtil.locationIndexOnEdgeOrPathPoint(LatLng(22.763673, -102.597502),list,true,true,25.0)
                Log.d("Puntos mas cercano",points.toString())

            }
            1->
            {
                //RUTA 1 Horario
                getSupportActionBar()!!.setTitle(R.string.r2)
                agregarPolilinea(Ruta.ruta1Horario.polilinea)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR1().get(0), 13f))
                agregarmarcadores(Paradas.getR1(), R.drawable.m_1)
            }
            2->
            {
                getSupportActionBar()!!.setTitle(R.string.r2)
                agregarPolilinea(Ruta.ruta2.polilinea)
                agregarmarcadores(Paradas.getR2(), R.drawable.m_2)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR2().get(0), 13f))
            }
            3->
            {
                getSupportActionBar()!!.setTitle(R.string.r3)
                agregarPolilinea(Ruta.ruta3.polilinea)
                agregarmarcadores(Paradas.getR3(), R.drawable.m_3)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR3().get(0), 13f))
            }
            4 -> {
                getSupportActionBar()!!.setTitle(R.string.r4)
                agregarPolilinea(Ruta.ruta4.polilinea)
                agregarmarcadores(Paradas.getR4(), R.drawable.m_4)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR4().get(0), 13f))

            }
            5->
            {
                getSupportActionBar()!!.setTitle(R.string.r5)
                agregarPolilinea(Ruta.ruta5.polilinea)
                agregarmarcadores(Paradas.getR5(), R.drawable.m_5 )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR5().get(0), 13f))
            }
            6 -> {
                //RUTA 7
                getSupportActionBar()!!.setTitle(R.string.r6)
                /*val stream = File("C:\Users\\nnaan\\AndroidStudioProjects\Proyecto\app\src\main\res\raw\marcadores.xml").inputStream()
                val layer = KmlLayer(mMap, stream, this)
                layer.addLayerToMap();*/
                agregarPolilinea(Ruta.ruta6.polilinea)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR6().get(0), 13f))
                agregarmarcadores(Paradas.getR6(), R.drawable.m_6)

            }
            7->
            {
                getSupportActionBar()!!.setTitle(R.string.r7)
                agregarPolilinea(Ruta.ruta7.polilinea)
                agregarmarcadores(Paradas.getR7(), R.drawable.m_7)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR7().get(0), 13f))
            }
            8 -> {
                getSupportActionBar()!!.setTitle(R.string.r8)
                agregarPolilinea(Ruta.ruta8.polilinea)
                agregarmarcadores(Paradas.getR8(), R.drawable.m_8)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR8().get(0), 13f))
            }
            9->
            {
                getSupportActionBar()!!.setTitle(R.string.r9)
                agregarPolilinea(Ruta.ruta9.polilinea)
                agregarmarcadores(Paradas.getR9(), R.drawable.m_9)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR9().get(0), 13f))
            }
            10->
            {
                getSupportActionBar()!!.setTitle(R.string.r11)
                agregarPolilinea(Ruta.ruta11.polilinea)
                agregarmarcadores(Paradas.getR11(), R.drawable.m_11)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR11().get(0), 13f))
            }
            11 -> {
                //RUTA 11B
                getSupportActionBar()!!.setTitle(R.string.r11)
                agregarPolilinea(Ruta.ruta11_b.polilinea)
                agregarmarcadores(Paradas.getR11(), R.drawable.m_11)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR11().get(0), 13f))

            }
            12->
            {
                getSupportActionBar()!!.setTitle(R.string.r13)
                agregarPolilinea(Ruta.ruta13.polilinea)
                agregarmarcadores(Paradas.getR13(), R.drawable.m_13)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR13().get(0), 13f))
            }
            13->
            {
                getSupportActionBar()!!.setTitle(R.string.r14)
                agregarPolilinea(Ruta.ruta14.polilinea)
                agregarmarcadores(Paradas.getR14(), R.drawable.m_14)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR14().get(0), 13f))
            }
            14->
            {
                getSupportActionBar()!!.setTitle(R.string.r15)
                agregarPolilinea(Ruta.ruta15.polilinea)
                agregarmarcadores(Paradas.getR15(), R.drawable.m_15)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR15().get(0), 13f))
            }
            15->
            {
                getSupportActionBar()!!.setTitle(R.string.r16)
                agregarPolilinea(Ruta.ruta16.polilinea)
                agregarmarcadores(Paradas.getR16(), R.drawable.m_16)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR16().get(0), 13f))
            }
            16->
            {
                getSupportActionBar()!!.setTitle(R.string.r17)
                agregarPolilinea(Ruta.ruta17.polilinea)
                agregarmarcadores(Paradas.getR13(), R.drawable.m_17)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getR17().get(0), 13f))
            }
            17->
            {
                getSupportActionBar()!!.setTitle(R.string.rtyl)
                agregarPolilinea(Ruta.rutaTyL.polilinea)
                agregarmarcadores(Paradas.getRTyL(), R.drawable.m_tdg_1)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getRTyL().get(0), 13f))
            }
            18->
            {
                getSupportActionBar()!!.setTitle(R.string.rtdg)
                agregarPolilinea(Ruta.rutaTdG.polilinea)
                agregarmarcadores(Paradas.getRTdG(), R.drawable.m_tdg_1)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paradas.getRTdG().get(0), 13f))
            }



        }


    }
    fun  destinationPoint( point: LatLng, distance: Double,  bearing: Double): LatLng {
        //Obtained http://www.movable-type.co.uk/scripts/latlong.html#bearing

        // sinφ2 = sinφ1⋅cosδ + cosφ1⋅sinδ⋅cosθ
        // tanΔλ = sinθ⋅sinδ⋅cosφ1 / cosδ−sinφ1⋅sinφ2
        // see mathforum.org/library/drmath/view/52049.html for derivation

        var δ = distance / com.example.proyecto.google.MathUtil.EARTH_RADIUS; // angular distance in radians
        var θ = Math.toRadians(bearing)

        var φ1 = Math.toRadians(point.latitude)
        var λ1 = Math.toRadians(point.longitude)

        var sinφ2 = Math.sin(φ1) * Math.cos(δ) + Math.cos(φ1) * Math.sin(δ) * Math.cos(θ);
        var φ2 = Math.asin(sinφ2);
        var y = Math.sin(θ) * Math.sin(δ) * Math.cos(φ1);
        var x = Math.cos(δ) - Math.sin(φ1) * sinφ2;
        var λ2 = λ1 + Math.atan2(y, x);

        var lat = Math.toDegrees(φ2)
        var lon = Math.toDegrees(λ2)

        return LatLng(lat,lon)
    }
    private fun obtainSamplesbyDistance(polyline: ArrayList<LatLng>, distance: Double) : ArrayList <LatLng>?
    {
        //¿Como sacabamos el bearing?
        var sample : LatLng
            for(polypoint : LatLng in polyline )
        {
           // sample = destinationPoint(polypoint,distance, SphericalUtil.computeHeading(polypoint, ))
        }

        return null
    }

}
