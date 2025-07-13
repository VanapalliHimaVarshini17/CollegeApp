package com.nsrit.CollegeApp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nsrit.CollegeApp.ui.screens.auth.LoginScreen
import com.nsrit.CollegeApp.ui.screens.auth.SignUpScreen
import com.nsrit.CollegeApp.ui.screens.main.AnnouncementsScreen
import com.nsrit.CollegeApp.ui.screens.main.AttendanceScreen
import com.nsrit.CollegeApp.ui.screens.main.ChatScreen
import com.nsrit.CollegeApp.ui.screens.main.ClassScheduleScreen
import com.nsrit.CollegeApp.ui.screens.main.DashboardScreen
import com.nsrit.CollegeApp.ui.screens.main.QuizScreen
import com.nsrit.CollegeApp.ui.screens.main.EventsScreen
import com.nsrit.CollegeApp.ui.screens.main.FeedbackScreen
import com.nsrit.CollegeApp.ui.screens.main.NewsDetailScreen
import com.nsrit.CollegeApp.ui.screens.main.NewsScreen
import com.nsrit.CollegeApp.ui.screens.main.ProfileScreen
import com.nsrit.CollegeApp.ui.screens.main.StudyGroupsScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Auth Navigation
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController)
        }

        // Main Navigation
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
        composable(
            route = Screen.NewsDetail.route,
            arguments = listOf(navArgument("newsId") { type = NavType.StringType })
        ) { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
            NewsDetailScreen(navController, newsId)
        }
        composable(Screen.Chat.route) {
            ChatScreen(navController)
        }
        composable(Screen.Quiz.route) {
            QuizScreen(navController)
        }
    }
} 