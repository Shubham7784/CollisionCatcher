package com.collisioncatcher.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.collisioncatcher.retrofit.api.HardwareApi
import com.collisioncatcher.retrofit.entity.MapData
import com.collisioncatcher.retrofit.entity.Speed
import com.collisioncatcher.retrofit.instance.RetrofitService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import kotlin.math.max
import androidx.core.net.toUri

class HardwareViewModel : ViewModel() {

    val isLoading = mutableStateOf(false)
    val message = mutableStateOf("")
    val isSuccess = mutableStateOf(false)
    val isFailure = mutableStateOf(false)
    private val _speed = MutableStateFlow(0.0)
    val speed: StateFlow<Double> = _speed
    val avgSpeed = mutableStateOf(0.0)
    val maxSpeed = mutableStateOf(0.0)
    val speedList = mutableListOf<Double>()
    val retrofit: HardwareApi =
        RetrofitService().getPlaneRetrofit().create<HardwareApi>(HardwareApi::class.java)
    val isSpeedFetching = mutableStateOf(false)

    val isVehicleArmed = MutableStateFlow(false)

    val locationData = MutableStateFlow<MapData?>(null)
    val isLocationTracking = MutableStateFlow(false)

    fun startSpeedFetching(hardwareId: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = retrofit.startSpeedStream(hardwareId)
                if (response.isSuccessful && response.code() == 200) {
                    isSuccess.value = true
                    message.value = response.body().toString()
                    isSpeedFetching.value = true
                    fetchSpeed(hardwareId)
                } else {
                    isFailure.value = true
                    message.value = "Some Error Has Occurred"
                }
            } catch (e: IOException) {
                message.value = "Network Error"
                isFailure.value = true

            } catch (e: HttpException) {
                message.value = "Server Error"
                isFailure.value = true

            } finally {
                isLoading.value = false
            }
        }
    }

    fun stopSpeedFetching(hardwareId: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = retrofit.stopSpeedStream(hardwareId)
                if (response.isSuccessful && response.code() == 200) {
                    isSuccess.value = true
                    message.value = "Speed Stream Stopped"
                    isSpeedFetching.value = false
                    speedList.clear()
                    avgSpeed.value = 0.0
                    maxSpeed.value = 0.0
                } else {
                    isFailure.value = true
                    message.value = "Some Error Has Occurred"
                }
            } catch (e: IOException) {
                message.value = "Network Error"
                isFailure.value = true

            } catch (e: HttpException) {
                message.value = "Server Error"
                isFailure.value = true

            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchSpeed(hardwareId: String) {
        viewModelScope.launch {
            isLoading.value = true
            while (isSpeedFetching.value) {
                try {
                    val response = retrofit.getLatestSpeed(hardwareId)
                    if (response.isSuccessful && response.code() == 200) {
                        isSuccess.value = true
                        message.value = "Speed Fetched"
                        response.body()?.let {
                            _speed.value = it.speed
                            speedList.add(it.speed)
                            avgSpeed.value = speedList.average()
                            maxSpeed.value = speedList.max()
                        }
                        Log.d("Speed Fetching",speed.value.toString())
                    } else {
                        isFailure.value = true
                        message.value = "Some Error Has Occurred"
                    }
                } catch (e: IOException) {
                    message.value = "Network Error"
                    isFailure.value = true

                } catch (e: HttpException) {
                    message.value = "Server Error"
                    isFailure.value = true

                } finally {
                    isLoading.value = false
                }

                delay(1000)
            }
            Log.e("Speed Fetching",message.value)
        }
    }

    fun enableMotor(hardwareId:String){
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = retrofit.enableMotor(hardwareId)
                if(response.isSuccessful && response.code()==200)
                {
                    isSuccess.value = true
                    message.value = response.body()?.message!!
                    isVehicleArmed.value = false
                }
                else
                {
                    isFailure.value = true
                    message.value = response.body()?.message!!
                }
            }
            catch (e: IOException) {
                message.value = "Network Error"
                isFailure.value = true
            }
            catch (e: HttpException) {
                message.value = "Server Error"
                isFailure.value = true
            }
            finally {
                isLoading.value = false
            }
        }
    }


    fun disableMotor(hardwareId:String){
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = retrofit.disableMotor(hardwareId)
                if(response.isSuccessful && response.code()==200)
                {
                    isSuccess.value = true
                    message.value = response.body()?.message!!
                    isVehicleArmed.value = true
                }
                else
                {
                    isFailure.value = true
                    message.value = response.body()?.message!!
                }
            }
            catch (e: IOException) {
                message.value = "Network Error"
                isFailure.value = true
            }
            catch (e: HttpException) {
                message.value = "Server Error"
                isFailure.value = true
            }
            finally {
                isLoading.value = false
            }
        }
    }

    fun getLocation(context: Context,hardwareId: String) {

        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = retrofit.getGpsData(hardwareId)
                if(response.isSuccessful && response.code()==200)
                {
                    isSuccess.value = true
                    message.value = "Location Fetched"
                    isLocationTracking.value = true
                    locationData.value = response.body()
                }
                else
                {
                    isFailure.value = true
                    message.value = "Some Error Has Occurred"
                    Log.e("Location Fetching Error",response.message())
                }
            }
            catch (e: IOException){
                message.value = "Network Error"
                isFailure.value = true
            }
            catch (e: HttpException){
                message.value = "Server Error"
                isFailure.value = true
            }
            finally {
                isLoading.value = false
                if(isSuccess.value && message.value == "Location Fetched")
                    openInGoogleMaps(context,locationData.value!!.latitude,locationData.value!!.longitude)
            }
        }

    }

    fun stopLocationFetching(hardwareId:String) {
        isLocationTracking.value = false
    }

    fun openInGoogleMaps(context: Context, lat: Double, lon: Double) {
        val uri = "geo:$lat,$lon?q=$lat,$lon(Hardware Location)".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }

}
