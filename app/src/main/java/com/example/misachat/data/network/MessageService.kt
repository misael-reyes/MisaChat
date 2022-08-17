package com.example.misachat.data.network

import com.example.misachat.domain.model.Message
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageService @Inject constructor(
    private val chatService: ChatService
) {

    private val MESSAGE_COLLECTION = "messages"

    suspend fun getAllMessages(chatId: String): CollectionReference {
        return withContext(Dispatchers.IO) {
            chatService.getChatById(chatId).collection(MESSAGE_COLLECTION)
        }
    }

    suspend fun setMessage(message: Message, chatId: String) {
        return withContext(Dispatchers.IO) {
            chatService.getChatById(chatId).collection(MESSAGE_COLLECTION).document().set(message)
        }
    }
}