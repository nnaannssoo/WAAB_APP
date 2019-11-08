package com.example.proyecto.google

import android.app.*
import android.app.Service.START_NOT_STICKY
import android.support.v4.content.ContextCompat
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
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
    private val CHANNEL_ID = "Validation/Colaboration Service"
    private final val ERROR_CODE = 0
    private final val  SUCCESS_CODE=1
    private lateinit var locationCallback : LocationCallback
    private lateinit var locationRequest : LocationRequest
    private final val UPDATE_INTERVAL_IN_MILLISECONDS : Long= 10000
    private final val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS :Long= UPDATE_INTERVAL_IN_MILLISECONDS/2
    private val notification : Notification? = null
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private var num = 9
    private val myBinder = MyBinder()
    private lateinit var ctxt : Context
    companion object {
        lateinit var serviceApi: ApiService
        lateinit var retrofit: Retrofit

        fun startService(context: Context, message: String, point: LatLng, nearPoints :ArrayList<Muestra>, colaboration : Boolean) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            startIntent.putExtra("colaboration",colaboration)
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
        }
    }
     private fun createLocationRequest() {
        locationRequest = LocationRequest()
         locationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
         locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
         locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
         locationRequest.smallestDisplacement = 50.0f
    }
    fun loadSamples(nameFile: String): ArrayList<LatLng> {
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
    public fun startContributtion()
    {
        Log.d("Aqui andamos compa", "Simon")
    }
    private fun validateFirst(point : LatLng, nearPoints: ArrayList<Muestra>) {
        //TODO("Cambiar esto para la implementación")
        Log.d("Near Points",nearPoints.toString())
        var lastLocation = point
        Log.d("Ruta", Ruta.getNameFile())
        var samples = loadSamples(Ruta.getNameFile())
        Log.d("First Point", lastLocation.toString())
        var intents=0
        var validated : Boolean? = null
        //locationRequest.setNumUpdates(2)
        locationRequest.setExpirationDuration(20000)//300000)//3 min
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                var location = p0!!.locations.get(p0!!.locations.size - 1) //Ultima ubicacion
                var newLocation = LatLng(location.latitude, location.longitude)
                Log.d("Updates", locationRequest.numUpdates.toString())
                Log.d("Actualización: ", location.latitude.toString() + " , " + location.longitude.toString())
                intents+= 1
                var distance = SphericalUtil.computeDistanceBetween(newLocation, lastLocation)
                if (distance >= 50) {
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
                            sendReg(newNearPoints[0].point!!, newNearPoints[0].index)
                            validated = true
                            validatedDialog(SUCCESS_CODE)
                            fusedLocationClient.removeLocationUpdates(locationCallback)
                            stopSelf()

                        }
                        else if (newNearPoints[0].index > nearPoints[0].index) {
                                //Bearing AB [0] = Ida
                                //Send newNearPoints [0] to server
                                Log.d("Envía IDA", newNearPoints[0].point!!.toString())
                                sendReg(newNearPoints[0].point!!, newNearPoints[0].index)
                                validated = true
                            validatedDialog(SUCCESS_CODE)
                            fusedLocationClient.removeLocationUpdates(locationCallback)
                            stopSelf()

                            }
                        else if (newNearPoints[1].index > nearPoints[1].index) {
                                //Bearing AB [0] = Regreso
                                Log.d("Envía no regreso", newNearPoints[1].point!!.toString())
                                sendReg(newNearPoints[1].point!!, newNearPoints[1].index)
                                validated = true
                            validatedDialog(SUCCESS_CODE)
                            fusedLocationClient.removeLocationUpdates(locationCallback)
                            stopSelf()

                            }
                        else
                        {
                            validated= false
                        }

                    }
                    else
                    {
                        //ERORR
                        validated=false
                        if(intents==4)
                        {
                            Log.d("ERROR", "NO NEW NEAR POINTS")
                            validatedDialog(ERROR_CODE)
                            stopSelf()
                        }


                    }

                }
                else
                {
                    if(validated==false && intents==4)
                    validatedDialog(ERROR_CODE)
                    stopSelf()
                }


            }


        }
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

        } catch (unlikely: SecurityException) {
           Log.d("Error",unlikely.toString())
        }




    }
    fun validatedDialog(code : Int)
    {

        val notificationIntent = Intent(applicationContext, RegistroActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0, notificationIntent, 0
        )
        lateinit var notification: Notification

        if(code ==1) {
             notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle("We All Are Bus")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentText("Validacion correcta. Gracias por contribuir")
                .setSmallIcon(R.drawable.ic_ok)
                .setContentIntent(pendingIntent)
                .build()
        }
        else {
             notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle("We All Are Bus")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                 .setColorized(true)
                 .setColor(getColor(R.color.notificacionError))
                .setContentText("No pudimos validar tu registro para la ruta "+Ruta.getName())
                .setSmallIcon(R.drawable.ic_bad)
                .setContentIntent(pendingIntent)
                .build()
        }
        with (NotificationManagerCompat.from(applicationContext))
        {
            notify(9, notification)
        }

        var c= applicationContext

    }
    private fun builLocationCallback()
    {
        locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(p0: LocationResult?) {

                var location = p0!!.locations.get(p0!!.locations.size - 1) //Ultima ubicacion
                Log.d("Location", location.latitude.toString()+ " , "+location.longitude.toString())
                var t = num.inc()
                Log.d("Num", t.toString()+"-"+num.toString())
                sendReg(LatLng(location.latitude,location.longitude),99)
            }
        }
    }
    public fun sendReg(point: LatLng, sample: Int) {

        val myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy/HH:mm:ss")
        var rdata = regData(
            point.latitude.toString() + "," + point.longitude.toString(),
            LocalDateTime.now().format(myFormat).toString(),
            MainActivity.ruta.toString(),
            "-", sample
            //nearPoints[0].index
        )
        Log.d("Registro", Gson().toJson(rdata))
        serviceApi.saveReg(rdata).enqueue(object : Callback<regData> {
            override fun onFailure(call: Call<regData>, t: Throwable) {
                //t?.printStackTrace()
                Log.d("Something Wrong", "Algo salio mal con el servidor:   " + t.toString())
                stopSelf()
            }

            override fun onResponse(call: Call<regData>, response: Response<regData>) {
                val reg = response?.body()
                Log.d("Saved", Gson().toJson(reg))
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        Log.d("Flags",flags.toString())
        //builLocationCallback()
        createLocationRequest()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        /*try {
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

        } catch (unlikely: SecurityException) {
           Log.d("Error",unlikely.toString())
        }*/
        val input = intent?.getStringExtra("inputExtra")
        val point   = intent?.getParcelableExtra("point") as LatLng
        val nearPoints =intent?.getParcelableArrayListExtra<Muestra>("nearPoints")
        val colaborationFlag= intent?.getBooleanExtra("colaboration",false)
        Log.d("QUIERE COLABORAR??",colaborationFlag.toString())
        validateFirst(point,nearPoints)
        createNotificationChannel()
        val notificationIntent = Intent(this, ColaborationActivity::class.java)
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
        startForeground(1, notification)

        //stopSelf();
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return myBinder
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_HIGH)

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