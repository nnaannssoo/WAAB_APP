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
import java.security.spec.ECField

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val background = object : Thread()
        {
            override fun run ()
            {
                try {

                        Thread.sleep(500)
                    val intent = Intent(baseContext,MainActivity::class.java)
                    startActivity(intent)
                }
                catch (e : Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
