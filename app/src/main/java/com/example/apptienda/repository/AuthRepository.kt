package com.example.apptienda.repository

import com.example.apptienda.data.network.AuthApi
import android.content.Context
import com.example.apptienda.data.local.SessionManager
import com.example.apptienda.data.model.LoginRequest

class AuthRepository (
    private val api: AuthApi,
    private val appContext: Context // appContext para evitar memory leak
) {
    suspend fun login(username: String, password: String): Result<String> {
        val response = api.login(LoginRequest(username, password))
        return if (response.isSuccessful && response.body()?.token != null) {
            val token = response.body()!!.token
            SessionManager.saveToken(appContext, token)
            Result.success(token)
        } else {
            Result.failure(Exception("Credenciales inválidas"))
        }
    }

    suspend fun logout() {
        SessionManager.clearToken(appContext)
    }

    suspend fun getToken(): String? = SessionManager.getToken(appContext)
}