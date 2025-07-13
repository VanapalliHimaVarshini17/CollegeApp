package com.nsrit.CollegeApp.data.model

data class ClassSchedule(
    val id: String = "",
    val subjectName: String = "",
    val subjectCode: String = "",
    val facultyId: String = "",
    val roomNumber: String = "",
    val dayOfWeek: Int = 1,
    val startTime: String = "",
    val endTime: String = "",
    val semester: Int = 1,
    val year: Int = 2024,
    val department: String = "",
    val yearOfStudy: Int = 1
) {
    companion object {
        fun fromFirestore(id: String, data: Map<String, Any?>): ClassSchedule {
            return ClassSchedule(
                id = id,
                subjectName = data["subjectName"] as? String ?: "",
                subjectCode = data["subjectCode"] as? String ?: "",
                facultyId = data["facultyId"] as? String ?: "",
                roomNumber = data["roomNumber"] as? String ?: "",
                dayOfWeek = (data["dayOfWeek"] as? Long)?.toInt() ?: 1,
                startTime = data["startTime"] as? String ?: "",
                endTime = data["endTime"] as? String ?: "",
                semester = (data["semester"] as? Long)?.toInt() ?: 1,
                year = (data["year"] as? Long)?.toInt() ?: 2024,
                department = data["department"] as? String ?: "",
                yearOfStudy = (data["yearOfStudy"] as? Long)?.toInt() ?: 1
            )
        }
    }
} 