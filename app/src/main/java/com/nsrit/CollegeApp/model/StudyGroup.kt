package com.nsrit.CollegeApp.model

import java.util.*

data class StudyGroup(
    val id: String,
    val name: String,
    val description: String,
    val subject: String,
    val createdBy: String,
    val members: List<String>,
    val maxMembers: Int,
    val meetingSchedule: List<MeetingSchedule>
)

data class MeetingSchedule(
    val id: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val location: String,
    val isOnline: Boolean,
    val meetingLink: String? = null
)

data class DayOfWeek(
    val value: Int,
    val name: String,
    val shortName: String
)

fun getDaysOfWeek(): List<DayOfWeek> = listOf(
    DayOfWeek(Calendar.SUNDAY, "Sunday", "Sun"),
    DayOfWeek(Calendar.MONDAY, "Monday", "Mon"),
    DayOfWeek(Calendar.TUESDAY, "Tuesday", "Tue"),
    DayOfWeek(Calendar.WEDNESDAY, "Wednesday", "Wed"),
    DayOfWeek(Calendar.THURSDAY, "Thursday", "Thu"),
    DayOfWeek(Calendar.FRIDAY, "Friday", "Fri"),
    DayOfWeek(Calendar.SATURDAY, "Saturday", "Sat")
) 