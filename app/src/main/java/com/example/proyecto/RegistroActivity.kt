package com.example.proyecto
import android.content.*
import android.content.pm.PackageManager
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.example.proyecto.MainActivity.Companion.ruta
import com.example.proyecto.Ruta.Companion.getNameFile
import com.example.proyecto.google.ForegroundService
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_registro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import com.google.maps.android.PolyUtil as PolyUtil1

private const val LOCATION_PERMISSION_REQUEST_CODE = 1
class RegistroActivity : AppCompatActivity() {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    lateinit var mapFragment: SupportMapFragment
    private lateinit var serviceApi: ApiService
    private var ubicacion: LatLng? = null
    lateinit var retrofit: Retrofit
    private var myService : ForegroundService? = null
    private var isBound= false
    private lateinit var registerService: RegisterService
    val REQUEST_CODE_LOCATION = 1
    companion object {
        private const val UPDATE_INTERVAL = 600000 // 10 min
        private const val FASTEST_INTERVAL = 10000 // 5 min
        private const val DISPLACEMENT = 10 // 1 km
        const val RUN_TIME_PERMISSION_CODE = 999
        var gpsState :Boolean= false
        public var contribute: Boolean? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            map = it
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        })
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            buildLocationRequest(5000, 10000, 10f)
            buildLocationCallback()
            checkGpsSetting()
            StartUpdateLocations()
            getUbicacion()
            if (ubicacion != null)
                StopUpdateLocations()
        retrofit = Retrofit.Builder()
            .baseUrl("http://148.204.142.162:3031/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        serviceApi = retrofit.create<ApiService>(ApiService::class.java)
        btn_actualizar.setOnClickListener {
            StartUpdateLocations()
            //locationRequest.setSmallestDisplacement(10.0f)
            Thread.sleep(1000)
            getUbicacion()
            StopUpdateLocations()
        }
        btn_confirmar.setOnClickListener {
            //validarRegistro()
            var y = getUbicacion()
            if(y==null)
            //ForegroundService.startService(this,"Servicio corriendo",y!!)
                checkGpsSetting()
                if(gpsState)
                {
                    y =getUbicacion()
                }
            validarRegistro2()
        }
        btn_regresar.setOnClickListener {
            ForegroundService.stopService(this)
        }
    }
    fun bindStoService()
    {
        val intent = Intent(this, ForegroundService::class.java)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
        if(isBound)
        {
            Log.d("ENLAZADO","SI")
        }
        else
        {
            Log.d("NELPASTEL","NO")
            //ForegroundService.startService(this," ")
        }
    }
    override fun onStart() {
        super.onStart()
        checkGpsSetting()
        getUbicacion()
        var hora = LocalDateTime.now()
        StopUpdateLocations()
    }

    override fun onStop() {
        super.onStop()
        try {
            StopUpdateLocations()
        } catch (c: Exception) {
            Toast.makeText(this, "Algo salió mal ", Toast.LENGTH_SHORT)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.size > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Todo bien con la ubicacion", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No jala la ubicacion", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
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
                gpsState= true
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
                gpsState= false
                // Show the dialog by calling startResolutionForResult(),
                // Location settings are not satisfied. However, we have no way to fix the
                // settings so we won't show the dialog.
            }
        }
        return gpsState
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

    private fun StopUpdateLocations() {
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

        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                Toast.makeText(this@RegistroActivity, "Llegó nueva ubicacion", Toast.LENGTH_SHORT)
                var geocoder = Geocoder(applicationContext, Locale.getDefault())
                var location = p0!!.locations.get(p0!!.locations.size - 1) //Ultima ubicacion
                txt_latlng.text =
                    "Ultima Ubicacion:   " + (location.latitude.toString() + location.longitude.toString())
                var aux = geocoder.getFromLocation(location.latitude, location.longitude, 2);
                Toast.makeText(this@RegistroActivity, ubicacion.toString(), Toast.LENGTH_SHORT).show()
                ubicacion = LatLng(location.latitude, location.longitude)
                txt_direccion.text = aux[0].getAddressLine(0)
                if (ubicacion != null) {
                    //ubicacion= LatLng(22.7675,-102.572)
                    map.clear()
                    map.addMarker(MarkerOptions().position(ubicacion!!))
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion!!, 18f))
                }
                Log.d("Si", aux[0].getAddressLine(0))
            }
        }
    }
    private fun buildLocationRequest(fastesInterval: Long, interval: Long, smallestDisplacement: Float) {
        locationRequest = LocationRequest()
        locationRequest.fastestInterval = fastesInterval //1000
        locationRequest.interval = interval //3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.smallestDisplacement = smallestDisplacement//meters
    }
    fun getUbicacion(): LatLng? {
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
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                ubicacion = LatLng(location.latitude, location.longitude)
                var geocoder = Geocoder(applicationContext, Locale.getDefault())
                txt_latlng.text =
                    "Ultima Ubicacion:   " + (location.latitude.toString() + location.longitude.toString())
                var aux = geocoder.getFromLocation(location.latitude, location.longitude, 2)
                txt_direccion.text = aux[0].getAddressLine(0)
                Toast.makeText(this@RegistroActivity, ubicacion.toString(), Toast.LENGTH_SHORT).show()
                ubicacion = LatLng(location.latitude, location.longitude)
                txt_direccion.text = aux[0].getAddressLine(0)
                if (ubicacion != null) {
                    //ubicacion= LatLng(22.7675,-102.572)
                    map.clear()
                    map.addMarker(MarkerOptions().position(ubicacion!!))
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion!!, 18f))
                }
                else{
                    Log.d("NULLLLL","La ubicaicon es null")
                }
                Log.d("Si", aux[0].getAddressLine(0))
            }
            // StopUpdateLocations()
        }
        Log.d("Ubicacion", ubicacion.toString())
        return ubicacion
    }

    fun wantsToConttribute(point : LatLng?, nearPoints: MutableList<Muestra>, needsValidation: Boolean) :Boolean{
        var answer = false
        //Ask user if wants to contributte
        val builder = AlertDialog.Builder(this@RegistroActivity)
        // Set the alert dialog title
        builder.setTitle("SIGUE AYUDANDO... We All Are Bus")

        // Display a message on alert dialog
        builder.setMessage("¿Te gustaría colaborar durante tu viaje?")

        // Set a positive button and its click listener on alert dialog
        // Display a neutral button on alert dialog
        builder.setPositiveButton(
            "SI",
            DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(
                    applicationContext,
                    "Gracias por contribuir, no olvides REGISTRAR tu BAJADA",
                    Toast.LENGTH_SHORT
                ).show()
                bindStoService()
                if(isBound)
                {
                    //myService!!.setUserResponse(true)
                    if(needsValidation)
                    myService!!.setUserResponse(true)
                    else
                        myService!!.setContributing()
                }
                else
                {
                    ForegroundService.startService(this,"Registrando colaboración...",point!!, ArrayList(nearPoints),false )
                    bindStoService()
                    myService!!.setContributing()

                    //bindStoService()
                }
                answer=true
            }
        )
        builder.setNegativeButton("NO") { _, _ ->
            Toast.makeText(applicationContext, "Gracias, hasta la próxima", Toast.LENGTH_SHORT).show()
            answer = false
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
        return answer
    }
    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder
        ) {
            val binder = service as ForegroundService.MyBinder
            myService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }
    fun sendReg(point: LatLng, sample: Int, type: String) {

        val myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy/HH:mm:ss")
        var rdata = regData(
            point.latitude.toString() + "," + point.longitude.toString(),
            LocalDateTime.now().format(myFormat).toString(),
            ruta.toString(),
            type, sample
            //nearPoints[0].index
        )
        Log.d("JSON", Gson().toJson(rdata))
        serviceApi.saveReg(rdata).enqueue(object : Callback<regData> {
            override fun onFailure(call: Call<regData>, t: Throwable) {
                //t?.printStackTrace()
                Log.d("MAL SERVIDOR", "Algo salio mal con el servidor:   " + t.toString())
            }

            override fun onResponse(call: Call<regData>, response: Response<regData>) {
                val reg = response?.body()
                Log.d("TAG", Gson().toJson(reg))
            }
        })
    }
    public fun validarRegistro2() {
        if(ubicacion!=null) {
            var samples = loadSamples(getNameFile())
            val lastLocation = getUbicacion() //La ubicacion del usuario
            Log.d("Ubicacion", lastLocation.toString())
            var nearPoints = com.example.proyecto.google.PolyUtil.locationIndexOnEdgeOrPathPoint(
                ubicacion,
                samples,
                true,
                true,
                25.0
            )
            //NO AMBIGUICY
            if (nearPoints != null && nearPoints.size == 1) {
                ForegroundService.startService(this,"We All Are Bus en ejecución",nearPoints[0].point!!,ArrayList(nearPoints!!),false)
                bindStoService()
                sendReg(lastLocation!!,nearPoints[0].index, regData.TYPE_FISRT_NO_AMBIGUITY)
                Log.d("Gracias","Fin")
                wantsToConttribute(lastLocation!!, nearPoints,false)
            }
            else if(nearPoints.size>1)
            {
                if(checkGpsSetting())
                {
                    wantsToConttribute(lastLocation!!, nearPoints,true)
                    //ForegrounService receive true in action parameter if the service needs to validate
                    ForegroundService.startService(this,"Validando tu registro... ",lastLocation!!,ArrayList(nearPoints),true)
                    bindStoService()
                }
            }
            else
            {
                val builder = AlertDialog.Builder(this@RegistroActivity)
                // Set the alert dialog title
                builder.setTitle("Error")
                // Display a message on alert dialog
                builder.setMessage("Parece que tu ubicación no coincide con la ruta seleccionada. Asegurate de estar dentro de la trayectoria del camión de la ruta "+Ruta.getName())
                // Display a neutral button on alert dialog
                builder.setNeutralButton("OK"){_,_ ->
                    Toast.makeText(applicationContext,"OK.",Toast.LENGTH_SHORT).show()
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
    inner class broadcast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
           // finish()
        }


    };

}





