package com.collisioncatcher.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.collisioncatcher.retrofit.api.HardwareApi
import com.collisioncatcher.retrofit.entity.Speed
import com.collisioncatcher.retrofit.instance.RetrofitService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class HardwareViewModel : ViewModel() {

    val isLoading = mutableStateOf(false)
    val message = mutableStateOf("")
    val isSuccess = mutableStateOf(false)
    val isFailure = mutableStateOf(false)
    val speedList = mutableListOf<Speed>()
    val retrofit: HardwareApi = RetrofitService().getPlaneRetrofit().create<HardwareApi>(HardwareApi::class.java)
    val isSpeedFetching = mutableStateOf(false)

    fun startSpeedFetching(hardwareId:String){
        viewModelScope.launch {
            isLoading.value = true
            try{
                val response = retrofit.startSpeedStream(hardwareId)
                if (response.isSuccessful && response.code() == 200) {
                    isSuccess.value = true
                    message.value = "Speed Stream Started"
                    isSpeedFetching.value = true
                } else {
                    isFailure.value = true
                    message.value = "Some Error Has Occurred"
                }
            }
            catch (e: IOException) {
                message.value = "Network Error"
                isFailure.value = true

            }catch (e: HttpException) {
                message.value = "Server Error"
                isFailure.value = true

            }finally {
                isLoading.value = false
            }
        }
    }

    fun stopSpeedFetching(hardwareId:String){
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = retrofit.stopSpeedStream(hardwareId)
                if (response.isSuccessful && response.code() == 200) {
                    isSuccess.value = true
                    message.value = "Speed Stream Stopped"
                    isSpeedFetching.value = false
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

    fun fetchSpeed(hardwareId:String){
        viewModelScope.launch {
            while(isSpeedFetching.value){
                isLoading.value = true
                speedList.drop(speedList.size)
                try {
                    val response = retrofit.getLatestSpeed(hardwareId)
                    if (response.isSuccessful && response.code() == 200) {
                        isSuccess.value = true
                        message.value = "Speed Fetched"
                        response.body()?.forEach { speedList.add(it) }
                    }
                    else{
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
        }
    }
}
