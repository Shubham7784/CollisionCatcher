package com.collisioncatcher.retrofit.api

import com.collisioncatcher.retrofit.entity.ApiResponse
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
    suspend fun addHardware(@Body hardware: Hardware,): Response<Hardware>

    @PUT("/hardware")
    suspend fun updateHardware(@Body hardware: Hardware):Response<Hardware>

    @GET("/hardware/{hardwareId}/disable-motor")
    suspend fun disableMotor(@Path("hardwareId") hardwareId:String):Response<ApiResponse<String>>

    @GET("/hardware/{hardwareId}/enable-motor")
    suspend fun enableMotor(@Path("hardwareId") hardwareid:String):Response<ApiResponse<String>>

    @GET("/hardware/{hardwareId}/get-gps-data")
    suspend fun getGpsData(@Path("hardwareId") hardwareId:String):Response<MapData>

    @GET("/hardware/{hardwareId}/stream/start")
    suspend fun startSpeedStream(@Path("hardwareId") hardwareId: String): Response<ApiResponse<String>>

    @GET("/hardware/{hardwareId}/stream/stop")
    suspend fun stopSpeedStream(@Path("hardwareId") hardwareId: String): Response<ApiResponse<String>>

    @GET("/hardware/{hardwareId}/latest")
    suspend fun getLatestSpeed(@Path("hardwareId") hardwareId: String): Response<Speed>
}