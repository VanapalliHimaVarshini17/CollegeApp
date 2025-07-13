package com.nsrit.CollegeApp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.FirebaseFirestore
import com.nsrit.CollegeApp.model.Feedback
import com.nsrit.CollegeApp.model.FeedbackType
import com.nsrit.CollegeApp.model.FeedbackStatus
import java.util.UUID
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(navController: NavController) {
    val departments = listOf("Computer Science", "Electronics", "Mechanical", "Civil", "Electrical")
    var selectedDepartment by remember { mutableStateOf(departments.first()) }
    var academicsFeedback by remember { mutableStateOf("") }
    var facultyFeedback by remember { mutableStateOf("") }
    var collegeFeedback by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()
    var isSubmitting by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color(0xFFAECBFA))
            }

            Text(
                text = "Feedback",
                style = MaterialTheme.typography.headlineMedium
            )

            Box(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Department Dropdown
        Text("Select Department", style = MaterialTheme.typography.bodyMedium)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedDepartment,
                onValueChange = { selectedDepartment = it },
                label = { Text("Select Department") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color(0xFFAECBFA),
                    unfocusedTextColor = Color(0xFFAECBFA),
                    focusedContainerColor = Color(0xFF1B263B),
                    unfocusedContainerColor = Color(0xFF1B263B),
                    focusedIndicatorColor = Color(0xFFAECBFA),
                    unfocusedIndicatorColor = Color(0xFFAECBFA).copy(alpha = 0.7f),
                    cursorColor = Color(0xFFAECBFA)
                ),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                departments.forEach { dept ->
                    DropdownMenuItem(
                        text = { Text(dept, color = Color(0xFF0D1B2A)) },
                        onClick = {
                            selectedDepartment = dept
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Feedback Form
        OutlinedTextField(
            value = academicsFeedback,
            onValueChange = { academicsFeedback = it },
            label = { Text("Academics Feedback") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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
        OutlinedTextField(
            value = facultyFeedback,
            onValueChange = { facultyFeedback = it },
            label = { Text("Faculty Feedback") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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
        OutlinedTextField(
            value = collegeFeedback,
            onValueChange = { collegeFeedback = it },
            label = { Text("College Feedback") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                    isSubmitting = true
                    val feedback = Feedback(
                        id = UUID.randomUUID().toString(),
                        userId = "", // Set userId if available
                        type = FeedbackType.GENERAL, // You can customize this
                        title = "Feedback for $selectedDepartment",
                        description = "Academics: $academicsFeedback\nFaculty: $facultyFeedback\nCollege: $collegeFeedback",
                        rating = 0,
                        status = FeedbackStatus.PENDING,
                        response = null,
                        respondedBy = null,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    db.collection("feedback")
                        .document(feedback.id)
                        .set(feedback)
                        .addOnSuccessListener {
                            Log.d("FeedbackScreen", "Feedback submitted successfully: ${feedback.id}")
                            Toast.makeText(context, "Feedback submitted!", Toast.LENGTH_SHORT).show()
                            isSubmitting = false
                showSuccessDialog = true
                academicsFeedback = ""
                facultyFeedback = ""
                collegeFeedback = ""
                        }
                        .addOnFailureListener { e ->
                            Log.e("FeedbackScreen", "Failed to submit feedback: ${e.localizedMessage}")
                            Toast.makeText(context, "Failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                            isSubmitting = false
                            scope.launch {
                                snackbarHostState.showSnackbar("Failed to submit feedback: ${e.localizedMessage}")
                            }
                        }
            },
            modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAECBFA)),
                enabled = !isSubmitting
        ) {
                Text(if (isSubmitting) "Submitting..." else "Submit Feedback", color = Color(0xFF0D1B2A))
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Thank You!") },
            text = { Text("Your feedback for $selectedDepartment has been submitted.") },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
} 