package com.nsrit.CollegeApp.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nsrit.CollegeApp.navigation.Screen

@Composable
fun MainScreen(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Screen.Events.route) {
            EventsScreen(navController)
        }
        composable(Screen.ClassSchedule.route) {
            ClassScheduleScreen(navController)
        }
        composable(Screen.Attendance.route) {
            AttendanceScreen(navController)
        }
        composable(Screen.Announcements.route) {
            AnnouncementsScreen(navController)
        }
        composable(Screen.StudyGroups.route) {
            StudyGroupsScreen(navController)
        }
        composable(Screen.Feedback.route) {
            FeedbackScreen(navController)
        }
        composable(Screen.News.route) {
            NewsScreen(navController)
        }
        composable(Screen.Chat.route) {
            ChatScreen(navController)
        }
    }
} 