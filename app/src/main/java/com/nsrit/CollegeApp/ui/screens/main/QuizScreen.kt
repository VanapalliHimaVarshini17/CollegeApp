package com.nsrit.CollegeApp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nsrit.CollegeApp.model.Quiz
import com.nsrit.CollegeApp.viewmodel.QuizViewModel
import androidx.compose.ui.graphics.Color
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val quizzes by viewModel.quizzes.collectAsState()
    val selectedQuiz by viewModel.selectedQuiz.collectAsState()
    val answers by viewModel.answers.collectAsState()
    val showResult by viewModel.showResult.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quizzes") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFFAECBFA))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)) {
            when {
                selectedQuiz == null -> QuizListView(quizzes, onQuizSelected = viewModel::selectQuiz)
                showResult -> QuizResultView(selectedQuiz!!, answers, onBack = viewModel::resetQuiz)
                else -> QuizAttemptView(selectedQuiz!!, answers, onAnswer = viewModel::answerQuestion, onSubmit = viewModel::submitQuiz)
            }
        }
    }
}

@Composable
private fun QuizListView(quizzes: List<Quiz>, onQuizSelected: (Quiz) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Available Quizzes", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(quizzes) { quiz ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onQuizSelected(quiz) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(quiz.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Subject: ${quiz.subject}", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFAECBFA))
                        Text("Questions: ${quiz.questions.size}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFAECBFA))
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizAttemptView(
    quiz: Quiz,
    answers: Map<String, Int>,
    onAnswer: (String, Int) -> Unit,
    onSubmit: () -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val question = quiz.questions[currentQuestionIndex]
    val selectedAnswer = answers[question.id]

    // Timer state
    var timeLeft by remember { mutableStateOf(300) } // 5 minutes in seconds
    var timerRunning by remember { mutableStateOf(true) }

    // Timer effect
    LaunchedEffect(timerRunning) {
        while (timerRunning && timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft == 0 && timerRunning) {
            timerRunning = false
            onSubmit()
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val quizActive = timerRunning && timeLeft > 0

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        // Timer at the top
        Text("Time Left: $timeFormatted", style = MaterialTheme.typography.titleMedium, color = Color(0xFFD32F2F), modifier = Modifier.padding(bottom = 8.dp))
        Text(quiz.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp), color = Color.White)
        Text("Question ${currentQuestionIndex + 1} of ${quiz.questions.size}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp), color = Color.White)
        Text(question.question, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 16.dp), color = Color.White)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            question.options.forEachIndexed { idx, option ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedAnswer == idx,
                        onClick = { if (quizActive) onAnswer(question.id, idx) },
                        enabled = quizActive,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFFAECBFA),
                            unselectedColor = Color(0xFFAECBFA).copy(alpha = 0.7f)
                        )
                    )
                    Text(option, color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { if (currentQuestionIndex > 0 && quizActive) currentQuestionIndex-- },
                enabled = currentQuestionIndex > 0 && quizActive,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAECBFA))
            ) { Text("Previous", color = Color(0xFF0D1B2A)) }
            if (currentQuestionIndex < quiz.questions.size - 1) {
                Button(
                    onClick = { if (currentQuestionIndex < quiz.questions.size - 1 && quizActive) currentQuestionIndex++ },
                    enabled = currentQuestionIndex < quiz.questions.size - 1 && quizActive,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAECBFA))
                ) { Text("Next", color = Color(0xFF0D1B2A)) }
            } else {
                Button(
                    onClick = { if (quizActive) onSubmit() },
                    enabled = answers.size == quiz.questions.size && quizActive,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAECBFA))
                ) { Text("Submit Quiz", color = Color(0xFF0D1B2A)) }
            }
        }
        if (!quizActive) {
            Text("Time's up! Quiz auto-submitted.", color = Color(0xFFD32F2F), modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
private fun QuizResultView(
    quiz: Quiz,
    answers: Map<String, Int>,
    onBack: () -> Unit
) {
    val correctCount = quiz.questions.count { q -> answers[q.id] == q.correctAnswerIndex }
    val total = quiz.questions.size
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Quiz Result", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp), color = Color(0xFFAECBFA))
        Text("Score: $correctCount / $total", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 16.dp), color = Color(0xFFAECBFA))
        Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAECBFA))) { Text("Back to Quizzes", color = Color(0xFF0D1B2A)) }
    }
} 