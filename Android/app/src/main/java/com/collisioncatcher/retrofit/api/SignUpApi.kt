package com.collisioncatcher.retrofit.api


import com.collisioncatcher.retrofit.entity.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {
    @POST("/public/signup")
    suspend fun signup(@Body user : User) : Response<User>

}