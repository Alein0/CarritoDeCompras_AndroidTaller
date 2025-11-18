package com.example.apptienda.data.network

import com.example.apptienda.data.model.LoginRequest
import com.example.apptienda.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String)

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}