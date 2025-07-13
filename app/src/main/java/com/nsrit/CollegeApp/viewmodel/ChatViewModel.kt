package com.nsrit.CollegeApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsrit.CollegeApp.data.model.ChatMessage
import com.nsrit.CollegeApp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadMessages(groupId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                chatRepository.observeMessages(groupId).collect { messages ->
                    _messages.value = messages
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load messages"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sendMessage(groupId: String, content: String) {
        viewModelScope.launch {
            try {
                chatRepository.sendMessage(groupId, content)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to send message"
            }
        }
    }

    fun joinGroup(groupId: String) {
        viewModelScope.launch {
            try {
                chatRepository.joinGroup(groupId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to join group"
            }
        }
    }

    fun leaveGroup(groupId: String) {
        viewModelScope.launch {
            try {
                chatRepository.leaveGroup(groupId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to leave group"
            }
        }
    }

    fun isUserInGroup(groupId: String): Boolean {
        return chatRepository.isUserInGroup(groupId)
    }

    fun clearError() {
        _error.value = null
    }
} 