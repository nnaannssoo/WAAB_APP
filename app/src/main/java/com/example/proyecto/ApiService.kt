package com.example.proyecto


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("registry/save/")
    fun saveReg(@Body registro: regData): Call<regData>
}