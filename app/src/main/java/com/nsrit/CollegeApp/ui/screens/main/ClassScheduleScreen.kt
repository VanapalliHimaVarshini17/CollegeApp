package com.nsrit.CollegeApp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nsrit.CollegeApp.data.model.ClassSchedule
import java.util.*
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassScheduleScreen(navController: NavController) {
    var selectedDay by remember { mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) }
    var showAddClassDialog by remember { mutableStateOf(false) }
    val schedules = remember {
        mutableStateListOf(
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Class Schedule") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFFAECBFA))
                    }
                },
                actions = {
                    IconButton(onClick = { showAddClassDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Class", tint = Color(0xFFAECBFA))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Day Selection
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(getDaysOfWeek()) { day ->
                    DayChip(
                        day = day,
                        isSelected = selectedDay == day.dayOfWeek,
                        onClick = { selectedDay = day.dayOfWeek }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Schedule List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                val filteredSchedules = schedules.filter { it.dayOfWeek == selectedDay }
                    .sortedBy { it.startTime }
                if (filteredSchedules.isEmpty()) {
                    item {
                        EmptySchedule()
                    }
                } else {
                    items(filteredSchedules) { schedule ->
                        ClassScheduleCard(schedule = schedule)
                    }
                }
            }
        }

        if (showAddClassDialog) {
            AddClassDialog(
                onDismiss = { showAddClassDialog = false },
                onClassAdded = { newClass ->
                    schedules.add(newClass)
                    showAddClassDialog = false
                }
            )
        }
    }
}

@Composable
fun DayChip(
    day: DayInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { 
            Text(
                text = day.shortName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF07090C)
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color(0xFF1B263B),
            labelColor = Color(0xFFAECBFA),
            selectedContainerColor = Color(0xFFAECBFA),
            selectedLabelColor = Color(0xFF0D1B2A)
        )
    )
}

@Composable
fun ClassScheduleCard(schedule: ClassSchedule) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B263B)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = schedule.subjectName,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFAECBFA)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = schedule.subjectCode,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFAECBFA).copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Room",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFAECBFA)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = schedule.roomNumber,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFAECBFA)
                    )
                }
                Text(
                    text = "${schedule.startTime} - ${schedule.endTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFAECBFA)
                )
            }
        }
    }
}

@Composable
fun EmptySchedule() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = Color(0xFFAECBFA)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No classes scheduled for this day",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFFAECBFA).copy(alpha = 0.7f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClassDialog(
    onDismiss: () -> Unit,
    onClassAdded: (ClassSchedule) -> Unit
) {
    var subjectName by remember { mutableStateOf("") }
    var subjectCode by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf(Calendar.MONDAY) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Class") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF0D1B2A)
                ) {
                    OutlinedTextField(
                        value = subjectName,
                        onValueChange = { subjectName = it },
                        label = { Text("Subject Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color(0xFFAECBFA),
                            unfocusedTextColor = Color(0xFFAECBFA),
                            cursorColor = Color(0xFFAECBFA),
                            focusedContainerColor = Color(0xFF1B263B),
                            unfocusedContainerColor = Color(0xFF1B263B),
                            focusedIndicatorColor = Color(0xFFAECBFA),
                            unfocusedIndicatorColor = Color(0xFFAECBFA).copy(alpha = 0.7f)
                        )
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF0D1B2A)
                ) {
                    OutlinedTextField(
                        value = subjectCode,
                        onValueChange = { subjectCode = it },
                        label = { Text("Subject Code") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color(0xFFAECBFA),
                            unfocusedTextColor = Color(0xFFAECBFA),
                            cursorColor = Color(0xFFAECBFA),
                            focusedContainerColor = Color(0xFF1B263B),
                            unfocusedContainerColor = Color(0xFF1B263B),
                            focusedIndicatorColor = Color(0xFFAECBFA),
                            unfocusedIndicatorColor = Color(0xFFAECBFA).copy(alpha = 0.7f)
                        )
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF0D1B2A)
                ) {
                    OutlinedTextField(
                        value = roomNumber,
                        onValueChange = { roomNumber = it },
                        label = { Text("Room Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color(0xFFAECBFA),
                            unfocusedTextColor = Color(0xFFAECBFA),
                            cursorColor = Color(0xFFAECBFA),
                            focusedContainerColor = Color(0xFF1B263B),
                            unfocusedContainerColor = Color(0xFF1B263B),
                            focusedIndicatorColor = Color(0xFFAECBFA),
                            unfocusedIndicatorColor = Color(0xFFAECBFA).copy(alpha = 0.7f)
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF0D1B2A)
                    ) {
                        OutlinedTextField(
                            value = startTime,
                            onValueChange = { startTime = it },
                            label = { Text("Start Time") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("HH:MM") },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color(0xFFAECBFA),
                                unfocusedTextColor = Color(0xFFAECBFA),
                                cursorColor = Color(0xFFAECBFA),
                                focusedContainerColor = Color(0xFF1B263B),
                                unfocusedContainerColor = Color(0xFF1B263B),
                                focusedIndicatorColor = Color(0xFFAECBFA),
                                unfocusedIndicatorColor = Color(0xFFAECBFA).copy(alpha = 0.7f)
                            )
                        )
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF0D1B2A)
                    ) {
                        OutlinedTextField(
                            value = endTime,
                            onValueChange = { endTime = it },
                            label = { Text("End Time") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("HH:MM") },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color(0xFFAECBFA),
                                unfocusedTextColor = Color(0xFFAECBFA),
                                cursorColor = Color(0xFFAECBFA),
                                focusedContainerColor = Color(0xFF1B263B),
                                unfocusedContainerColor = Color(0xFF1B263B),
                                focusedIndicatorColor = Color(0xFFAECBFA),
                                unfocusedIndicatorColor = Color(0xFFAECBFA).copy(alpha = 0.7f)
                            )
                        )
                    }
                }

                // Day selection chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(getDaysOfWeek()) { day ->
                        FilterChip(
                            selected = selectedDay == day.dayOfWeek,
                            onClick = { selectedDay = day.dayOfWeek },
                            label = { Text(day.shortName) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (subjectName.isNotBlank() && subjectCode.isNotBlank() && 
                        roomNumber.isNotBlank() && startTime.isNotBlank() && 
                        endTime.isNotBlank()
                    ) {
                        onClassAdded(
                            ClassSchedule(
                                id = UUID.randomUUID().toString(),
                                subjectName = subjectName,
                                subjectCode = subjectCode,
                                facultyId = "F001",
                                roomNumber = roomNumber,
                                dayOfWeek = selectedDay,
                                startTime = startTime,
                                endTime = endTime,
                                semester = 1,
                                year = Calendar.getInstance().get(Calendar.YEAR),
                                department = "Computer Science",
                                yearOfStudy = 2
                            )
                        )
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

data class DayInfo(
    val dayOfWeek: Int,
    val shortName: String,
    val fullName: String
)

private fun getDaysOfWeek(): List<DayInfo> = listOf(
    DayInfo(Calendar.SUNDAY, "Sun", "Sunday"),
    DayInfo(Calendar.MONDAY, "Mon", "Monday"),
    DayInfo(Calendar.TUESDAY, "Tue", "Tuesday"),
    DayInfo(Calendar.WEDNESDAY, "Wed", "Wednesday"),
    DayInfo(Calendar.THURSDAY, "Thu", "Thursday"),
    DayInfo(Calendar.FRIDAY, "Fri", "Friday"),
    DayInfo(Calendar.SATURDAY, "Sat", "Saturday")
) 