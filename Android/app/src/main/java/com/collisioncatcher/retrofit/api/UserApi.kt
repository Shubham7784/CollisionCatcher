package com.collisioncatcher.retrofit.api

import com.collisioncatcher.retrofit.entity.Alert
import com.collisioncatcher.retrofit.entity.ApiResponse
import com.collisioncatcher.retrofit.entity.Member
import com.collisioncatcher.retrofit.entity.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @POST("/public/login")
    suspend fun login(@Body user: User): Response<ApiResponse<User>>

    @GET("/user/check-login")
    suspend fun isLoggedIn():Response<User>

    @POST("/member")
    suspend fun addMember(@Body member: Member): Response<Member>

    @GET("/member/getAll")
    suspend fun getMembers():Response<List<Member>>

    @GET("/user/getUserDetails")
    suspend fun getUserDetails():Response<User>

    @PUT("/user/update")
    suspend fun updateUserDetails(@Body updatedUser : User):Response<User>

    @GET("/alerts/{userName}")
    suspend fun getAlerts(@Path("userName") userName:String):Response<List<Alert>>

}