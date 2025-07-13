package com.nsrit.CollegeApp.model

data class Feedback(
    val id: String = "",
    val userId: String = "",
    val type: FeedbackType = FeedbackType.GENERAL,
    val title: String = "",
    val description: String = "",
    val rating: Int = 0, // 1-5 rating
    val status: FeedbackStatus = FeedbackStatus.PENDING,
    val response: String? = null,
    val respondedBy: String? = null, // User ID of the responder
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class FeedbackType {
    GENERAL,
    ACADEMIC,
    FACILITY,
    FACULTY,
    EVENT,
    OTHER
}

enum class FeedbackStatus {
    PENDING,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
} 