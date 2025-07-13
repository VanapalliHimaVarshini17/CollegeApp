package com.nsrit.CollegeApp.model

data class Quiz(
    val id: String,
    val subject: String,
    val title: String,
    val questions: List<QuizQuestion>
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
) 