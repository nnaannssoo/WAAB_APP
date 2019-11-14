package com.example.proyecto

import android.app.*
import android.support.v4.content.ContextCompat
import android.content.Context
import android.content.Intent
import android.drm.DrmStore.Playback.STOP
import android.os.*
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.example.proyecto.google.PolyUtil
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
    private val CHANNEL_ID = "WAAB en ejecucion"
    private val CHANNEL_ID_2 = "Validación de registros"
    private val CHANNEL_ID_3 = "Contribucion durant el viaje"

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
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private val myBinder = MyBinder()
    private var firstLocation: LatLng?= null
    private var firstIndex: Int? = null
    private var userResponse:Boolean?=false
    private var currentLlocation: LatLng?= null
    private var colaboration : Boolean? = null
    private var intents=0
    private var samples : ArrayList<LatLng>? = null
    private lateinit var notification : Notification
    private var intent: Intent? = null
    companion object {
        lateinit var serviceApi: ApiService
        lateinit var retrofit: Retrofit
        var validated : Boolean? = false
        final val BROADCAST_ACTION = "com.proyecto.UNBIND"
        private final val STOP_ACTION = "STOP SERVICE FROM NOTIFICACION"
        private final val START_ACTION = "START SERVICE"
        fun startService(context: Context, message: String, point: LatLng, nearPoints :ArrayList<Muestra>?, validated : Boolean) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.action= START_ACTION
            startIntent.putExtra("inputExtra", message)
            startIntent.putExtra("validation",validated)
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
            Log.d("stopService @ForegroundService","Service off")

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
        samples = loadSamples(Ruta.getNameFile())
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
                    var newNearPoints = PolyUtil.locationIndexOnEdgeOrPathPoint(
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
                            firstIndex= newNearPoints[0].index
                            //stopService(applicationContext)
                            STATE= stateNone
                            validatedDialog(SUCCESS_CODE)
                        }
                        else if (newNearPoints[0].index > nearPoints!![0].index) {
                                //Bearing AB [0] = Ida
                                //Send newNearPoints [0] to server
                                Log.d("Envía IDA", newNearPoints[0].point!!.toString())
                                sendReg(newNearPoints[0].point!!, newNearPoints[0].index, regData.TYPE_FIRST_AMBIGUITY)
                                validated = true
                            firstIndex= newNearPoints[0].index
                            //stopService(applicationContext)
                            STATE= stateNone
                            validatedDialog(SUCCESS_CODE)

                            }
                        else if (newNearPoints[1].index > nearPoints!![1].index) {
                                //Bearing AB [0] = Regreso
                                Log.d("Envía no regreso", newNearPoints[1].point!!.toString())
                                sendReg(newNearPoints[1].point!!, newNearPoints[1].index, regData.TYPE_FIRST_AMBIGUITY)
                                validated = true
                            firstIndex= newNearPoints[1].index
                           STATE= stateNone

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
                                //validatedDialog(ERROR_CODE)
                                StopUpdateLocations()
                                ForegroundService.stopService(applicationContext)
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
                            //validatedDialog(ERROR_CODE)
                            StopUpdateLocations()
                            ForegroundService.stopService(applicationContext)

                        }
                    }
                }
                if(intents>=4 && validated== false)
                {
                    Log.d("NO pudo validar nunca ",intents.toString())
                    //validatedDialog(ERROR_CODE)
                    StopUpdateLocations()
                    ForegroundService.stopService(applicationContext)

                }

    }
    fun broadcastIntentUnbind() {
        sendBroadcast(intent)
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
        broadcastIntentUnbind()
    }
    fun validatedDialog(code : Int)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID_2, "Validation Channel",
                NotificationManager.IMPORTANCE_DEFAULT)

            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
        if(code == SUCCESS_CODE) {
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
    fun changeNotificationContent(message: String)
    {
        var stopSelfIntent =  Intent(this, ForegroundService::class.java)
        stopSelfIntent.action= STOP_ACTION
        var btPendingIntent = PendingIntent.getForegroundService( this, 0  ,stopSelfIntent,PendingIntent.FLAG_UPDATE_CURRENT ) ;
        this.notification= NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bus)
            .setOnlyAlertOnce(true)
            .setColorized(true)
            .setColor(getColor(R.color.notificationColaboration))
            .setContentTitle("We All Are Bus")
            .setContentText(message)
            .addAction(R.drawable.ic_launcher_foreground , "Detener colaboración" , btPendingIntent)
            .build()
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(1,notification);


    }
    fun contributingDialog()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID_3, "Contribution Channel",
                NotificationManager.IMPORTANCE_HIGH)

            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }

            notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID_3)
                .setContentTitle("We All Are Bus")
                .setColorized(true)
                .setColor(getColor(R.color.secondaryLightColor))
                .setContentText("Contribuyendo con mi viaje")
                .setSmallIcon(R.drawable.ic_ok)
                .build()

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
                Log.d("Location LocationCallBack Service:", location.latitude.toString()+ " , "+location.longitude.toString())
                currentLlocation = LatLng(location.latitude, location.longitude)
                //checkState
                Log.d("State on LocationCallBack Service: ",STATE)
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
                            Log.d("AQUI MATA", "335")
                            StopUpdateLocations()
                            stopService(applicationContext)
                        }


                    }
                    if(STATE==stateContributing)
                    {
                        //Cambiar el contenido de la notificacion
                        changeNotificationContent("Contribuyendo con mi viaje ...")
                        validateAndSend()
                    }
                    if(STATE==stateNone)
                    {
                        if(userResponse!!)
                        {
                            changeNotificationContent("Contribuyendo con mi viaje ...")
                            STATE = stateContributing
                        }
                        else
                        {
                            //Matar el servicio //No uhubo contribucion
                            Log.d("Usuario no quiere contribuir", "ForegroundService 355")
                            StopUpdateLocations()
                            stopService(applicationContext)

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

    private fun validateAndSend ()
    {
        if(samples!=null)
        {
            var location=currentLlocation
            var newPoints= PolyUtil.locationIndexOnEdgeOrPathPoint(
                location,
                samples,
                true,
                true,
                25.0
            )
            if(newPoints.size!= 0)
            {

                if(newPoints[0].index.compareTo(firstIndex!!)>0)
                {
                    //Valid, can be send to server
                    sendReg(newPoints[0].point!!, newPoints[0].index, regData.TYPE_CONTRIBUTTION)
                }
                else {
                    if (newPoints[1] != null && newPoints[1].index.compareTo(firstIndex!!) > 0) {
                        //Valid, can be send to server
                        sendReg(newPoints[1].point!!, newPoints[1].index, regData.TYPE_CONTRIBUTTION)
                    } else {
                        // Not valid, notify the user and then, stop service
                        //validatedDialog(this.ERROR_CODE)
                        Log.d("Aqui se muere el servicio", "Muere durante la colaboracion 411")
                        stopService(applicationContext)
                        StopUpdateLocations()
                    }
                }
            }
            else
            {
                // Not valid, notify the user, stop service
                //validatedDialog(this.ERROR_CODE)
                Log.d("Aqui se muere el servicio", "Muere durante la colaboracion 421")
                stopService(applicationContext)
                StopUpdateLocations()
            }

        }
        else
        {
            samples= loadSamples(Ruta.getNameFile())
            validateAndSend()
        }
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
        if(intent!!.action.equals(STOP_ACTION))
        {
            Log.d("STOP","onStartCommand")
            Companion.stopService(this)
            StopUpdateLocations()
            broadcastIntentUnbind()
        }
        else {
            Log.d("START","onStartCommand")
            this.intent = Intent(BROADCAST_ACTION)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            val input = intent?.getStringExtra("inputExtra")
            firstLocation = intent?.getParcelableExtra("point") as LatLng
            nearPoints = intent?.getParcelableArrayListExtra<Muestra>("nearPoints")
            needsValidation = intent?.getBooleanExtra("validation", false)
            if (needsValidation!!) {
                STATE = stateValidating
                Handler().postDelayed(Runnable {
                    if (validated == false) {
                        //Time exceeded
                        Log.d("Time Exceeded", "OnStartComand ")
                        StopUpdateLocations()
                        ForegroundService.stopService(applicationContext)
                        validatedDialog(ERROR_CODE)
                    }
                }, 20000) //2mins
            } else {
                STATE = stateNone
                firstIndex = nearPoints!![0].index
                Handler().postDelayed(Runnable {
                    Log.d("Time Exceeded", "OnStartComand 480 ")
                    StopUpdateLocations()
                    ForegroundService.stopService(applicationContext)
                }, 15000) //30 Sef
            }
            //val colaboration: Boolean = intent?.getBooleanExtra("colaboration",false)
            createNotificationChannel()
            val notificationIntent = Intent(this, RegistroActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                0, notificationIntent, 0
            )
            this.notification = NotificationCompat.Builder(this, CHANNEL_ID)
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
            startForeground(1, this.notification)

        }
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return myBinder
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
        fun getService() : ForegroundService {
             // Simply return a reference to this instance
             //of the Service.
             return this@ForegroundService;
        }
    }




}