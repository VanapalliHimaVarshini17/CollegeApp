package com.nsrit.CollegeApp.data.model

import java.util.Date

data class StudyGroup(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val subject: String = "",
    val creatorId: String = "",
    val createdAt: Date = Date(),
    val members: List<String> = emptyList(),
    val maxMembers: Int = 10,
    val meetingSchedule: MeetingSchedule? = null
)

data class MeetingSchedule(
    val dayOfWeek: Int, // 1 (Monday) to 7 (Sunday)
    val startTime: String, // HH:mm format
    val endTime: String, // HH:mm format
    val location: String
) 