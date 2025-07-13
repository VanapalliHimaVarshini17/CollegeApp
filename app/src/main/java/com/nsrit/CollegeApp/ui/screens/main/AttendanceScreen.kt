package com.nsrit.CollegeApp.ui.screens.main
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    navController: NavController,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedSection by viewModel.selectedSection.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Attendance Menu",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFFAECBFA)
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Summary") },
                    selected = selectedSection == AttendanceViewModel.AttendanceSection.SUMMARY,
                    onClick = {
                        viewModel.selectSection(AttendanceViewModel.AttendanceSection.SUMMARY)
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Subject-wise") },
                    selected = selectedSection == AttendanceViewModel.AttendanceSection.SUBJECTS,
                    onClick = {
                        viewModel.selectSection(AttendanceViewModel.AttendanceSection.SUBJECTS)
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Day-to-Day") },
                    selected = selectedSection == AttendanceViewModel.AttendanceSection.DAY_TO_DAY,
                    onClick = {
                        viewModel.selectSection(AttendanceViewModel.AttendanceSection.DAY_TO_DAY)
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Attendance", color = Color(0xFFAECBFA)) },
                    navigationIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFFAECBFA))
                            }
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color(0xFFAECBFA))
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)) {
                when (selectedSection) {
                    AttendanceViewModel.AttendanceSection.SUMMARY -> AttendanceSummaryView(viewModel)
                    AttendanceViewModel.AttendanceSection.SUBJECTS -> AttendanceSubjectWiseView(viewModel)
                    AttendanceViewModel.AttendanceSection.DAY_TO_DAY -> AttendanceDayToDayView(viewModel)
                }
            }
        }
    }
}

@Composable
private fun AttendanceSummaryView(viewModel: AttendanceViewModel) {
    val classSchedules by viewModel.classSchedules.collectAsState()
    val attendanceMap by viewModel.attendanceMap.collectAsState()
    val subjectAttendanceList = viewModel.subjectAttendanceList

    val totalAttended = subjectAttendanceList.sumOf { subject ->
        val schedule = classSchedules.find { it.subjectName == subject.subjectName }
        val attendanceList = attendanceMap[schedule?.id] ?: emptyList()
        attendanceList.count { it.presentStudents.contains("S001") } // Replace with actual studentId if needed
    }
    val totalClasses = subjectAttendanceList.sumOf { subject ->
        val schedule = classSchedules.find { it.subjectName == subject.subjectName }
        val attendanceList = attendanceMap[schedule?.id] ?: emptyList()
        attendanceList.size
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Present Attendance: $totalAttended",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp),
            color = Color(0xFFAECBFA)
        )
        Text(
            text = "Total Classes: $totalClasses",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color(0xFFAECBFA)
        )
        Text(
            text = "Overall Percentage: ${"%.1f".format(viewModel.overallPercentage)}%",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color(0xFFAECBFA)
        )
        if (viewModel.overallClassesNeededToRecover > 0) {
            Text(
                text = "Classes to be attended: ${viewModel.overallClassesNeededToRecover}",
                color = Color(0xFFD32F2F),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun AttendanceSubjectWiseView(viewModel: AttendanceViewModel) {
    val classSchedules by viewModel.classSchedules.collectAsState()
    val attendanceMap by viewModel.attendanceMap.collectAsState()
    val subjectAttendanceList = viewModel.subjectAttendanceList

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(subjectAttendanceList) { subject ->
            val schedule = classSchedules.find { it.subjectName == subject.subjectName }
            val attendanceList = attendanceMap[schedule?.id] ?: emptyList()
            val attended = attendanceList.count { it.presentStudents.contains("S001") } // Replace with actual studentId if needed
            val total = attendanceList.size
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = subject.subjectName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFAECBFA)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Present Attendance: $attended",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFAECBFA)
                    )
                    Text(
                        text = "Total Classes: $total",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFAECBFA)
                    )
                    Text(
                        text = "Percentage: ${"%.1f".format(subject.percentage)}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFAECBFA)
                    )
                    if (subject.classesNeededToRecover > 0) {
                        Text(
                            text = "Classes to be attended: ${subject.classesNeededToRecover}",
                            color = Color(0xFFD32F2F),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AttendanceDayToDayView(viewModel: AttendanceViewModel) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(viewModel.subjectAttendanceList) { subject ->
            val dayToDay = viewModel.getDayToDayAttendance(subject.subjectName)
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = subject.subjectName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFAECBFA)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (dayToDay.isEmpty()) {
                        Text("No attendance records.", color = Color(0xFFAECBFA))
                    } else {
                        dayToDay.forEach { (date, wasPresent) ->
                            val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(date))
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(dateStr, modifier = Modifier.weight(1f), color = Color(0xFFAECBFA))
                                Text(
                                    if (wasPresent) "Present" else "Absent",
                                    color = if (wasPresent) Color(0xFFAECBFA) else Color(0xFFD32F2F)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 