package com.example.misachat.domain.useCase

import com.example.misachat.data.network.MessageService
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messageService: MessageService
) {
    suspend operator fun invoke(chatId: String): CollectionReference {
        return messageService.getAllMessages(chatId)
    }
}