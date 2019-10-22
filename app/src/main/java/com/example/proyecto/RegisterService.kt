package com.example.proyecto

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_registro.*
import retrofit2.Retrofit
import java.util.*

class RegisterService : Service()
{
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var serviceApi :ApiService
    private var ubicacion: LatLng? = null
    lateinit var retrofit: Retrofit
    val REQUEST_CODE_LOCATION = 1
    companion object {
        private const val UPDATE_INTERVAL = 600000 // 10 min
        private const val FASTEST_INTERVAL = 10000 // 5 min
        private const val DISPLACEMENT = 50 //  km
        const  val RUN_TIME_PERMISSION_CODE = 999
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Send a notification that service is started

        Toast.makeText(this,"Service started.",Toast.LENGTH_SHORT)
        Log.d("11","Ya empezo el servicio")
        starTrip()
        // Do a periodic task
        /*mHandler = Handler()
        mRunnable = Runnable { starTrip() }
        mRunnable.run()*/
       //
       // mHandler.postDelayed(mRunnable, 60000)

        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"Service destroyed.",Toast.LENGTH_SHORT)
        mHandler.removeCallbacks(mRunnable)
    }

    // Custom method to do a task
    private fun starTrip() {
        Toast.makeText(this,"Service working.",Toast.LENGTH_SHORT)
        buildLocationCallback()
        buildLocationRequest()
        StartUpdateLocations()
        //mHandler.postDelayed(mRunnable, 5000)
    }
    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.smallestDisplacement = DISPLACEMENT.toFloat()//meters
    }
    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                Toast.makeText(this@RegisterService,"Llegó nueva ubicacion",Toast.LENGTH_SHORT)
                var geocoder = Geocoder(applicationContext, Locale.getDefault())
                var location = p0!!.locations.get(p0!!.locations.size - 1) //Ultima ubicacion
                //txt_latlng.text = "Ultima Ubicacion:   " + (location.latitude.toString() + location.longitude.toString())
                var aux = geocoder.getFromLocation(location.latitude, location.longitude, 2);
                Toast.makeText(this@RegisterService, ubicacion.toString(), Toast.LENGTH_SHORT).show()
                ubicacion = LatLng(location.latitude, location.longitude)
                //txt_direccion.text = aux[0].getAddressLine(0)
                Log.d("Han pasado 50", ubicacion.toString() + aux[0].getAddressLine(0))
            }
        }
    }
    private fun StartUpdateLocations() {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }

    }
}

