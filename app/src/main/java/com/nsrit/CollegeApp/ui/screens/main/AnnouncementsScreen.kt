package com.nsrit.CollegeApp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.Color

// Announcement data model
private data class Announcement(
    val title: String,
    val description: String,
    val date: Long? = null
)

@Composable
fun AnnouncementsScreen(navController: NavController) {
    val announcements = remember {
        listOf(
            Announcement(
                title = "Holiday Declaration",
                description = "The college will remain closed on June 15th for Eid-ul-Adha.",
                date = SimpleDateFormat("yyyy-MM-dd").parse("2025-06-15")?.time
            ),
            Announcement(
                title = "Annual Day Celebration",
                description = "Join us for the Annual Day celebration on July 10th in the main auditorium.",
                date = SimpleDateFormat("yyyy-MM-dd").parse("2025-07-10")?.time
            ),
            Announcement(
                title = "Placement Drive",
                description = "TCS and Infosys are conducting a placement drive on campus on June 20th. Register by June 18th.",
                date = SimpleDateFormat("yyyy-MM-dd").parse("2025-06-20")?.time
            ),
            Announcement(
                title = "Important Exam Dates",
                description = "Mid-semester exams will be held from August 5th to August 12th. Check the timetable for details.",
                date = null
            ),
            Announcement(
                title = "Blood Donation Camp Nearby",
                description = "A blood donation camp is being organized at City Hospital on June 25th. All are encouraged to participate.",
                date = SimpleDateFormat("yyyy-MM-dd").parse("2025-06-25")?.time
            )
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
                text = "Announcements",
                style = MaterialTheme.typography.headlineMedium
            )

            // Placeholder for settings
            Box(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(announcements) { announcement ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1B263B)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = announcement.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFAECBFA)
                        )
                        if (announcement.date != null) {
                            Text(
                                text = "Date: " + SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(announcement.date)),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFAECBFA).copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                            )
                        }
                        Text(
                            text = announcement.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFAECBFA).copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
} 