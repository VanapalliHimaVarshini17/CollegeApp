package com.nsrit.CollegeApp.data.model

data class Attendance(
    val id: String = "",
    val classScheduleId: String = "",
    val date: Long = System.currentTimeMillis(),
    val presentStudents: List<String> = emptyList(),
    val absentStudents: List<String> = emptyList(),
    val markedBy: String = "",
    val totalStudents: Int = 0,
    val subject: String = "",
    val className: String = "",
    val lastModified: Long = System.currentTimeMillis()
) 