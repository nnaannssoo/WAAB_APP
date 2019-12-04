package com.example.proyecto

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.support.constraint.ConstraintLayout
import android.support.constraint.solver.widgets.ConstraintAnchor
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.example.proyecto.Ruta.Companion.getNameFile
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.activity_time_request.*
import kotlinx.android.synthetic.main.content_time_request.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import com.google.gson.Gson

class TimeRequestActivity : AppCompatActivity() {
    lateinit var mapFragment: SupportMapFragment
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var markerLocation : Marker? = null
    private lateinit var ubicacion : LatLng
    private lateinit var serviceApi: ApiService
    lateinit var retrofit: Retrofit
    private val REQUEST_CODE_LOCATION = 1
    private var amb = false
    private var countresp = 0
    private var countAddress = 0
    private lateinit var samples : ArrayList<LatLng>
    private lateinit var mResultReceiver: AddressResultReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_request)
        setSupportActionBar(toolbar)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        buildLocationCallback()
        buildLocationRequest(5000,2000,10.0f)
        StartUpdateLocations()
        var handler= Handler()
        mResultReceiver= AddressResultReceiver(handler)
        var client = OkHttpClient.Builder()
            client.connectTimeout(10, TimeUnit.SECONDS)
            client.readTimeout(10,TimeUnit.SECONDS)
            client.writeTimeout(10, TimeUnit.SECONDS)

        retrofit = Retrofit.Builder()
            .baseUrl("http://148.204.142.162:3031/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
        serviceApi = retrofit.create<ApiService>(ApiService::class.java)
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapTime) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback{
                map = it
                map.uiSettings.setAllGesturesEnabled(true)
            Log.d("RUTA ",MainActivity.ruta!!.toString())
            cargarTrayectoria(MainActivity.ruta!!)
            map.setOnMapLongClickListener {
                    markerLocation!!.position = it
                    ubicacion=it
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion!!, 18f))
            }
        })

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Estamos procesando tu solicitud...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            get_index()

        }
    }
    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                var location = p0!!.locations.get(p0!!.locations.size - 1) //Ultima ubicacion
                ubicacion = LatLng(location.latitude,location.longitude)
                if(ubicacion!=null) {
                    if(markerLocation!=null)
                        markerLocation!!.position=ubicacion
                    else
                        markerLocation= map.addMarker(MarkerOptions().position(ubicacion!!).draggable(true))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion!!, 14f))
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion!!, 17f))
                }
            }
        }
    }
    private fun buildLocationRequest(fastesInterval: Long, interval: Long, smallestDisplacement: Float) {
        locationRequest = LocationRequest()
        locationRequest.fastestInterval = fastesInterval //1000
        locationRequest.interval = interval //3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.numUpdates=1
        locationRequest.smallestDisplacement = smallestDisplacement//meters
    }
    private fun loadSamples(nameFile: String): ArrayList<LatLng> {
        var inputStreamReader = InputStreamReader(assets.open(nameFile))
        var bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        val content = StringBuilder()
        var samples: ArrayList<LatLng> = ArrayList()
        try {
            var line = bufferedReader.readLine()
            while (line != null) {
                var a = line.split(",")
                samples.add(LatLng(a[0].toDouble(), a[1].toDouble()))
                line = bufferedReader.readLine()
            }
        } catch (e: Exception) {
            Log.d("Exc", e.toString())
        } finally {
            bufferedReader.close()
            return samples
        }
    }
    private fun StartUpdateLocations() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }
    public fun get_index(){
        samples = loadSamples(getNameFile())
        if(ubicacion!=null && samples !=null ) {

            var nearPoints = com.example.proyecto.google.PolyUtil.locationIndexOnEdgeOrPathPoint(
                ubicacion,
                samples,
                true,
                true,
                25.0
            )
            //NO AMBIGUICY
            if (nearPoints != null && nearPoints.size == 1) {
                //make HTTP REQUEST
                amb=false
                countresp=0
                getTime(ubicacion,nearPoints.get(0).index)
                Log.d("Index: ", nearPoints.get(0).index.toString())
            }
            else if(nearPoints.size>1)
            {
                //make HTTP REQUEST
                amb=true
                countresp=0
                countAddress=0
                getTime(ubicacion,nearPoints.get(0).index)
                Log.d("Index1: ", nearPoints.get(0).index.toString())
                getTime(ubicacion,nearPoints.get(1).index)
                Log.d("Index2: ", nearPoints.get(1).index.toString())
            }
            else
            {
                val builder = AlertDialog.Builder(this@TimeRequestActivity)
                // Set the alert dialog title
                builder.setTitle("Error")
                // Display a message on alert dialog
                builder.setMessage("Parece que tu ubicación no coincide con la ruta seleccionada. Asegurate de estar dentro de la trayectoria del camión de la  "+Ruta.getName())
                // Display a neutral button on alert dialog
                builder.setNeutralButton("OK"){_,_ ->
                }
                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()
                // Display the alert dialog on app interface
                dialog.show()
            }
        }
        else
        {
            checkGpsSetting()
        }
    }
    private fun checkGpsSetting(): Boolean {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest!!)
        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        task.addOnCompleteListener { result ->
            try {
                result.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
                RegistroActivity.gpsState = true
                StartUpdateLocations()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(this, MainActivity.GPS_REQUEST_CODE)
                    }
                    // Location settings are not satisfied. But could be fixed by showing the
                    // user a dialog.
                    // Cast to a resolvable exception.
                }
                RegistroActivity.gpsState = false
                // Show the dialog by calling startResolutionForResult(),
                // Location settings are not satisfied. However, we have no way to fix the
                // settings so we won't show the dialog.
            }
        }
        return RegistroActivity.gpsState
    }
    private fun startIntentService(location : Location) {
        val intent = Intent(this, FetchingAddress::class.java).apply {
            putExtra(Constants.RECEIVER, mResultReceiver)
            putExtra(Constants.LOCATION_DATA_EXTRA, location)
        }
        FetchingAddress.enqueueWork(this,intent)
    }
    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?){
            // Display the address string
            // or an error message sent from the intent service.
            var addressOutput = resultData?.getParcelable(Constants.RESULT_DATA_KEY) ?: Address(null)

            if(addressOutput.locale!=null) {
                var filteredAddress = "Con dirección a colonia: "+ addressOutput.subLocality
                if(countAddress==0) {
                    displayAddressOutput(filteredAddress, firstDirection)
                    countAddress+=1
                }
                else
                {
                    displayAddressOutput(filteredAddress, secondDirection)
                }
            }
            }

    }
    private fun displayAddressOutput(addressText: String, txtV : TextView) {
        runOnUiThread { txtV.setText(addressText) }
    }
    private fun cargarTrayectoria(ruta: Int) {
        Log.d("SI","Entra a cargar trayectoria")
        when (ruta) {
            0 -> {
                //RUTA 1 Antihorario
                getSupportActionBar()!!.setTitle(R.string.r1)
                agregarPolilinea(Ruta.ruta1.polilinea)
            }
            1->
            {
                //RUTA 1 Horario
                getSupportActionBar()!!.setTitle(R.string.r1)
                agregarPolilinea(Ruta.ruta1Horario.polilinea)
            }
            2->
            {
                getSupportActionBar()!!.setTitle(R.string.r2)
                agregarPolilinea(Ruta.ruta2.polilinea)
            }
            3->
            {
                getSupportActionBar()!!.setTitle(R.string.r3)
                agregarPolilinea(Ruta.ruta3.polilinea)
            }
            4 -> {
                getSupportActionBar()!!.setTitle(R.string.r4)
                agregarPolilinea(Ruta.ruta4.polilinea)

            }
            5->
            {
                getSupportActionBar()!!.setTitle(R.string.r5)
                agregarPolilinea(Ruta.ruta5.polilinea)
            }
            6 -> {
                //RUTA 7
                getSupportActionBar()!!.setTitle(R.string.r6)
                agregarPolilinea(Ruta.ruta6.polilinea)

            }
            7->
            {
                getSupportActionBar()!!.setTitle(R.string.r7)
                agregarPolilinea(Ruta.ruta7.polilinea)
            }
            8 -> {
                getSupportActionBar()!!.setTitle(R.string.r8)
                agregarPolilinea(Ruta.ruta8.polilinea)
            }
            9->
            {
                getSupportActionBar()!!.setTitle(R.string.r9)
                agregarPolilinea(Ruta.ruta9.polilinea)
            }
            10->
            {
                getSupportActionBar()!!.setTitle(R.string.r11)
                agregarPolilinea(Ruta.ruta11.polilinea)
            }
            11 -> {
                //RUTA 11B
                getSupportActionBar()!!.setTitle(R.string.r11)
                agregarPolilinea(Ruta.ruta11_b.polilinea)

            }
            12->
            {
                getSupportActionBar()!!.setTitle(R.string.r13)
                agregarPolilinea(Ruta.ruta13.polilinea)
            }
            13->
            {
                getSupportActionBar()!!.setTitle(R.string.r14)
                agregarPolilinea(Ruta.ruta14.polilinea)
            }
            14->
            {
                getSupportActionBar()!!.setTitle(R.string.r15)
                agregarPolilinea(Ruta.ruta15.polilinea)
            }
            15->
            {
                getSupportActionBar()!!.setTitle(R.string.r16)
                agregarPolilinea(Ruta.ruta16.polilinea)
            }
            16->
            {
                getSupportActionBar()!!.setTitle(R.string.r17)
                agregarPolilinea(Ruta.ruta17.polilinea)
            }
            17->
            {
                getSupportActionBar()!!.setTitle(R.string.rtdg)
                agregarPolilinea(Ruta.rutaTyL.polilinea)
            }
            18->
            {
                getSupportActionBar()!!.setTitle(R.string.rtyl)
                agregarPolilinea(Ruta.rutaTdG.polilinea)
            }
        }
    }

    private fun agregarPolilinea(polilinea: String) {
        var backGroungPoly = PolyUtil.decode(polilinea)
        map.addPolyline(
            PolylineOptions().addAll(backGroungPoly)
                .width(15f)
                .color(Color.LTGRAY)
                .startCap(SquareCap())
                .endCap(SquareCap())
                .jointType(JointType.ROUND)
        )
    }

    public fun getTime(destinationPoint: LatLng, originSample: Int) {
        val myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy/HH:mm:ss")
        var date = LocalDateTime.now().format(myFormat).toString()
        //Log.d("JSON", Gson().toJson(rdata))
        var pointString= destinationPoint.latitude.toString()+","+destinationPoint.longitude.toString()
        Log.d("OBJECTS",date.toString()+ " "+originSample.toString()+" - "+pointString+" - "+ MainActivity.ruta!!.toString())
        Log.d("TIME", java.util.Calendar.getInstance().time.toString())
        serviceApi.getTime(date,originSample,pointString,MainActivity.ruta!!).enqueue(object : Callback<timeResponse> {
            override fun onFailure(call: Call<timeResponse>, t: Throwable) {
                Log.d("Call: ",call.toString())
                t?.printStackTrace()
                Log.d("MAL SERVIDOR", "Algo salio mal con el servidor:   " + t.toString())
                Log.d("TIME", java.util.Calendar.getInstance().time.toString())
            }
            override fun onResponse(call: Call<timeResponse>, response: Response<timeResponse>) {
                val reg = response?.body()
                Log.d("Respuesta", Gson().toJson(reg))
                printTimeResponse(reg!!)
                var location = Location(LocationManager.GPS_PROVIDER).apply {
                    latitude = samples.get(originSample+10).latitude
                    longitude = samples.get(originSample+10).longitude
                }
                startIntentService(location)
            }
        })
    }
    fun setTRFVisible(constraintFragment: ConstraintLayout, boolean: Boolean)
    {
        if(boolean)
            constraintFragment.visibility = ConstraintLayout.VISIBLE
        else
            constraintFragment.visibility = ConstraintLayout.INVISIBLE
    }
    public fun printTimeResponse(response: timeResponse){
        if (!amb) {
            //imprimir en primer renglon
            Log.d("Respuesta1:", response.message.toString())
            //changeContent(firstResponse, response.message, (response.timeEstimation as Int).toString())
            setTRFVisible(firstFragment,true)
            FirstMessage.setText(response.message.toString())
            FirstTime.setText(response.timeEstimation.toString())
        }
        else{
            //imprimir en segundo renglon
            if(countresp==0){
                setTRFVisible(firstFragment,true)
                FirstMessage.setText(response.message.toString())
                FirstTime.setText(response.timeEstimation.toString())
                countresp+=1
            }
            else{
                setTRFVisible(secondFragment,true)
                SecondMessage.setText(response.message.toString())
                secondTime.setText(response.timeEstimation.toString())
            }
        }
    }
}
