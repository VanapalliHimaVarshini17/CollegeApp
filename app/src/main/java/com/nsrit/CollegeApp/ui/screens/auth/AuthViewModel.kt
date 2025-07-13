package com.nsrit.CollegeApp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsrit.CollegeApp.data.repository.AuthRepository
import com.nsrit.CollegeApp.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    data object Initial : AuthState()
    data object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        val currentUser = authRepository.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    val userDoc = authRepository.getUserData(currentUser.uid)
                    _authState.value = AuthState.Authenticated(userDoc)
                } catch (e: Exception) {
                    _authState.value = AuthState.Error(e.message ?: "Failed to get user data")
                }
            }
        } else {
            _authState.value = AuthState.Initial
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = authRepository.signIn(email, password)
                result.fold(
                    onSuccess = { user ->
                        _authState.value = AuthState.Authenticated(user)
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Sign in failed")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    fun signUp(email: String, password: String, name: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = authRepository.signUp(email, password, name)
                result.fold(
                    onSuccess = { user ->
                        _authState.value = AuthState.Authenticated(user)
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Sign up failed")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _authState.value = AuthState.Initial
    }
} 