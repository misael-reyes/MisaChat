package com.example.misachat.data.network

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService @Inject constructor(
    private val firebase: FirebaseClient
) {

    suspend fun registerUserWithEmailAndPassword(email:String, password: String): Task<AuthResult?> {
        return withContext(Dispatchers.IO) {
            firebase.auth.createUserWithEmailAndPassword(email, password)
        }
    }

    suspend fun loginWithEmailAndPassword(email: String, password: String): Task<AuthResult?> {
        return withContext(Dispatchers.IO) {
            firebase.auth.signInWithEmailAndPassword(email, password)
        }
    }

    suspend fun loginWithGoogle(credential: AuthCredential): Task<AuthResult?> {
        return withContext(Dispatchers.IO) {
            firebase.auth.signInWithCredential(credential)
        }
    }
}