package com.example.misachat.domain.useCase

import com.example.misachat.data.network.ChatService
import com.example.misachat.data.network.UserService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val userService: UserService,
    private val chatService: ChatService
) {
    suspend operator fun invoke(email: String): CollectionReference {
        val user = userService.getUserByEmail(email)
        return chatService.getAllChats(user)
    }
}