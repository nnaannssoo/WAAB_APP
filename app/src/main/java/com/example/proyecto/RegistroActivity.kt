package com.example.proyecto
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.app.JobIntentService
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.example.proyecto.ForegroundService.Companion.serviceApi
import com.example.proyecto.ForegroundService.Companion.startService
import com.example.proyecto.MainActivity.Companion.ruta
import com.example.proyecto.RegistroActivity.Companion.TOLERANCE
import com.example.proyecto.Ruta.Companion.getNameFile
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_registro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList
import com.google.maps.android.PolyUtil as PolyUtil1

private const val LOCATION_PERMISSION_REQUEST_CODE = 1
class RegistroActivity : AppCompatActivity(), CancelListener
{
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
    private var isRegisterded= false
    private lateinit var LL: ConstraintLayout
    private var receiver = broadcast()
    private lateinit var mResultReceiver: AddressResultReceiver
    private var answer: Boolean? = null
    private var markerLocation : Marker? = null
    val REQUEST_CODE_LOCATION = 1
    private var fragmentVisible= false
    var addressOutput = ""
    companion object {
        const val TOLERANCE = 30.0
        private const val UPDATE_INTERVAL = 600000 // 10 min
        private const val FASTEST_INTERVAL = 10000 // 5 min
        private const val DISPLACEMENT = 10 // 1 km
        const val RUN_TIME_PERMISSION_CODE = 999
        var gpsState :Boolean= false
        lateinit var preferences : SharedPreferences
        fun setContributingFlagSharedPreferences(flag: Boolean, context : Context)
        {
            preferences = PreferenceManager.getDefaultSharedPreferences(context)
            var editor :SharedPreferences.Editor = preferences.edit()
            editor.putBoolean("contributing", flag)
            editor.apply()
        }
        fun getContributingFlagSharedPreferences():Boolean
        {
            return preferences.getBoolean("contributing", false)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        preferences = getDefaultSharedPreferences(this)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            map = it
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
            cargarTrayectoria(MainActivity.ruta!!)
        })
        LL = LinearLayoutRegistro
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        buildLocationRequest(5000, 1000, 10f)
        buildLocationCallback()
        checkGpsSetting()
        StartUpdateLocations()
        getUbicacion()
        var handler= Handler()
        mResultReceiver= AddressResultReceiver(handler)
        if (ubicacion != null)
            StopUpdateLocations()
        retrofit = Retrofit.Builder()
            .baseUrl("http://148.204.142.162:3031/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        serviceApi = retrofit.create<ApiService>(ApiService::class.java)
        btn_actualizar.setOnClickListener {
            StartUpdateLocations()
            var y = getUbicacion()
            if (y == null)
                checkGpsSetting()
            if (gpsState) {
                y = getUbicacion()
            }
            //locationRequest.setSmallestDisplacement(10.0f)
            Thread.sleep(1000)
            getUbicacion()
            StopUpdateLocations()
        }
        Log.d("Entra oncreate", isBound.toString())
        btn_confirmar.setOnClickListener {
            //validarRegistro()
            validarRegistro()
        }
        /*if(isBound)
            btn_confirmar.isEnabled=false*/
    }
    override fun onCancelListener() {
        Log.d("Cancelar","Servicio desde Fragment")
        if(isBound)
            myService!!.StopUpdateLocations()
        unBindService()
        ForegroundService.stopService(this)
        finish()
    }
    fun registerBroadcast(){
        /*var filter = IntentFilter()
        filter.addAction( ForegroundService.BROADCAST_ACTION_UNBIND)
        filter.addAction( ForegroundService.BROADCAST_ACTION_SPLASH)*/
        registerReceiver(
            receiver, IntentFilter(
                ForegroundService.BROADCAST_ACTION_UNBIND
            )
        )
        registerReceiver(
            receiver, IntentFilter(
                ForegroundService.BROADCAST_ACTION_SPLASH
            )
        )
        registerReceiver(
            receiver, IntentFilter(
                ForegroundService.BROADCAST_ACTION_VALIDATING
            ) )
        isRegisterded=true
    }
    fun unRegisterBroadcast(){
        if (isRegisterded) {
            unregisterReceiver(receiver)
        }
    }
    override fun onPause() {
        super.onPause()
        unRegisterBroadcast()
    }
    fun bindStoService(){
        val intent = Intent(this, ForegroundService::class.java)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
    }
    fun showContributingFragment(){
        if(!fragmentVisible) {
            LL.visibility = ConstraintLayout.GONE
            layoutFragment.visibility = ConstraintLayout.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.layoutFragment, ContributingFragment())
                .commit()
            fragmentVisible=true
        }
    }
    fun dismissContributingFragment(){
        if(fragmentVisible) {
            Log.d("Dismiis Fragment", "RegistroActivity 200")
            layoutFragment.visibility = ConstraintLayout.GONE
            LL.visibility = ConstraintLayout.VISIBLE
            supportFragmentManager.beginTransaction()
                .remove(ContributingFragment())
                .commit()
            fragmentVisible=false
        }
    }
    fun unBindService(){
        if(isBound) {
            Log.d("Desenlaza el servicio", "unBindService RegistroActivity")
            unbindService(myConnection)
            isBound = false
        }
    }
    override fun onResume() {
        super.onResume()
        registerBroadcast()
        bindStoService()
        Log.d("Entra onResume", isBound.toString())
        if (getContributingFlagSharedPreferences()) {
            showContributingFragment()
        }
        else {
            dismissContributingFragment()
            checkGpsSetting()
            getUbicacion()
            var hora = LocalDateTime.now()
            StopUpdateLocations()
        }
    }
    override fun onStart() {
        super.onStart()
        bindStoService()
        Log.d("Entra onstart", isBound.toString())
        if (getContributingFlagSharedPreferences()) {
            showContributingFragment()
        }
        else {
            getUbicacion()
            dismissContributingFragment()
            checkGpsSetting()
            StopUpdateLocations()
        }
    }
    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(myConnection)
            isBound = false
            if (answer==null)
                myService!!.setUserResponse(false)
        }
        //unRegisterBroadcast()
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
                        getUbicacion()
                    } else {
                        Toast.makeText(this, "Tenemos problemas con tu ubicación, intenta más tarde", Toast.LENGTH_SHORT).show()
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
                getUbicacion()
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
            Log.d("ENTRO AL CALLBACK","RegistroActivity")
                   //ubicacion= LatLng(22.7675,-102.572)
                   /* map.clear()
                    map.addMarker(MarkerOptions().position(ubicacion!!))
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion!!, 18f))*/
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
              /*  var geocoder = Geocoder(applicationContext, Locale.getDefault())
                var aux = geocoder.getFromLocation(location.latitude, location.longitude, 2)
                txt_direccion.text = aux[0].getAddressLine(0)*/
                startIntentService(location)
                ubicacion = LatLng(location.latitude, location.longitude)
                //txt_direccion.text = aux[0].getAddressLine(0)
                    //ubicacion= LatLng(22.7675,-102.572)
                if(markerLocation!=null)
                    markerLocation!!.position=ubicacion
                else
                    markerLocation= map.addMarker(MarkerOptions().position(ubicacion!!).draggable(true))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion!!, 14f))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion!!, 17f))
            }
            // StopUpdateLocations()
        }
        Log.d("Ubicacion  @getUbicacion RegistroActivity", ubicacion.toString())
        return ubicacion
    }
    fun wantsToConttribute(point : LatLng?, nearPoints: MutableList<Muestra>, needsValidation: Boolean){
        answer=null
        //Ask user if wants to contributte
        val builder = AlertDialog.Builder(this@RegistroActivity)
        // Set the alert dialog title
        builder.setTitle("SIGUE AYUDANDO... We All Are Bus")
        // Display a message on alert dialog
        builder.setMessage("¿Te gustaría colaborar durante tu viaje?")
        if(isBound)
            myService!!.setUserResponse(null)
        // Set a positive button and its click listener on alert dialog
        // Display a neutral button on alert dialog
        builder.setPositiveButton(
            "SI",
            DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(
                    applicationContext,
                    "Gracias por contribuir, no olvides registrar tu bajada.",
                    Toast.LENGTH_LONG
                ).show()
                bindStoService()
                if(!isBound)
                {
                    ForegroundService.startService(this,"Registrando colaboración...",point!!,ArrayList(nearPoints),needsValidation)
                    bindStoService()
                    //bindStoService()
                }
                myService!!.setUserResponse(true)
                answer=true
                finish()
            }
        )
        builder.setNegativeButton("NO") { _, _ ->
            Toast.makeText(applicationContext, "Gracias, hasta la próxima", Toast.LENGTH_LONG).show()
            myService!!.setUserResponse(false)
            answer = false
            unBindService()
            finish()
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()
    }
    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
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
    public fun validarRegistro() {
        if(ubicacion!=null) {
            var samples = loadSamples(getNameFile())
            val lastLocation = getUbicacion() //La ubicacion del usuario
            Log.d("Ubicacion", lastLocation.toString())
            var nearPoints = com.example.proyecto.google.PolyUtil.locationIndexOnEdgeOrPathPoint(
                ubicacion,
                samples,
                true,
                true,
                TOLERANCE
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
                builder.setMessage("Parece que tu ubicación no coincide con la ruta seleccionada. Asegurate de estar dentro de la trayectoria del camión de la  "+Ruta.getName())
                // Display a neutral button on alert dialog
                builder.setNeutralButton("OK"){_,_ ->
                }
                btn_confirmar.isEnabled=true
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
    private fun startIntentService(location :Location) {
        val intent = Intent(this, FetchingAddress::class.java).apply {
            putExtra(Constants.RECEIVER, mResultReceiver)
            putExtra(Constants.LOCATION_DATA_EXTRA, location)
        }
        FetchingAddress.enqueueWork(this,intent)
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
        var backGroungPoly = com.google.maps.android.PolyUtil.decode(polilinea)
        map.addPolyline(
            PolylineOptions().addAll(backGroungPoly)
                .width(15f)
                .color(Color.LTGRAY)
                .startCap(SquareCap())
                .endCap(SquareCap())
                .jointType(JointType.ROUND)
        )
    }
    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?){
            // Display the address string
            // or an error message sent from the intent service.
             var addressOutput = resultData?.getParcelable(Constants.RESULT_DATA_KEY) ?: Address(null)

            if(addressOutput.locale!=null) {
                var filteredAddress = "Calle: "+ addressOutput.thoroughfare + " "+addressOutput.featureName +"\n"+ "Colonia: "+ addressOutput.subLocality+"\n"+addressOutput.locality
                displayAddressOutput(filteredAddress)
            }
        }
    }
    private fun displayAddressOutput(addressText: String) {
        runOnUiThread { txt_direccion.setText(addressText) }
    }
    inner class broadcast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("Si entra aqui", "570 RegistroActivity")
            if(intent!!.action.equals(ForegroundService.BROADCAST_ACTION_UNBIND)){
                Log.d("UNBIND","RegistroActivy 68")
                btn_confirmar.isEnabled=true
                dismissContributingFragment()
                unBindService()
            }
            else if(intent!!.action.equals(ForegroundService.BROADCAST_ACTION_SPLASH))
            {
                Log.d("LANZAR SPLASH","RegistroActivy 573")
                showContributingFragment()
            }
            else if(intent!!.action.equals(ForegroundService.BROADCAST_ACTION_VALIDATING))
            {
                //Toast.makeText(applicationContext,"Si llego al Bloqueo RegistroActivity",Toast.LENGTH_SHORT).show()
                if(btn_confirmar.isEnabled==true){
                    Log.d("Si bloquea botones","547 Registro Activity"
                    )
                    btn_confirmar.isEnabled = false
                }
                //finish()
            }
        }
    }
}