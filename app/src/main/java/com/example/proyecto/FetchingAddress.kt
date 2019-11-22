package com.example.proyecto

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.app.JobIntentService
import android.util.Log
import java.io.IOException
import java.util.*

class FetchingAddress : JobIntentService() {
    private var receiver: ResultReceiver? = null
    companion object
    {
        val JOB_ID = 1000
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, FetchingAddress::class.java, JOB_ID, work)
        }
    }
    override fun onHandleWork(intent: Intent) {
        intent ?: return
        var errorMessage = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        // Get the location passed to this service through an extra.
        val location = intent.getParcelableExtra<Location>(
            Constants.LOCATION_DATA_EXTRA)
        receiver = intent.getParcelableExtra(Constants.RECEIVER)
        var addresses: List<Address> = emptyList()
        try {
            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                // In this sample, we get just a single address.
                1)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.AddressTrouble)
            Log.e("ERROR GEOCODER", errorMessage, ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.AddressTrouble)
            Log.e("No address available ", "$errorMessage. Latitude = $location.latitude , " +
                    "Longitude =  $location.longitude", illegalArgumentException)
        }
        // Handle case where no address was found.
        if (addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.NoAddresFound)
                Log.e("No address found", errorMessage)
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage)
        } else {
            val address = addresses[0]
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            val addressFragments = with(address) {
                (0..maxAddressLineIndex).map { getAddressLine(it) }
            }
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                addressFragments.joinToString(separator = "\n"))
        }
    }
    private fun deliverResultToReceiver(resultCode: Int, message: String) {
        val bundle = Bundle().apply { putString(Constants.RESULT_DATA_KEY, message) }
        receiver?.send(resultCode, bundle)
    }

}