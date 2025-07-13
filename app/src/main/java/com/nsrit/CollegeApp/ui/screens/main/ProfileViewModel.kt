package com.nsrit.CollegeApp.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsrit.CollegeApp.data.repository.AuthRepository
import com.nsrit.CollegeApp.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val currentUser = authRepository.currentUser
            if (currentUser != null) {
                val userData = authRepository.getUserData(currentUser.uid)
                _user.value = userData
            }
        }
    }

    fun updateProfile(updatedUser: User) {
        viewModelScope.launch {
            _user.value = updatedUser
            authRepository.updateUserData(updatedUser)
        }
    }
} 