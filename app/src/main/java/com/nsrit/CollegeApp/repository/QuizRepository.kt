package com.nsrit.CollegeApp.repository

import com.nsrit.CollegeApp.model.Quiz
import com.nsrit.CollegeApp.model.QuizQuestion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class QuizSubmission(
    val userId: String = "",
    val userEmail: String = "",
    val userName: String = "",
    val quizId: String = "",
    val answers: Map<String, Int> = emptyMap(),
    val score: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

class QuizRepository {
    // Sample quizzes
    private val quizzes = listOf(
        Quiz(
            id = "quiz1",
            subject = "Data Structures",
            title = "DSA Basics Quiz",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    question = "What is the time complexity of binary search?",
                    options = listOf("O(n)", "O(log n)", "O(n log n)", "O(1)"),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    id = "q2",
                    question = "Which data structure uses FIFO order?",
                    options = listOf("Stack", "Queue", "Tree", "Graph"),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    id = "q3",
                    question = "Which of the following is not a linear data structure?",
                    options = listOf("Array", "Linked List", "Tree", "Queue"),
                    correctAnswerIndex = 2
                ),
                QuizQuestion(
                    id = "q4",
                    question = "What is the worst-case time complexity of quicksort?",
                    options = listOf("O(n^2)", "O(n log n)", "O(log n)", "O(n)"),
                    correctAnswerIndex = 0
                ),
                QuizQuestion(
                    id = "q5",
                    question = "Which data structure is used for recursion?",
                    options = listOf("Queue", "Stack", "Array", "Graph"),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    id = "q6",
                    question = "Which traversal is used to get the contents of a binary search tree in ascending order?",
                    options = listOf("Preorder", "Inorder", "Postorder", "Level order"),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    id = "q7",
                    question = "Which of the following is not an application of stack?",
                    options = listOf("Function call management", "Expression evaluation", "Job scheduling", "Backtracking"),
                    correctAnswerIndex = 2
                ),
                QuizQuestion(
                    id = "q8",
                    question = "Which algorithm is used to find the shortest path in a graph?",
                    options = listOf("Prim's", "Kruskal's", "Dijkstra's", "Floyd's"),
                    correctAnswerIndex = 2
                ),
                QuizQuestion(
                    id = "q9",
                    question = "Which data structure is used in breadth-first search of a graph?",
                    options = listOf("Stack", "Queue", "Array", "Tree"),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    id = "q10",
                    question = "Which of the following is a non-primitive data structure?",
                    options = listOf("Integer", "Float", "Array", "Char"),
                    correctAnswerIndex = 2
                )
            )
        ),
        Quiz(
            id = "quiz2",
            subject = "Database Management",
            title = "DBMS Fundamentals Quiz",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    question = "Which SQL command is used to remove a table?",
                    options = listOf("DELETE", "DROP", "REMOVE", "TRUNCATE"),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    id = "q2",
                    question = "What does ACID stand for in databases?",
                    options = listOf("Atomicity, Consistency, Isolation, Durability", "Access, Control, Integrity, Data", "Accuracy, Consistency, Isolation, Durability", "None of the above"),
                    correctAnswerIndex = 0
                ),
                QuizQuestion(
                    id = "q3",
                    question = "Which of the following is a DDL command?",
                    options = listOf("INSERT", "UPDATE", "CREATE", "SELECT"),
                    correctAnswerIndex = 2
                ),
                QuizQuestion(
                    id = "q4",
                    question = "Which normal form eliminates transitive dependency?",
                    options = listOf("1NF", "2NF", "3NF", "BCNF"),
                    correctAnswerIndex = 2
                ),
                QuizQuestion(
                    id = "q5",
                    question = "Which of the following is not a type of join in SQL?",
                    options = listOf("INNER JOIN", "OUTER JOIN", "CROSS JOIN", "FULL JOIN"),
                    correctAnswerIndex = 3
                ),
                QuizQuestion(
                    id = "q6",
                    question = "Which key uniquely identifies a record in a table?",
                    options = listOf("Foreign Key", "Primary Key", "Candidate Key", "Super Key"),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    id = "q7",
                    question = "Which command is used to remove all records from a table but not the table itself?",
                    options = listOf("DELETE", "DROP", "TRUNCATE", "REMOVE"),
                    correctAnswerIndex = 2
                ),
                QuizQuestion(
                    id = "q8",
                    question = "Which of the following is not a valid SQL data type?",
                    options = listOf("VARCHAR", "INTEGER", "DATE", "ARRAYLIST"),
                    correctAnswerIndex = 3
                ),
                QuizQuestion(
                    id = "q9",
                    question = "Which language is used to define the structure of a database?",
                    options = listOf("DML", "DDL", "DCL", "TCL"),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    id = "q10",
                    question = "Which of the following is used to grant privileges to users in SQL?",
                    options = listOf("GRANT", "REVOKE", "ALLOW", "PERMIT"),
                    correctAnswerIndex = 0
                )
            )
        )
    )

    fun getQuizzes(): List<Quiz> = quizzes

    suspend fun fetchQuizzesFromFirestore(): List<Quiz> {
        val db = FirebaseFirestore.getInstance()
        val snapshot = db.collection("quizzes").get().await()
        return snapshot.documents.mapNotNull { it.toObject(Quiz::class.java)?.copy(id = it.id) }
    }

    suspend fun submitQuizToFirestore(submission: QuizSubmission) {
        val db = FirebaseFirestore.getInstance()
        db.collection("quiz_submissions")
            .add(submission)
            .await()
    }
} 