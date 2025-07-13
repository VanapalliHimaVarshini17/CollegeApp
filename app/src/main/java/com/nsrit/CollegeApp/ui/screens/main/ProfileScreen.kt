package com.nsrit.CollegeApp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nsrit.CollegeApp.model.User
import com.nsrit.CollegeApp.model.UserRole
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF0D1B2A), // Dark Blue Background
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFFAECBFA))
                    }
                },
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = Color(0xFFAECBFA))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D1B2A) // Dark Blue Background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.large,
                color = Color(0xFF1B263B) // Medium Blue Surface
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.padding(16.dp),
                    tint = Color.White
                )
            }

            // User Details
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1B263B) // Medium Blue Surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    ProfileDetailItem("Name", user.name)
                    ProfileDetailItem("Email", user.email)
                    ProfileDetailItem("Role", user.role.name)
                    ProfileDetailItem("Current Year", user.yearOfStudy.toString())
                    ProfileDetailItem("Department", user.department)
                    ProfileDetailItem("Roll Number", user.rollNumber)
                    ProfileDetailItem("Gender", user.gender)
                    ProfileDetailItem("Nationality", user.nationality)
                    ProfileDetailItem("Current Semester", user.currentSemester.toString())
                    ProfileDetailItem("Date of Birth", user.dateOfBirth)
                    ProfileDetailItem("Phone Number", user.phoneNumber)
                    ProfileDetailItem("Backlogs", user.backlogs.toString())
                }
            }

            // Additional Information Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1B263B) // Medium Blue Surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Additional Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = Color(0xFFAECBFA)
                    )
                    
                    ProfileDetailItem("Member Since", formatDate(user.createdAt))
                    if (user.role == UserRole.STUDENT) {
                        ProfileDetailItem("Current Semester", "Semester ${(user.yearOfStudy - 1) * 2 + 1}")
                    }
                }
            }

            // Past Semester Results section: show up to the current semester, fetch from DB or show '-'
            val totalSemesters = (user.yearOfStudy - 1) * 2 + user.currentSemester
            if (totalSemesters > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Past Semester Results", style = MaterialTheme.typography.titleMedium, color = Color(0xFFAECBFA))
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (i in 1..totalSemesters) {
                        val grade = user.pastSemestersPerformances.find { it.semester == i.toString() }?.grade ?: "-"
                        Text("Semester $i : $grade", color = Color.White)
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        EditProfileDialog(user = user, onDismiss = { showEditDialog = false }, onSave = { updatedUser ->
            viewModel.updateProfile(updatedUser)
            showEditDialog = false
        })
    }
}

@Composable
private fun ProfileDetailItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 2.dp),
            color = Color.White
        )
    }
}

@Composable
fun EditProfileDialog(user: User, onDismiss: () -> Unit, onSave: (User) -> Unit) {
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var password by remember { mutableStateOf(user.password) }
    var yearOfStudy by remember { mutableStateOf(user.yearOfStudy.toString()) }
    var department by remember { mutableStateOf(user.department) }
    var rollNumber by remember { mutableStateOf(user.rollNumber) }
    var gender by remember { mutableStateOf(user.gender) }
    var nationality by remember { mutableStateOf(user.nationality) }
    var currentSemester by remember { mutableStateOf(user.currentSemester.toString()) }
    var dateOfBirth by remember { mutableStateOf(user.dateOfBirth) }
    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }
    var pastSemesters by remember { mutableStateOf(user.pastSemestersPerformances.toMutableList()) }
    var newSemester by remember { mutableStateOf("") }
    var newGrade by remember { mutableStateOf("") }

    val textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedContainerColor = Color(0xFF1B263B),
        unfocusedContainerColor = Color(0xFF1B263B),
        focusedIndicatorColor = Color(0xFFAECBFA),
        unfocusedIndicatorColor = Color(0xFFAECBFA).copy(alpha = 0.7f),
        cursorColor = Color(0xFFAECBFA)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {}, // Non-editable
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                    enabled = false
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = {}, // Non-editable
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                    enabled = false
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {}, // Non-editable
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                    enabled = false
                )
                OutlinedTextField(
                    value = yearOfStudy,
                    onValueChange = { yearOfStudy = it },
                    label = { Text("Current Year") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                OutlinedTextField(
                    value = department,
                    onValueChange = { department = it },
                    label = { Text("Department") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                OutlinedTextField(
                    value = rollNumber,
                    onValueChange = { rollNumber = it },
                    label = { Text("Roll Number") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("Gender") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                OutlinedTextField(
                    value = nationality,
                    onValueChange = { nationality = it },
                    label = { Text("Nationality") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                OutlinedTextField(
                    value = currentSemester,
                    onValueChange = { currentSemester = it },
                    label = { Text("Current Semester") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = { Text("Date of Birth") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Past Semester Performances", style = MaterialTheme.typography.titleMedium, color = Color(0xFFAECBFA))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pastSemesters.forEachIndexed { index, perf ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = perf.semester,
                                onValueChange = { newValue ->
                                    pastSemesters[index] = perf.copy(semester = newValue)
                                },
                                label = { Text("Semester") },
                                modifier = Modifier.weight(1f),
                                colors = textFieldColors
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = perf.grade,
                                onValueChange = { newValue ->
                                    pastSemesters[index] = perf.copy(grade = newValue)
                                },
                                label = { Text("Grade") },
                                modifier = Modifier.weight(1f),
                                colors = textFieldColors
                            )
                            IconButton(onClick = { pastSemesters.removeAt(index) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Remove", tint = Color.Red)
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newSemester,
                            onValueChange = { newSemester = it },
                            label = { Text("Semester") },
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = newGrade,
                            onValueChange = { newGrade = it },
                            label = { Text("Grade") },
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors
                        )
                        IconButton(onClick = {
                            if (newSemester.isNotBlank() && newGrade.isNotBlank()) {
                                pastSemesters.add(com.nsrit.CollegeApp.model.SemesterPerformance(newSemester, newGrade))
                                newSemester = ""
                                newGrade = ""
                            }
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Add", tint = Color.Green)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    user.copy(
                        name = name,
                        password = password,
                        yearOfStudy = yearOfStudy.toIntOrNull() ?: 1,
                        department = department,
                        rollNumber = rollNumber,
                        gender = gender,
                        nationality = nationality,
                        currentSemester = currentSemester.toIntOrNull() ?: 1,
                        dateOfBirth = dateOfBirth,
                        phoneNumber = phoneNumber,
                        pastSemestersPerformances = pastSemesters.toList()
                    )
                )
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun DropdownField(label: String, value: String, options: List<String>, onValueChange: (String) -> Unit, colors: TextFieldColors) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {}, // Prevent manual typing
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            readOnly = true,
            colors = colors
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    if (timestamp == 0L) return "N/A"
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
} 