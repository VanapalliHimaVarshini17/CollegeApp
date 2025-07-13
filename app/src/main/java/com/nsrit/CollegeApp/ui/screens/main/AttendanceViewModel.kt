package com.nsrit.CollegeApp.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsrit.CollegeApp.data.model.Attendance
import com.nsrit.CollegeApp.data.model.ClassSchedule
import com.nsrit.CollegeApp.model.AttendanceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor() : ViewModel() {
    enum class AttendanceSection { SUMMARY, SUBJECTS, DAY_TO_DAY }

    private val _selectedSection = MutableStateFlow(AttendanceSection.SUMMARY)
    val selectedSection: StateFlow<AttendanceSection> = _selectedSection.asStateFlow()
    fun selectSection(section: AttendanceSection) { _selectedSection.value = section }

    // Sample studentId (replace with actual user id in real app)
    private val studentId = "S001"

    private val _classSchedules = MutableStateFlow<List<ClassSchedule>>(emptyList())
    val classSchedules: StateFlow<List<ClassSchedule>> = _classSchedules.asStateFlow()

    // Map: classScheduleId -> List<Attendance>
    private val _attendanceMap = MutableStateFlow<Map<String, List<Attendance>>>(emptyMap())
    val attendanceMap: StateFlow<Map<String, List<Attendance>>> = _attendanceMap.asStateFlow()

    // AttendanceModel for calculations
    private val _attendanceModel = MutableStateFlow<AttendanceModel?>(null)
    val attendanceModel: StateFlow<AttendanceModel?> = _attendanceModel.asStateFlow()

    // Expose summary/subject-wise properties
    val overallPercentage: Double get() = _attendanceModel.value?.overallPercentage ?: 0.0
    val overallShortageAlert: String? get() = _attendanceModel.value?.overallShortageAlert
    val overallClassesNeededToRecover: Int get() = _attendanceModel.value?.overallClassesNeededToRecover ?: 0
    val subjectAttendanceList: List<AttendanceModel.SubjectAttendance> get() = _attendanceModel.value?.subjectAttendanceList ?: emptyList()

    init {
        loadClassSchedules()
        loadAttendance()
    }

    private fun loadClassSchedules() {
        viewModelScope.launch {
            _classSchedules.value = listOf(
                ClassSchedule(
                    id = "1",
                    subjectName = "Data Structures",
                    subjectCode = "CS201",
                    facultyId = "F001",
                    roomNumber = "Room 101",
                    dayOfWeek = Calendar.MONDAY,
                    startTime = "09:00",
                    endTime = "10:30",
                    semester = 1,
                    year = 2024,
                    department = "Computer Science",
                    yearOfStudy = 2
                ),
                ClassSchedule(
                    id = "2",
                    subjectName = "Database Management",
                    subjectCode = "CS202",
                    facultyId = "F002",
                    roomNumber = "Room 102",
                    dayOfWeek = Calendar.MONDAY,
                    startTime = "11:00",
                    endTime = "12:30",
                    semester = 1,
                    year = 2024,
                    department = "Computer Science",
                    yearOfStudy = 2
                )
            )
        }
    }

    private fun loadAttendance() {
        viewModelScope.launch {
            _attendanceMap.value = mapOf(
                // For Data Structures: 10 classes, attended 6
                "1" to List(10) { i ->
                    Attendance(
                        id = "A1_$i",
                        classScheduleId = "1",
                        date = System.currentTimeMillis() - (10 - i) * 86400000L,
                        presentStudents = if (i < 6) listOf(studentId) else emptyList(),
                        absentStudents = if (i < 6) emptyList() else listOf(studentId),
                        markedBy = "F001"
                    )
                },
                // For Database Management: 8 classes, attended 4
                "2" to List(8) { i ->
                    Attendance(
                        id = "A2_$i",
                        classScheduleId = "2",
                        date = System.currentTimeMillis() - (8 - i) * 86400000L,
                        presentStudents = if (i < 4) listOf(studentId) else emptyList(),
                        absentStudents = if (i < 4) emptyList() else listOf(studentId),
                        markedBy = "F002"
                    )
                }
            )
            // Update AttendanceModel
            _attendanceModel.value = AttendanceModel(
                classSchedules = _classSchedules.value,
                attendanceMap = _attendanceMap.value,
                studentId = studentId
            )
        }
    }

    fun getDayToDayAttendance(subjectName: String): List<Pair<Long, Boolean>> {
        val schedule = _classSchedules.value.find { it.subjectName == subjectName } ?: return emptyList()
        val attendanceList = _attendanceMap.value[schedule.id] ?: emptyList()
        return attendanceList.sortedBy { it.date }.map { it.date to it.presentStudents.contains(studentId) }
    }

    fun markAttendance(attendance: Attendance) {
        viewModelScope.launch {
            val currentMap = _attendanceMap.value.toMutableMap()
            val list = currentMap[attendance.classScheduleId]?.toMutableList() ?: mutableListOf()
            list.add(attendance)
            currentMap[attendance.classScheduleId] = list
            _attendanceMap.value = currentMap
            // Update AttendanceModel
            _attendanceModel.value = AttendanceModel(
                classSchedules = _classSchedules.value,
                attendanceMap = _attendanceMap.value,
                studentId = studentId
            )
            // TODO: Save to repository
        }
    }
} 