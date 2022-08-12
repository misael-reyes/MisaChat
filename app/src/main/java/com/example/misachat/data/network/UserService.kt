package com.example.misachat.data.network

import com.example.misachat.domain.model.Chat
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserService @Inject constructor(
    private val firebase: FirebaseClient
) {

    private val USER_COLLECTION = "users"

    suspend fun getUserByEmail(email: String): DocumentReference {
        return withContext(Dispatchers.IO) {
            firebase.db.collection(USER_COLLECTION).document(email)
        }
    }

    suspend fun setUser(user: String, chat: Chat) {
        return withContext(Dispatchers.IO) {
            firebase.db.collection(USER_COLLECTION).document(user).collection("chats")
                .document(chat.id)
                .set(chat)
        }
    }

    suspend fun setOtherUser(otherUser: String, chat: Chat) {
        return withContext(Dispatchers.IO) {
            firebase.db.collection(USER_COLLECTION).document(otherUser).collection("chats")
                .document(chat.id).set(chat)
        }
    }
}