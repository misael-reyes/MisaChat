package com.example.misachat.data.network

import com.example.misachat.domain.model.Chat
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatService @Inject constructor(
    private val firebase: FirebaseClient
) {

    private val CHAT_COLLECTION = "chats"

    suspend fun getAllChatsUser(user: DocumentReference): Task<QuerySnapshot> {
        return withContext(Dispatchers.IO) {
            user.collection(CHAT_COLLECTION).get()
        }
    }

    suspend fun getAllChats(user: DocumentReference): CollectionReference {
        return withContext(Dispatchers.IO) {
            user.collection(CHAT_COLLECTION)
        }
    }

    suspend fun insertChat(chat: Chat) {
        return withContext(Dispatchers.IO) {
            firebase.db.collection(CHAT_COLLECTION).document(chat.id).set(chat)
        }
    }

    suspend fun getChatById(chatId: String): DocumentReference {
        return withContext(Dispatchers.IO) {
            firebase.db.collection(CHAT_COLLECTION).document(chatId)
        }
    }
}