package com.example.misachat.domain.useCase

import com.example.misachat.data.network.MessageService
import com.example.misachat.domain.model.Message
import javax.inject.Inject

class SetMessageUseCase @Inject constructor(
    private val messageService: MessageService
) {
    suspend operator fun invoke(message: Message, chatId: String) {
        messageService.setMessage(message, chatId)
    }
}