package com.nsrit.CollegeApp.model

import com.nsrit.CollegeApp.data.model.Attendance
import com.nsrit.CollegeApp.data.model.ClassSchedule

class AttendanceModel(
    private val classSchedules: List<ClassSchedule>,
    private val attendanceMap: Map<String, List<Attendance>>,
    private val studentId: String
) {
    data class SubjectAttendance(
        val subjectName: String,
        val percentage: Double,
        val shortageAlert: String?,
        val classesNeededToRecover: Int
    )

    val subjectAttendanceList: List<SubjectAttendance>
    val overallPercentage: Double
    val overallShortageAlert: String?
    val overallClassesNeededToRecover: Int

    init {
        val subjectList = mutableListOf<SubjectAttendance>()
        var totalAttended = 0
        var totalClasses = 0
        var maxClassesNeeded = 0

        for (schedule in classSchedules) {
            val attendanceList = attendanceMap[schedule.id] ?: emptyList()
            val total = attendanceList.size
            val attended = attendanceList.count { it.presentStudents.contains(studentId) }
            val percent = if (total > 0) (attended.toDouble() / total) * 100 else 0.0
            val needed = classesNeededToReach65(attended, total)
            val alert = if (percent < 65.0) {
                "Attendance shortage in ${schedule.subjectName}: ${"%.1f".format(percent)}%. Attend $needed more classes to reach 65%."
            } else null
            if (needed > maxClassesNeeded) maxClassesNeeded = needed
            subjectList.add(
                SubjectAttendance(
                    subjectName = schedule.subjectName,
                    percentage = percent,
                    shortageAlert = alert,
                    classesNeededToRecover = needed
                )
            )
            totalAttended += attended
            totalClasses += total
        }
        subjectAttendanceList = subjectList
        overallPercentage = if (totalClasses > 0) (totalAttended.toDouble() / totalClasses) * 100 else 0.0
        overallClassesNeededToRecover = maxClassesNeeded
        overallShortageAlert = if (overallPercentage < 65.0) {
            "Overall attendance shortage: ${"%.1f".format(overallPercentage)}%. Attend $overallClassesNeededToRecover more classes to reach 65%."
        } else null
    }

    private fun classesNeededToReach65(attended: Int, total: Int): Int {
        var attendedVar = attended
        var totalVar = total
        var needed = 0
        while (totalVar > 0 && (attendedVar.toDouble() / totalVar) * 100 < 65.0) {
            attendedVar++
            totalVar++
            needed++
        }
        return needed
    }
} 