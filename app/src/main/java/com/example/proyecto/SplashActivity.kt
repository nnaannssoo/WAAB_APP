package com.example.proyecto

import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Exception

class SplashActivity : AppCompatActivity() {
    private var isBound : Boolean = false
    private var myService : ForegroundService? = null
    private var receiver = broadcastSplash()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var filter = IntentFilter()
        filter.addAction(ForegroundService.BROADCAST_ACTION_UNBIND)
        filter.addAction(ForegroundService.BROADCAST_ACTION_DISMISS)
        registerReceiver(
            receiver, IntentFilter(
                ForegroundService.BROADCAST_ACTION_UNBIND
            )
        )
        registerReceiver(
            receiver, IntentFilter(
                ForegroundService.BROADCAST_ACTION_DISMISS
            )
        )
        registerReceiver(receiver,filter)
       cancelButton.setOnClickListener{
           ForegroundService.stopService(applicationContext)
           if(isBound)
           {
               myService!!.StopUpdateLocations()

           }
           unBindService()
           finish()
       }
        bindStoService()

    }
    fun bindStoService()
    {
        val intent = Intent(this, ForegroundService::class.java)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
        if(isBound)
        {
            Log.d("Servicio enlazado ","BindtoService Splash")
        }
        else
        {
            Log.d("Servicio no enlazado","BindtoService Splash")
            //ForegroundService.startService(this," ")
        }
    }
    fun unBindService()
    {
        if(isBound) {
            Log.d("Desenlaza el servicio", "unBindService Splash")
            unbindService(myConnection)
            isBound = false
        }
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
    inner class broadcastSplash : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("Si entra aqui", "82 Splash activity")
            if(intent!!.action.equals(ForegroundService.BROADCAST_ACTION_UNBIND))
            {
                Log.d("Desenlaza","85 Splash activity")
                unBindService()
            }
            else if(intent!!.action.equals(ForegroundService.BROADCAST_ACTION_DISMISS))
            {
                Log.d("CERRAR ","Splash ACTIVITY")
                onBackPressed()
                //finish()
                unregisterReceiver(receiver)
            }


        }


    }
    override fun onDestroy() {
        super.onDestroy()
        unBindService()
    }
    override fun onStop() {
        super.onStop()
        try {
            unregisterReceiver(receiver)
        } catch (c: Exception) {
            Toast.makeText(this, "Algo salió mal ", Toast.LENGTH_SHORT)
        }
    }
}
