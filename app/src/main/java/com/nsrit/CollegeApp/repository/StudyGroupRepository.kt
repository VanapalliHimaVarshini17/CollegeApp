package com.nsrit.CollegeApp.repository

import com.nsrit.CollegeApp.model.StudyGroup
import com.nsrit.CollegeApp.model.MeetingSchedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class StudyGroupRepository {
    private val studyGroups = MutableStateFlow<List<StudyGroup>>(emptyList())

    fun getStudyGroups(): Flow<List<StudyGroup>> = studyGroups

    fun getMyStudyGroups(userId: String): Flow<List<StudyGroup>> =
        studyGroups.map { groups ->
            groups.filter { it.members.contains(userId) }
        }

    fun getAvailableStudyGroups(userId: String): Flow<List<StudyGroup>> =
        studyGroups.map { groups ->
            groups.filter { !it.members.contains(userId) && it.members.size < it.maxMembers }
        }

    suspend fun createStudyGroup(studyGroup: StudyGroup) {
        val currentGroups = studyGroups.value.toMutableList()
        currentGroups.add(studyGroup)
        studyGroups.value = currentGroups
    }

    suspend fun joinStudyGroup(groupId: String, userId: String) {
        val currentGroups = studyGroups.value.toMutableList()
        val groupIndex = currentGroups.indexOfFirst { it.id == groupId }
        if (groupIndex != -1) {
            val group = currentGroups[groupIndex]
            val updatedMembers = group.members.toMutableList().apply { add(userId) }
            currentGroups[groupIndex] = group.copy(members = updatedMembers)
            studyGroups.value = currentGroups
        }
    }

    suspend fun leaveStudyGroup(groupId: String, userId: String) {
        val currentGroups = studyGroups.value.toMutableList()
        val groupIndex = currentGroups.indexOfFirst { it.id == groupId }
        if (groupIndex != -1) {
            val group = currentGroups[groupIndex]
            val updatedMembers = group.members.toMutableList().apply { remove(userId) }
            currentGroups[groupIndex] = group.copy(members = updatedMembers)
            studyGroups.value = currentGroups
        }
    }

    suspend fun updateMeetingSchedule(groupId: String, meetingSchedules: List<MeetingSchedule>) {
        val currentGroups = studyGroups.value.toMutableList()
        val groupIndex = currentGroups.indexOfFirst { it.id == groupId }
        if (groupIndex != -1) {
            val group = currentGroups[groupIndex]
            currentGroups[groupIndex] = group.copy(meetingSchedule = meetingSchedules)
            studyGroups.value = currentGroups
        }
    }
} 