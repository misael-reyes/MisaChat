package com.example.misachat.iu.listOfChats

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misachat.domain.model.Chat
import com.example.misachat.domain.useCase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ListOfChatsViewModel @Inject constructor(
    private val getAllChatsUserUseCase: GetAllChatsUserUseCase,
    private val setUserUseCase: SetUserUseCase,
    private val insertChatUseCase: InsertChatUseCase,
    private val setOtherUserUseCase: SetOtherUserUseCase,
    private val getChatsUseCase: GetChatsUseCase
): ViewModel() {

    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> get() = _chats

    private val _isAdded = MutableLiveData<Chat>()
    val isAdded: LiveData<Chat> get() = _isAdded

    fun getAllChatsUser(email: String) {
        viewModelScope.launch {
            getAllChatsUserUseCase(email).addOnSuccessListener {
                _chats.value = it.toObjects(Chat::class.java)
            }
            // esto es para escuchar cuando hayan agregado un nuevo chat (actualizacion en tiempo real)
            getChatsUseCase(email).addSnapshotListener{ chats, error ->
                if (error == null) {
                    chats?.let {
                        _chats.value = it.toObjects(Chat::class.java)
                    }
                }
            }
        }

    }

    fun newChat(user: String, otherUser: String) {
        // creamos un nuevo usuario para poder chatear
        val chatId = UUID.randomUUID().toString()
        val users = listOf(user, otherUser)

        val chat = Chat(
            id = chatId,
            name = "Chat con $otherUser",
            users = users
        )

        // lo damos de alta en firebase

        // primero agregamos nuestro chat dentro de la coleccion chats
        viewModelScope.launch {
            insertChatUseCase(chat)
            setUserUseCase(user, chat)
            setOtherUserUseCase(otherUser, chat)
        }

        _isAdded.value = chat
    }
}