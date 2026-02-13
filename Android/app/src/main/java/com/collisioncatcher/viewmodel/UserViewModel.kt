package com.collisioncatcher.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.collisioncatcher.retrofit.api.UserApi
import com.collisioncatcher.retrofit.entity.User
import com.collisioncatcher.retrofit.instance.RetrofitService
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import androidx.core.content.edit
import com.collisioncatcher.retrofit.api.SignUpApi
import com.collisioncatcher.retrofit.entity.Alert
import com.collisioncatcher.retrofit.entity.Member

class UserViewModel : ViewModel() {
    val isLoading = mutableStateOf(false)
    val message = mutableStateOf("")
    val isSuccess = mutableStateOf(false)
    val isFailure = mutableStateOf(false)

    fun login(user: User, context: Context) {
        val api = RetrofitService().getPlaneRetrofit().create<UserApi>(UserApi::class.java)
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = api.login(user)
                if (response.isSuccessful && response.code() == 200) {
                    isSuccess.value = true
                    message.value = "Login Successful"
                    val token = response.body() ?: ""
                    saveToken(context, token)
                } else {
                    message.value = "Invalid Credentials"
                    isFailure.value = true
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

    fun isLoggedIn(token: String) {
        val api = RetrofitService().getAuthRetrofit(token).create<UserApi>(UserApi::class.java)
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = api.isLoggedIn()
                if (response.isSuccessful && response.code() == 200) {
                    isSuccess.value = true
                    message.value = "Login Successful"

                } else {
                    message.value = "Invalid Credentials"
                    isFailure.value = true
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

    fun signup(user: User) {
        val api = RetrofitService().getPlaneRetrofit().create<SignUpApi>(SignUpApi::class.java)
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = api.signup(user)
                if (response.isSuccessful && response.code() == 201) {
                    isSuccess.value = true
                    message.value = "Registration Successful"
                } else if (response.isSuccessful && response.code() == 208) {
                    message.value = "User Already Exists"
                    isFailure.value = true
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

    suspend fun getUserDetails(context: Context): User? {
        val token = getToken(context).toString()
        val api = RetrofitService().getAuthRetrofit(token).create<UserApi>(UserApi::class.java)
        isLoading.value = true
        return try {
            val response = api.getUserDetails()
            if (response.isSuccessful && response.code() == 200) {
                isSuccess.value = true
                message.value = "User Found"
                response.body()
            } else if (response.isSuccessful && response.code() == 204) {
                isFailure.value = true
                message.value = "User Not Found"
                null
            } else {
                isFailure.value = true
                message.value = "Server Error"
                null
            }
        } catch (e: IOException) {
            message.value = "Network Error"
            isFailure.value = true
            null

        } catch (e: HttpException) {
            message.value = "Server Error"
            isFailure.value = true
            null

        } finally {
            isLoading.value = false
        }

    }

    suspend fun getMembers(context: Context): List<Member>?
    {
        val token = getToken(context)!!
        val api = RetrofitService().getAuthRetrofit(token).create<UserApi>(UserApi::class.java)
        isLoading.value = true
        return try {
            val response = api.getMembers()
            if(response.isSuccessful && response.code()==200) {
                isSuccess.value = true
                message.value = "Members Found"
                response.body()
            } else if(response.isSuccessful && response.code()==204) {
                isFailure.value = true
                message.value = "Members Not Found"
                null
            } else {
                isFailure.value = true
                message.value = "Some Error Has Occurred"
                null
            }
        } catch (e: IOException) {
            message.value = "Network Error"
            isFailure.value = true
            null

        } catch (e: HttpException) {
            message.value = "Server Error"
            isFailure.value = true
            null

        } finally {
            isLoading.value = false
        }
    }

    fun saveMember(context: Context,member: Member)
    {
        val token = getToken(context)!!
        val api = RetrofitService().getAuthRetrofit(token).create<UserApi>(UserApi::class.java)
        isLoading.value = true
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = api.addMember(member)
                if (response.isSuccessful && response.code() == 201) {
                    isSuccess.value = true
                    message.value = "Member Added Successfully"
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

    suspend fun getAlerts(context: Context): List<Alert>?{
        val api = RetrofitService().getPlaneRetrofit().create<UserApi>(UserApi::class.java)
        isLoading.value = true
        return try {
            val userName = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getString("userName",null);
            val response = api.getAlerts(userName.toString());
            if(response.isSuccessful && response.code()==200) {
                isSuccess.value = true
                message.value = "Alerts Available"
                response.body()
            } else if(response.isSuccessful && response.code()==204) {
                isFailure.value = true
                message.value = "No Alert Available"
                null
            } else {
                isFailure.value = true
                message.value = "Some Error Has Occurred"
                null
            }
        } catch (e: IOException) {
            message.value = "Network Error"
            isFailure.value = true
            null

        } catch (e: HttpException) {
            message.value = "Server Error"
            isFailure.value = true
            null

        } finally {
            isLoading.value = false
        }
    }
    private fun saveToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPref.edit { putString("auth_token", token) }
    }

    fun getToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("auth_token", null)
    }

    fun logout(context: Context) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPref.edit { remove("auth_token") }
    }
}