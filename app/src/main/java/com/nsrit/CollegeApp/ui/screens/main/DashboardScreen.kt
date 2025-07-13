package com.nsrit.CollegeApp.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.People
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nsrit.CollegeApp.navigation.Screen
import androidx.compose.ui.graphics.Color
import com.nsrit.CollegeApp.R
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val dashboardItems = remember {
        listOf(
            DashboardItem(
                title = "Profile",
                icon = Icons.Rounded.Person,
                onClick = { navController.navigate(Screen.Profile.route) }
            ),
            DashboardItem(
                title = "Events",
                icon = Icons.Rounded.DateRange,
                onClick = { navController.navigate(Screen.Events.route) }
            ),
            DashboardItem(
                title = "Class Schedule",
                icon = Icons.Rounded.Schedule,
                onClick = { navController.navigate(Screen.ClassSchedule.route) }
            ),
            DashboardItem(
                title = "Attendance",
                icon = Icons.Rounded.CheckCircle,
                onClick = { navController.navigate(Screen.Attendance.route) }
            ),
            DashboardItem(
                title = "Announcements",
                icon = Icons.Rounded.Notifications,
                onClick = { navController.navigate(Screen.Announcements.route) }
            ),
            DashboardItem(
                title = "Study Groups",
                icon = Icons.Rounded.People,
                onClick = { navController.navigate(Screen.StudyGroups.route) }
            ),
            DashboardItem(
                title = "Feedback",
                icon = Icons.Rounded.Star,
                onClick = { navController.navigate(Screen.Feedback.route) }
            ),
            DashboardItem(
                title = "News",
                icon = Icons.AutoMirrored.Rounded.List,
                onClick = { navController.navigate(Screen.News.route) }
            ),
            DashboardItem(
                title = "Chat",
                icon = Icons.Rounded.Email,
                onClick = { navController.navigate(Screen.Chat.route) }
            ),
            DashboardItem(
                title = "Quizzes",
                icon = Icons.Rounded.Quiz,
                onClick = { navController.navigate(Screen.Quiz.route) }
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color(0xFF0D1B2A),
            topBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.college_header),
                        contentDescription = "College Header Image",
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.Fit // Ensures the whole image is visible
                    )
                    // EDVORA APP title
                    Text(
                        text = "EDVORA APP",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFFD8DCE1),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                    CenterAlignedTopAppBar(
                        title = { Text("DASHBOARD") },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color(0xFF0D1B2A),
                            titleContentColor = Color(0xFFD3DAE7)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp, bottom = 0.dp)
                    )
                }
            }
        ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(dashboardItems) { item ->
                    DashboardCard(item)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardCard(item: DashboardItem) {
    Card(
        onClick = item.onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B263B),
            contentColor = Color(0xFFAECBFA)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFFAECBFA)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

private data class DashboardItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
) 