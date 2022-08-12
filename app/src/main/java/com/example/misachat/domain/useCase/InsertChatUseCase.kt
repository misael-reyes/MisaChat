package com.example.misachat.domain.useCase

import com.example.misachat.data.network.ChatService
import com.example.misachat.data.network.UserService
import com.example.misachat.domain.model.Chat
import javax.inject.Inject

class InsertChatUseCase @Inject constructor(
    private val chatService: ChatService,
    private val userService: UserService
) {
    suspend operator fun invoke(chat: Chat) {
        chatService.insertChat(chat)
    }
}