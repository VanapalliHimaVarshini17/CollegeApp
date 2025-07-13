package com.nsrit.CollegeApp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color

// Sample data model for Study Group
private data class StudyGroup(
    val id: String,
    val course: String,
    val groupName: String,
    val members: Int
)

@Composable
fun StudyGroupsScreen(navController: NavController) {
    val studyGroups = remember {
        listOf(
            StudyGroup("1", "Data Structures", "DS Warriors", 8),
            StudyGroup("2", "Database Management", "DB Masters", 6),
            StudyGroup("3", "Operating Systems", "OS Ninjas", 10),
            StudyGroup("4", "Computer Networks", "Net Gurus", 7)
        )
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
                text = "Study Groups",
                style = MaterialTheme.typography.headlineMedium
            )

            IconButton(onClick = { /* TODO: Implement create group */ }) {
                Icon(Icons.Default.Add, "Create Group", tint = Color(0xFFAECBFA))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(studyGroups) { group ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1B263B)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = group.groupName, style = MaterialTheme.typography.titleMedium, color = Color(0xFFAECBFA))
                        Text(text = "Course: ${group.course}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFAECBFA).copy(alpha = 0.7f))
                        Text(text = "Members: ${group.members}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFAECBFA).copy(alpha = 0.7f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = { /* TODO: Implement join group */ }) {
                                Text("Join")
                            }
                            Button(onClick = { /* TODO: Navigate to group discussion */ }) {
                                Text("Group Discussion")
                            }
                        }
                    }
                }
            }
        }
    }
} 