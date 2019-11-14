package com.example.proyecto.google

import android.app.*
import android.app.Service.START_NOT_STICKY
import android.support.v4.content.ContextCompat
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.ServiceCompat.stopForeground
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.proyecto.*
import com.example.proyecto.R
import com.example.proyecto.RegisterService.Companion.CHANNEL_ID
import com.example.proyecto.RegisterService.Companion.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
import com.example.proyecto.RegisterService.Companion.UPDATE_INTERVAL_IN_MILLISECONDS
import com.example.proyecto.google.ForegroundService.Companion.serviceApi
import com.example.proyecto.google.ForegroundService.Companion.validated
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.SphericalUtil
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

class ForegroundService : Service() {
    private var nearPoints: java.util.ArrayList<Muestra>? = null
    private var needsValidation : Boolean? = null
    private val CHANNEL_ID = "Bakcground Service"
    private val CHANNEL_ID_2 = "Validation Service"
    private var  STATE = ""
    private final val stateValidating = "VALIDATING"
    private final val stateContributing = "CONTRIBUTING"
    private final val stateNone = "NONE"
    private final val stateWaitingForValidation="WAITING"
    private final val ERROR_CODE = 0
    private final val  SUCCESS_CODE=1
    val REQUEST_CODE_LOCATION = 1
    private lateinit var locationCallback : LocationCallback
    private lateinit var locationRequest : LocationRequest
    private final val UPDATE_INTERVAL_IN_MILLISECONDS : Long= 10000
    private final val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS :Long= UPDATE_INTERVAL_IN_MILLISECONDS/2
    private val notification : Notification? = null
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private val myBinder = MyBinder()
    private var firstLocation: LatLng?= null
    private var userResponse:Boolean?=false
    private var currentLlocation: LatLng?= null
    private var colaboration : Boolean? = null
    private var intents=0
    companion object {
        lateinit var serviceApi: ApiService
        lateinit var retrofit: Retrofit
        var validated : Boolean? = false

        fun startService(context: Context, message: String, point: LatLng, nearPoints :ArrayList<Muestra>?, validated : Boolean) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            startIntent.putExtra("validation",validated)
            //startIntent.putExtra("colaboration",colaboration)
            startIntent.putExtra("point",point)
            startIntent.putParcelableArrayListExtra("nearPoints",(ArrayList<Muestra>(nearPoints)))

            retrofit = Retrofit.Builder()
                .baseUrl("http://148.204.142.162:3031/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            serviceApi = retrofit.create<ApiService>(ApiService::class.java)

            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)
            Log.d("Ya se detuvo el serivicio","SERVICE OFF")

        }
    }
     private fun createLocationRequest() {
        locationRequest = LocationRequest()
         locationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
         locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
         locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
         locationRequest.smallestDisplacement = 50.0f
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
    fun setContributing()
    {
        STATE= this.stateContributing
    }
    fun setWaiting()
    {
        STATE= this.stateWaitingForValidation
    }
    fun setValidating()
    {
        STATE= this.stateValidating
    }
    fun setNone()
    {
        STATE= this.stateNone
    }
    public fun startContribution()
    {
        Log.d("Foreground Service", "Comenzó la contribución")
    }
    private fun validateFirst() {
        var samples = loadSamples(Ruta.getNameFile())
        Handler().postDelayed(Runnable {
            if(validated==false || validated==null) {
                validatedDialog(ERROR_CODE)
            }
        },120000)
        locationRequest.setExpirationDuration(120000)//300000)//3 min
                var newLocation = currentLlocation
                var distance = SphericalUtil.computeDistanceBetween(newLocation, firstLocation)
                    if (distance >= 50 && intents<4) {
                    intents+= 1
                    var newNearPoints = com.example.proyecto.google.PolyUtil.locationIndexOnEdgeOrPathPoint(
                        newLocation,
                        samples,
                        true,
                        true,
                        25.0
                    )
                    if (newNearPoints.size > 0 && newNearPoints != null) {
                        Log.d("Nuevos Puntos", newNearPoints[0].point.toString())
                        if (newNearPoints.size == 1) {
                            Log.d("Envía no ambi", newNearPoints[0].point!!.toString())
                            sendReg(newNearPoints[0].point!!, newNearPoints[0].index, regData.TYPE_FIRST_AMBIGUITY )
                            validated = true
                            //stopService(applicationContext)
                            validatedDialog(SUCCESS_CODE)
                        }
                        else if (newNearPoints[0].index > nearPoints!![0].index) {
                                //Bearing AB [0] = Ida
                                //Send newNearPoints [0] to server
                                Log.d("Envía IDA", newNearPoints[0].point!!.toString())
                                sendReg(newNearPoints[0].point!!, newNearPoints[0].index, regData.TYPE_FIRST_AMBIGUITY)
                                validated = true
                            //stopService(applicationContext)
                            validatedDialog(SUCCESS_CODE)

                            }
                        else if (newNearPoints[1].index > nearPoints!![1].index) {
                                //Bearing AB [0] = Regreso
                                Log.d("Envía no regreso", newNearPoints[1].point!!.toString())
                                sendReg(newNearPoints[1].point!!, newNearPoints[1].index, regData.TYPE_FIRST_AMBIGUITY)
                                validated = true
                            //stopService(applicationContext)
                            validatedDialog(SUCCESS_CODE)
                            }
                        else
                        {
                            Log.d("Actualización NO VALIDA: ", currentLlocation.toString())
                            //NO NEW NEAR POINTS
                            validated= false
                            if(intents>=4)
                            {
                                Log.d("ACTUALIZACION NO Y STOP","stop")
                               // stopService(applicationContext)
                                validatedDialog(ERROR_CODE)
                            }
                        }
                    }
                    else
                    {
                        //ERORR
                        validated=false
                        Log.d("Hizo falso y los intentos ",intents.toString())
                        if(intents>=4)
                        {
                            Log.d("ERROR", "NO NEW NEAR POINTS")
                          // fusedLocationClient.removeLocationUpdates(locationCallback)
                           //stopService(applicationContext)
                            validatedDialog(ERROR_CODE)
                        }
                    }
                }
                if(intents>=4 && validated== false)
                {
                    Log.d("NO pudo validar nunca ",intents.toString())
                   //fusedLocationClient.removeLocationUpdates(locationCallback)
                    //stopService(applicationContext)
                    validatedDialog(SUCCESS_CODE)
                }

    }
    private fun StartUpdateLocations() {
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

        } catch (unlikely: SecurityException) {
            Log.d("Error",unlikely.toString())
        }
    }
    private fun StopUpdateLocations()
    {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    fun validatedDialog(code : Int)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID_2, "Validation Channel",
                NotificationManager.IMPORTANCE_HIGH)

            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
        lateinit var notification: Notification

        if(code ==1) {
             notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID_2)
                .setContentTitle("We All Are Bus")
                .setContentText("Validacion correcta. Gracias por contribuir")
                .setSmallIcon(R.drawable.ic_ok)
                .build()
        }
        else {
             notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID_2)
                 .setColor(getColor(R.color.notificacionError))
                .setContentText("No pudimos validar tu registro para la ruta "+Ruta.getName())
                .setSmallIcon(R.drawable.ic_bad)
                .build()
        }
        with (NotificationManagerCompat.from(applicationContext))
        {
            notify(2, notification)
        }

    }
    private fun buildLocationCallback()
    {
        locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(p0: LocationResult?) {

                var location = p0!!.locations.get(p0!!.locations.size - 1) //Ultima ubicacion
                Log.d("Location", location.latitude.toString()+ " , "+location.longitude.toString())
                currentLlocation = LatLng(location.latitude, location.longitude)
                //checkState
                Log.d("STATE SERVICE",STATE)
                if(needsValidation!!)
                {
                    if (STATE == stateValidating)
                    {
                        if(!validated!!)
                        validateFirst()
                        else if(userResponse!!)
                        {
                            STATE = stateContributing
                        }
                        else
                        {
                            //Matar el servicio
                            Log.d("KILL SERVICE","ADIOS POPO")
                        }


                    }
                    if(STATE==stateContributing)
                    {
                        sendtoServer()
                    }
                    if(STATE==stateNone)
                    {
                        if(userResponse!!)
                        {
                            STATE = stateContributing
                        }
                        else
                        {
                            //Matar el servicio
                            Log.d("KILL SERVICE","ADIOS POPO")
                        }
                    }
                }
                else
                {
                    if(STATE==stateContributing)
                    {
                        startContribution()
                    }
                }

            }
        }
    }

    private fun sendtoServer() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun sendReg(point: LatLng, sample: Int, type : String) {

        val myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy/HH:mm:ss")
        var rdata = regData(
            point.latitude.toString() + "," + point.longitude.toString(),
            LocalDateTime.now().format(myFormat).toString(),
            MainActivity.ruta.toString(),
            type, sample
        )
        serviceApi.saveReg(rdata).enqueue(object : Callback<regData> {
            override fun onFailure(call: Call<regData>, t: Throwable) {
                Log.d("Something Wrong", "Algo salio mal con el servidor:   " + t.toString())
            }

            override fun onResponse(call: Call<regData>, response: Response<regData>) {
                val reg = response?.body()
                Log.d("Saved", Gson().toJson(reg))
            }
        })
    }

    fun setUserResponse(userResponse:Boolean)
    {
        this.userResponse = userResponse;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        //builLocationCallback()
        createLocationRequest()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val input = intent?.getStringExtra("inputExtra")
        firstLocation   = intent?.getParcelableExtra("point") as LatLng
        nearPoints =intent?.getParcelableArrayListExtra<Muestra>("nearPoints")
        needsValidation= intent?.getBooleanExtra("validation",false)
        if(needsValidation!!)
        STATE=stateValidating
        else
            STATE=stateNone
        //val colaboration: Boolean = intent?.getBooleanExtra("colaboration",false)
        createNotificationChannel()
        val notificationIntent = Intent(this, RegistroActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("We All Are Bus")
            .setContentText(input)
            .setColorized(true)
            .setColor(getColor(R.color.notificationColor))
            .setSmallIcon(R.drawable.ic_bus)
            .setContentIntent(pendingIntent)
            .build()
        buildLocationCallback()
        StartUpdateLocations()
       /* if(validation)
        validateFirst(point,nearPoints)*/
        startForeground(1, notification)
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return myBinder
    }
    fun setContribution(flag: Boolean)
    {
        this.colaboration=flag
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)

            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

     inner class MyBinder : Binder() {
        fun getService() :ForegroundService{
             // Simply return a reference to this instance
             //of the Service.
             return this@ForegroundService;
        }
    }




}