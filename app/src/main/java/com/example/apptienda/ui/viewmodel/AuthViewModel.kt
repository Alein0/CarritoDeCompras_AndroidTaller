package com.example.apptienda.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptienda.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel (
private val repo: AuthRepository
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Inicializa el estado de sesión
    fun checkLogin() {
        viewModelScope.launch {
            _isLoggedIn.value = repo.getToken() != null
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = repo.login(username, password)
            _isLoggedIn.value = result.isSuccess
            if (result.isFailure) {
                _error.value = "Usuario o contraseña incorrectos"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            _isLoggedIn.value = false
        }
    }
}