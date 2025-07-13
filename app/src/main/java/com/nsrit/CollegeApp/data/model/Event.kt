package com.nsrit.CollegeApp.data.model

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: Long,
    val location: String,
    val organizer: String,
    val category: EventCategory
) 