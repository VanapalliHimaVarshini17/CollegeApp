package com.nsrit.CollegeApp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.Color

@Composable
fun NewsDetailScreen(
    navController: NavController,
    newsId: String
) {
    // Sample news data - in a real app, this would come from a repository
    val newsData = remember(newsId) {
        // This is sample data - replace with actual data fetching
        mapOf(
            "1" to NewsData(
                title = "New Library Resources Available",
                category = "Academic",
                date = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000,
                description = "The college library has added new digital resources including e-books, research papers, and online databases. Students can now access these resources through the library portal with their student credentials."
            ),
            "2" to NewsData(
                title = "Sports Meet Registration Open",
                category = "Sports",
                date = System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000,
                description = "Annual sports meet registration is now open. Students can register for various events including athletics, swimming, and team sports. The event will be held next month."
            )
        )[newsId] ?: NewsData(
            title = "News Not Found",
            category = "General",
            date = null,
            description = "The requested news article could not be found."
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
                text = "News Detail",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFAECBFA)
            )
            Box(modifier = Modifier.size(48.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = newsData.category, style = MaterialTheme.typography.labelMedium, color = Color(0xFFAECBFA))
        Text(text = newsData.title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFAECBFA))
        if (newsData.date != null) {
            Text(
                text = "Date: " + SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(newsData.date)),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFAECBFA).copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Text(text = newsData.description, style = MaterialTheme.typography.bodyLarge, color = Color(0xFFAECBFA))
    }
}

private data class NewsData(
    val title: String,
    val category: String,
    val date: Long?,
    val description: String
)