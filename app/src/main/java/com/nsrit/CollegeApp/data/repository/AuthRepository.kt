package com.nsrit.CollegeApp.data.repository

import com.nsrit.CollegeApp.model.User
import com.nsrit.CollegeApp.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun signUp(email: String, password: String, name: String): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("User creation failed")
            
            val user = User(
                id = firebaseUser.uid,
                email = email,
                name = name,
                password = "*".repeat(password.length), // Masked password for display only
                role = UserRole.STUDENT,
                department = "",
                rollNumber = "",
                yearOfStudy = 1,
                currentSemester = 1,
                gender = "",
                nationality = "",
                dateOfBirth = "",
                backlogs = 0,
                pastSemestersPerformances = emptyList(),
                designation = "",
                phoneNumber = ""
            )
            
            // Save user data to Firestore
            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(user)
                .await()
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Sign in failed")
            
            // Get user data from Firestore
            val userDoc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
            
            val user = userDoc.toObject(User::class.java)
                ?: throw Exception("User data not found")
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun getUserData(uid: String): User {
        val userDoc = firestore.collection("users")
            .document(uid)
            .get()
            .await()
        return userDoc.toObject(User::class.java)
            ?: throw Exception("User data not found")
    }

    suspend fun updateUserData(user: User) {
        firestore.collection("users")
            .document(user.id)
            .set(user)
            .await()
    }
} 