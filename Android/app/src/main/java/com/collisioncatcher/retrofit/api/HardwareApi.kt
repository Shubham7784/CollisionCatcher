package com.collisioncatcher.retrofit.api

import com.collisioncatcher.retrofit.entity.Hardware
import com.collisioncatcher.retrofit.entity.MapData
import com.collisioncatcher.retrofit.entity.Speed
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface HardwareApi {

    @POST("/hardware")
    fun addHardware(@Body hardware: Hardware,): Response<Hardware>

    @PUT("/hardware")
    fun updateHardware(@Body hardware: Hardware):Response<Hardware>

    @GET("/hardware/disable-motor")
    fun disableMotor():Response<String>

    @GET("/hardware/enable-motor")
    fun enableMotor():Response<String>

    @GET("/hardware/get-gps-data")
    fun getGpsData():Response<MapData>

    @POST("/hardware/{hardwareId}/stream/start")
    fun startSpeedStream(@Path("hardwareId") hardwareId: String): Response<String>

    @POST("/hardware/{hardwareId}/stream/stop")
    fun stopSpeedStream(@Path("hardwareId") hardwareId: String): Response<String>

    @GET("/hardware/{hardwareId}/latest")
    fun getLatestSpeed(@Path("hardwareId") hardwareId: String): Response<List<Speed>>
}