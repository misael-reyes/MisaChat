package com.example.misachat.domain.useCase

import com.example.misachat.data.network.UserService
import com.example.misachat.domain.model.Chat
import javax.inject.Inject

class SetUserUseCase @Inject constructor(
    private val userService: UserService
) {
    suspend operator fun invoke(user: String, chat: Chat) {
        userService.setUser(user, chat)
    }
}