package com.nsrit.CollegeApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsrit.CollegeApp.model.StudyGroup
import com.nsrit.CollegeApp.model.MeetingSchedule
import com.nsrit.CollegeApp.repository.StudyGroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StudyGroupViewModel(private val repository: StudyGroupRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<StudyGroupUiState>(StudyGroupUiState.Loading)
    val uiState: StateFlow<StudyGroupUiState> = _uiState

    fun loadStudyGroups(userId: String) {
        viewModelScope.launch {
            _uiState.value = StudyGroupUiState.Loading
            try {
                repository.getStudyGroups()
                    .catch { e ->
                        _uiState.value =
                            StudyGroupUiState.Error(e.message ?: "Unknown error occurred")
                    }
                    .collect { groups ->
                        _uiState.value = StudyGroupUiState.Success(
                            myGroups = groups.filter { it.members.contains(userId) },
                            availableGroups = groups.filter { !it.members.contains(userId) && it.members.size < it.maxMembers }
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = StudyGroupUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun createStudyGroup(studyGroup: StudyGroup) {
        viewModelScope.launch {
            try {
                repository.createStudyGroup(studyGroup)
                loadStudyGroups(studyGroup.createdBy)
            } catch (e: Exception) {
                _uiState.value =
                    StudyGroupUiState.Error(e.message ?: "Failed to create study group")
            }
        }
    }

    fun joinStudyGroup(groupId: String, userId: String) {
        viewModelScope.launch {
            try {
                repository.joinStudyGroup(groupId, userId)
                loadStudyGroups(userId)
            } catch (e: Exception) {
                _uiState.value = StudyGroupUiState.Error(e.message ?: "Failed to join study group")
            }
        }
    }

    fun leaveStudyGroup(groupId: String, userId: String) {
        viewModelScope.launch {
            try {
                repository.leaveStudyGroup(groupId, userId)
                loadStudyGroups(userId)
            } catch (e: Exception) {
                _uiState.value = StudyGroupUiState.Error(e.message ?: "Failed to leave study group")
            }
        }
    }

    fun updateMeetingSchedule(groupId: String, meetingSchedules: List<MeetingSchedule>) {
        viewModelScope.launch {
            try {
                repository.updateMeetingSchedule(groupId, meetingSchedules)
                // Reload the current state to reflect the changes
                val currentState = _uiState.value
                if (currentState is StudyGroupUiState.Success) {
                    loadStudyGroups(currentState.myGroups.firstOrNull()?.createdBy ?: return@launch)
                }
            } catch (e: Exception) {
                _uiState.value =
                    StudyGroupUiState.Error(e.message ?: "Failed to update meeting schedule")
            }
        }
    }
}

sealed class StudyGroupUiState {
    object Loading : StudyGroupUiState()
    data class Success(
        val myGroups: List<StudyGroup>,
        val availableGroups: List<StudyGroup>
    ) : StudyGroupUiState()
    data class Error(val message: String) : StudyGroupUiState()
} 