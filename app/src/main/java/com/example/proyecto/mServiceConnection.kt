package com.example.proyecto

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.example.proyecto.ColaborationActivity.Companion.mBound

/*
class mServiceConnection: ServiceConnection
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
*/