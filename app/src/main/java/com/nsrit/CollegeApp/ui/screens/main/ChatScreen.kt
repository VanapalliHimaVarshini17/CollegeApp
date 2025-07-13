package com.nsrit.CollegeApp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nsrit.CollegeApp.data.model.ChatMessage
import com.nsrit.CollegeApp.ui.screens.main.MainChatViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.collectAsState
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width

@Composable
fun ChatScreen(navController: NavController, viewModel: MainChatViewModel = hiltViewModel()) {
    val departments = listOf("Computer Science", "Electronics", "Mechanical", "Civil", "Electrical")
    var selectedDepartment by remember { mutableStateOf(departments.first()) }
    var messageText by remember { mutableStateOf("") }
    val chatMessages by viewModel.messages.collectAsState()
    var showFilePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    // Get current user ID (replace with actual auth logic if needed)
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "currentUserId"

    // Load messages for the selected department on first composition and when changed
    LaunchedEffect(selectedDepartment) {
        viewModel.loadMessages(selectedDepartment)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color(0xFFAECBFA))
            }

            Text(
                text = "Chat",
                style = MaterialTheme.typography.headlineMedium
            )

            Box(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Department Dropdown
        Text("Select Department to Chat", style = MaterialTheme.typography.bodyMedium)
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selectedDepartment)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                departments.forEach { dept ->
                    DropdownMenuItem(text = { Text(dept) }, onClick = {
                        selectedDepartment = dept
                        expanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Chat Messages
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chatMessages) { message ->
                val isMe = message.senderId == currentUserId
                ChatMessageItem(message.copy(isMe = isMe))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Message Input and File Picker
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                // TODO: Implement file picker logic
                showFilePicker = true
            }) {
                Icon(Icons.Default.AttachFile, contentDescription = "Attach File", tint = Color(0xFFAECBFA))
            }
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color(0xFFAECBFA),
                    unfocusedTextColor = Color(0xFFAECBFA),
                    cursorColor = Color(0xFFAECBFA),
                    focusedContainerColor = Color(0xFF1B263B),
                    unfocusedContainerColor = Color(0xFF1B263B),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Button(
                onClick = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(selectedDepartment, messageText)
                        messageText = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAECBFA))
            ) {
                Text("Send", color = Color(0xFF0D1B2A))
            }
        }

        // File picker dialog placeholder
        if (showFilePicker) {
            AlertDialog(
                onDismissRequest = { showFilePicker = false },
                title = { Text("File Picker") },
                text = { Text("File picker functionality will be implemented here.") },
                confirmButton = {
                    TextButton(onClick = { showFilePicker = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isMe) Arrangement.Start else Arrangement.End
    ) {
        if (message.isMe) {
            // Avatar for sent message (left)
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar",
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        // Message bubble (same color for both sent and received)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFAECBFA) // Use your default chat box color
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                color = Color(0xFF0D1B2A)
            )
        }

        if (!message.isMe) {
            Spacer(modifier = Modifier.width(4.dp))
            // Avatar for received message (right)
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar",
                modifier = Modifier.size(36.dp)
            )
        }
    }
} 