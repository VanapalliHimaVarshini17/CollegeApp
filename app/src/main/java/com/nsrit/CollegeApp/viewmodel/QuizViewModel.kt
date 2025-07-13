package com.nsrit.CollegeApp.viewmodel

import androidx.lifecycle.ViewModel
import com.nsrit.CollegeApp.model.Quiz
import com.nsrit.CollegeApp.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.nsrit.CollegeApp.repository.QuizSubmission
import com.google.firebase.auth.FirebaseAuth

class QuizViewModel : ViewModel() {
    private val repository = QuizRepository()

    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes.asStateFlow()

    private val _selectedQuiz = MutableStateFlow<Quiz?>(null)
    val selectedQuiz: StateFlow<Quiz?> = _selectedQuiz.asStateFlow()

    // Map of questionId to selected answer index
    private val _answers = MutableStateFlow<Map<String, Int>>(emptyMap())
    val answers: StateFlow<Map<String, Int>> = _answers.asStateFlow()

    private val _showResult = MutableStateFlow(false)
    val showResult: StateFlow<Boolean> = _showResult.asStateFlow()

    init {
        loadQuizzes()
    }

    fun loadQuizzes() {
        _quizzes.value = repository.getQuizzes()
    }

    fun selectQuiz(quiz: Quiz) {
        _selectedQuiz.value = quiz
        _answers.value = emptyMap()
        _showResult.value = false
    }

    fun answerQuestion(questionId: String, answerIndex: Int) {
        _answers.value = _answers.value.toMutableMap().apply { put(questionId, answerIndex) }
    }

    fun submitQuiz() {
        val quiz = _selectedQuiz.value ?: return
        val answers = _answers.value
        val score = quiz.questions.count { q -> answers[q.id] == q.correctAnswerIndex }

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid ?: "anonymous"
        val userEmail = user?.email ?: "unknown"
        val userName = user?.displayName ?: "unknown"

        viewModelScope.launch {
            repository.submitQuizToFirestore(
                QuizSubmission(
                    userId = userId,
                    userEmail = userEmail,
                    userName = userName,
                    quizId = quiz.id,
                    answers = answers,
                    score = score
                )
            )
            _showResult.value = true
        }
    }

    fun resetQuiz() {
        _selectedQuiz.value = null
        _answers.value = emptyMap()
        _showResult.value = false
    }

    // For backend: add suspend fun loadQuizzesFromBackend() { ... }
} 