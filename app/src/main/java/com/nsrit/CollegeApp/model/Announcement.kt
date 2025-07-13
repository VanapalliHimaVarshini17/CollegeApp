package com.nsrit.CollegeApp.model

data class Announcement(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val priority: AnnouncementPriority = AnnouncementPriority.NORMAL,
    val targetAudience: List<UserRole> = listOf(UserRole.STUDENT),
    val department: String? = null, // null means all departments
    val yearOfStudy: Int? = null, // null means all years
    val createdBy: String = "", // User ID of the creator
    val createdAt: Long = System.currentTimeMillis(),
    val attachments: List<String> = emptyList() // URLs to attached files
)

enum class AnnouncementPriority {
    LOW,
    NORMAL,
    HIGH,
    URGENT
} 