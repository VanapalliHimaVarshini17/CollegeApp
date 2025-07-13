package com.nsrit.CollegeApp.data.model

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
    val isMe: Boolean = false
) 