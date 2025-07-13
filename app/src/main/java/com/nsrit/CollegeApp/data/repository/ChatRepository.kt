package com.nsrit.CollegeApp.data.repository

import com.nsrit.CollegeApp.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor() {
    private val messages = MutableStateFlow<Map<String, List<ChatMessage>>>(emptyMap())
    private val userGroups = MutableStateFlow<Set<String>>(emptySet())
    private val db = FirebaseFirestore.getInstance()

    fun getMessages(groupId: String): List<ChatMessage> {
        return messages.value[groupId] ?: emptyList()
    }

    suspend fun sendMessage(groupId: String, content: String) {
        val newMessage = ChatMessage(
            id = System.currentTimeMillis().toString(),
            senderId = "currentUserId", // Replace with actual user ID
            senderName = "Current User", // Replace with actual user name
            content = content,
            timestamp = System.currentTimeMillis(),
            isMe = true
        )
        db.collection("chats")
            .document(groupId)
            .collection("messages")
            .document(newMessage.id)
            .set(newMessage)
            .await()
    }

    suspend fun joinGroup(groupId: String) {
        userGroups.value = userGroups.value + groupId
    }

    suspend fun leaveGroup(groupId: String) {
        userGroups.value = userGroups.value - groupId
    }

    fun isUserInGroup(groupId: String): Boolean {
        return groupId in userGroups.value
    }

    fun observeMessages(groupId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listener: ListenerRegistration = db.collection("chats")
            .document(groupId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull { it.toObject(ChatMessage::class.java) } ?: emptyList()
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }
} 