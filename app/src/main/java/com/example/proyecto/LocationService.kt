package com.example.proyecto

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.widget.Toast
import com.google.android.gms.location.LocationResult
import java.lang.Exception
import java.lang.StringBuilder

class LocationService : BroadcastReceiver(){


    companion object
    {
        val ACTION_PROCESS_UPDATE="Update_Location"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val location = intent!!.getParcelableExtra<Location>(LocationUpdatesService.EXTRA_LOCATION)
        if (location != null) {
            Toast.makeText(
                context!!.applicationContext, Utils.getLocationText(location),
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}

