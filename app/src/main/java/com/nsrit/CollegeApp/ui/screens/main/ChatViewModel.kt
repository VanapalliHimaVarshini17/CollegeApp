package com.nsrit.CollegeApp.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsrit.CollegeApp.data.repository.ChatRepository
import com.nsrit.CollegeApp.data.model.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun loadMessages(groupId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = ChatUiState.Loading
                val messages = chatRepository.getMessages(groupId)
                _messages.value = messages
                _uiState.value = ChatUiState.Success
            } catch (e: Exception) {
                _uiState.value = ChatUiState.Error(e.message ?: "Failed to load messages")
            }
        }
    }

    fun sendMessage(groupId: String, content: String) {
        viewModelScope.launch {
            try {
                chatRepository.sendMessage(groupId, content)
                // Reload messages after sending
                loadMessages(groupId)
            } catch (e: Exception) {
                _uiState.value = ChatUiState.Error(e.message ?: "Failed to send message")
            }
        }
    }

    fun joinGroup(groupId: String) {
        viewModelScope.launch {
            try {
                chatRepository.joinGroup(groupId)
                _uiState.value = ChatUiState.Success
            } catch (e: Exception) {
                _uiState.value = ChatUiState.Error(e.message ?: "Failed to join group")
            }
        }
    }

    fun leaveGroup(groupId: String) {
        viewModelScope.launch {
            try {
                chatRepository.leaveGroup(groupId)
                _uiState.value = ChatUiState.Success
            } catch (e: Exception) {
                _uiState.value = ChatUiState.Error(e.message ?: "Failed to leave group")
            }
        }
    }
}

sealed class ChatUiState {
    object Loading : ChatUiState()
    object Success : ChatUiState()
    data class Error(val message: String) : ChatUiState()
} 