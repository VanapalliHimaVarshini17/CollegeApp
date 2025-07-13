package com.nsrit.CollegeApp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nsrit.CollegeApp.data.model.Event
import com.nsrit.CollegeApp.data.model.EventCategory
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavController) {
    var selectedCategory by remember { mutableStateOf<EventCategory?>(null) }
    var showAddEventDialog by remember { mutableStateOf(false) }

    // Sample events data
    val events = remember {
        mutableStateListOf(
            Event(
                id = "1",
                title = "Tech Fest 2024",
                description = "Annual technology festival with workshops and competitions",
                date = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000,
                location = "Main Auditorium",
                organizer = "Computer Science Department",
                category = EventCategory.CULTURAL
            ),
            Event(
                id = "2",
                title = "Career Fair",
                description = "Connect with top companies and explore job opportunities",
                date = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000,
                location = "Seminar Hall",
                organizer = "Placement Cell",
                category = EventCategory.ACADEMIC
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Events") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFFAECBFA))
                    }
                },
                actions = {
                    IconButton(onClick = { showAddEventDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Event", tint = Color(0xFFAECBFA))
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
            // Category filters
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All") },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF1B263B),
                            labelColor = Color(0xFFAECBFA),
                            selectedContainerColor = Color(0xFFAECBFA),
                            selectedLabelColor = Color(0xFF0D1B2A)
                        )
                    )
                }
                items(EventCategory.values()) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF1B263B),
                            labelColor = Color(0xFFAECBFA),
                            selectedContainerColor = Color(0xFFAECBFA),
                            selectedLabelColor = Color(0xFF0D1B2A)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Events list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                val filteredEvents = events.filter { 
                    selectedCategory == null || it.category == selectedCategory 
                }

                if (filteredEvents.isEmpty()) {
                    item {
                        EmptyEventsMessage()
                    }
                } else {
                    items(filteredEvents) { event ->
                        EventCard(event = event)
                    }
                }
            }
        }
    }

    if (showAddEventDialog) {
        AddEventDialog(
            onDismiss = { showAddEventDialog = false },
            onEventAdded = { newEvent ->
                events.add(newEvent)
                showAddEventDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCard(event: Event) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                FilterChip(
                    selected = false,
                    onClick = { },
                    label = { Text(event.category.name, color = Color.White) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFF1B263B),
                        labelColor = Color.White,
                        selectedContainerColor = Color(0xFFAECBFA),
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFAECBFA)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFAECBFA).copy(alpha = 0.7f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Date",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFAECBFA)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            .format(Date(event.date)),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFAECBFA).copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyEventsMessage() {
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
            text = "No events found",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFFAECBFA).copy(alpha = 0.7f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onEventAdded: (Event) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(EventCategory.GENERAL) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Event") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    color = Color(0xFF0D1B2A)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Event Title") },
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
                    color = Color(0xFF0D1B2A)
                ) {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 3,
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
                    color = Color(0xFF0D1B2A)
                ) {
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
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

                // Category selection
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(EventCategory.values()) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFF1B263B),
                                labelColor = Color(0xFFAECBFA),
                                selectedContainerColor = Color(0xFFAECBFA),
                                selectedLabelColor = Color(0xFF0D1B2A)
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank() && location.isNotBlank()) {
                        onEventAdded(
                            Event(
                                id = UUID.randomUUID().toString(),
                                title = title,
                                description = description,
                                location = location,
                                category = selectedCategory,
                                date = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000, // 7 days from now
                                organizer = "Current User" // TODO: Get from user session
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