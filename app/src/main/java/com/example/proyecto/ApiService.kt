package com.example.proyecto


import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("registry/save/")
    fun saveReg(@Body registro: regData): Call<regData>
    @GET("registry/test/")
    fun getTime(@Query("date") date: String,@Query("index") index:Int,@Query("destination") destinationPoint:String, @Query("id_route") route: Int ): Call<timeResponse>
}