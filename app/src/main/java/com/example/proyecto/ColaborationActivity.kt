package com.example.proyecto

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.content.*
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.preference.PreferenceManager
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.proyecto.ColaborationActivity.Companion.mBound
import com.example.proyecto.google.ForegroundService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_colaboration.*
import retrofit2.Retrofit
import java.util.*

class ColaborationActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener{

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var serviceApi :ApiService
    public var mService :RegisterService?= RegisterService()
    private var ubicacion: LatLng? = null
    lateinit var retrofit: Retrofit
    val REQUEST_CODE_LOCATION = 1
    companion object
    {
        // Tracks the bound state of the service.
        public var mBound = false
    }
    private final var TAG : String = ColaborationActivity::class.java.canonicalName as String
    // Used in checking for runtime permissions.
    private final var REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    // The BroadcastReceiver used to listen from broadcasts from the service.
    private lateinit var myReceiver: LocationService
    private lateinit var serviceConnection :mServiceConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        mService = RegisterService()
        mService!!.setContext(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colaboration)
        myReceiver = LocationService()
        serviceConnection = mServiceConnection()
        // Check that the user hasn't revoked permissions by going to Settings.
        if (UtilsK.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions()
            }
        }
        if(mService!=null)
        {
            Log.d("Es null", "SIII")
        }
    }
    inner class mServiceConnection: ServiceConnection
    {
        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            mBound = false
        }
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as RegisterService.LocalBinder
            mService = binder.service
            mBound = true
        }
    }
    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
        startButton.setOnClickListener {
            if (!checkPermissions())
            {
                requestPermissions()
            }
            else
            {
                //ForegroundService.startService(this,"Servicio corriendo")
            }
        }
        stopButton.setOnClickListener {
                mService!!.removeLocationUpdates()
            }
        // Restore the state of the buttons when the activity (re)launches.
        //setButtonsState(Utils.requestingLocationUpdates(this));
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        //this.bindService(Intent(this,LocationService::class.java), serviceConnection ,Context.BIND_AUTO_CREATE)
    }
    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(RegisterService.ACTION_BROADCAST))
    }
    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver)
    }
    override fun onStop() {
        if(mBound)
        {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(serviceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }
    private fun checkPermissions() : Boolean{
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION);
    }
    @SuppressLint("ResourceType")
    private fun requestPermissions() {
        var  shouldProvideRationale =
        ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                this.findViewById(R.layout.activity_colaboration),
                R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, View.OnClickListener{
                        // Request permission
                        ActivityCompat.requestPermissions(this,
                           arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_PERMISSIONS_REQUEST_CODE);
                    }).show()
        }
        else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1000
        locationRequest.smallestDisplacement = 50.0f//meters
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key.equals(UtilsK.KEY_REQUESTING_LOCATION_UPDATES)) {
            setButtonsState(sharedPreferences!!.getBoolean(UtilsK.KEY_REQUESTING_LOCATION_UPDATES,
                false));
        }
    }
    private fun setButtonsState(requestingLocationUpdates: Boolean) {
        if (requestingLocationUpdates) {
             startButton.setEnabled(false)
             stopButton.setEnabled(true)
        } else {
            startButton.setEnabled(true)
            stopButton.setEnabled(false)
        }
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
            }
        }
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    /**
     * Callback received when a permissions request has been completed.
     */


    private fun StartUpdateLocations() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Si llega aqui", "OK")
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }

    }
}
