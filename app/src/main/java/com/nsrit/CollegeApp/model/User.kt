package com.nsrit.CollegeApp.model

data class SemesterPerformance(
    val semester: String = "",
    val grade: String = ""
)

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val password: String = "********", // Masked for UI only
    val role: UserRole = UserRole.STUDENT,
    val profilePictureUrl: String = "",
    val department: String = "",
    val rollNumber: String = "", // For students
    val yearOfStudy: Int = 1, // For students
    val currentSemester: Int = 1, // For students
    val gender: String = "",
    val nationality: String = "",
    val dateOfBirth: String = "",
    val backlogs: Int = 0,
    val pastSemestersPerformances: List<SemesterPerformance> = emptyList(),
    val designation: String = "", // For faculty
    val phoneNumber: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class UserRole {
    STUDENT
} 