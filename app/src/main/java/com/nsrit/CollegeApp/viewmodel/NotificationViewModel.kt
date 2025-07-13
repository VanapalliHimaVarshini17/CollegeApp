package com.nsrit.CollegeApp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {

    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    init {
        getFCMToken()
    }

    private fun getFCMToken() {
        viewModelScope.launch {
            try {
                val token = Firebase.messaging.token.await()
                _token.value = token
                sendTokenToFirestore(token)
            } catch (e: Exception) {
                Log.w(TAG, "Fetching FCM registration token failed", e)
            }
        }
    }

    private fun sendTokenToFirestore(token: String) {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            val db = Firebase.firestore
            val userRef = db.collection("users").document(userId)
            userRef.update("fcmToken", token)
                .addOnSuccessListener { Log.d(TAG, "FCM token updated successfully") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating FCM token", e) }
        }
    }

    companion object {
        private const val TAG = "NotificationViewModel"
    }
} 