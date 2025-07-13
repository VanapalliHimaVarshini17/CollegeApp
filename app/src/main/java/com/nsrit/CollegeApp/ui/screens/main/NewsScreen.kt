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

private data class NewsItem(
    val category: String,
    val title: String,
    val description: String,
    val date: Long? = null
)

@Composable
fun NewsScreen(navController: NavController) {
    val newsList = remember {
        listOf(
            NewsItem(
                category = "Tech News",
                title = "ISRO launches new satellite",
                description = "ISRO successfully launched the GSAT-24 communication satellite from Sriharikota.",
                date = SimpleDateFormat("yyyy-MM-dd").parse("2025-06-10")?.time
            ),
            NewsItem(
                category = "Current Affairs",
                title = "Union Budget 2025 Highlights",
                description = "The Finance Minister announced new reforms in the education and technology sectors.",
                date = SimpleDateFormat("yyyy-MM-dd").parse("2025-06-11")?.time
            ),
            NewsItem(
                category = "Placement Drives",
                title = "Wipro National Level Drive",
                description = "Wipro is conducting a national level placement drive for freshers on June 20th. Register online.",
                date = SimpleDateFormat("yyyy-MM-dd").parse("2025-06-20")?.time
            ),
            NewsItem(
                category = "Tech News",
                title = "Google announces new AI features",
                description = "Google has unveiled new AI-powered features for its search engine and workspace apps.",
                date = SimpleDateFormat("yyyy-MM-dd").parse("2025-06-09")?.time
            ),
            NewsItem(
                category = "Placement Drives",
                title = "TCS Off-Campus Drive",
                description = "TCS is organizing an off-campus drive for engineering graduates across India. Apply by June 18th.",
                date = SimpleDateFormat("yyyy-MM-dd").parse("2025-06-18")?.time
            ),
            NewsItem(
                category = "Current Affairs",
                title = "International Yoga Day",
                description = "India to celebrate International Yoga Day on June 21st with events across the country.",
                date = SimpleDateFormat("yyyy-MM-dd").parse("2025-06-21")?.time
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
                text = "News",
                style = MaterialTheme.typography.headlineMedium
            )

            Box(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(newsList) { news ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1B263B)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = news.category,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFAECBFA)
                        )
                        Text(
                            text = news.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFAECBFA)
                        )
                        if (news.date != null) {
                            Text(
                                text = "Date: " + SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(news.date)),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFAECBFA).copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                            )
                        }
                        Text(
                            text = news.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFAECBFA)
                        )
                    }
                }
            }
        }
    }
} 