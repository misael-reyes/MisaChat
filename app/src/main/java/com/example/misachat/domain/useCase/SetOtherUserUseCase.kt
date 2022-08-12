package com.example.misachat.domain.useCase

import com.example.misachat.data.network.UserService
import com.example.misachat.domain.model.Chat
import javax.inject.Inject

class SetOtherUserUseCase @Inject constructor(
    private val userService: UserService
) {
    suspend operator fun invoke(otherUser: String, chat: Chat) {
        userService.setOtherUser(otherUser, chat)
    }
}