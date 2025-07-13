package com.nsrit.CollegeApp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Dashboard : Screen("dashboard")
    object Profile : Screen("profile")
    object Events : Screen("events")
    object ClassSchedule : Screen("class_schedule")
    object Attendance : Screen("attendance")
    object Announcements : Screen("announcements")
    object StudyGroups : Screen("study_groups")
    object Feedback : Screen("feedback")
    object News : Screen("news")
    object NewsDetail : Screen("news_detail/{newsId}") {
        fun createRoute(newsId: String) = "news_detail/$newsId"
    }
    object Chat : Screen("chat")
    object Quiz : Screen("quiz")
} 