package com.example.misachat.iu.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misachat.domain.model.Message
import com.example.misachat.domain.useCase.GetMessagesUseCase
import com.example.misachat.domain.useCase.SetMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.firestore.Query

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val setMessageUseCase: SetMessageUseCase
): ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    fun getMessages(chatId: String) {
        viewModelScope.launch {
            getMessagesUseCase(chatId).orderBy("dob", Query.Direction.ASCENDING)
                .get().addOnSuccessListener {
                _messages.value = it.toObjects(Message::class.java)
            }

            getMessagesUseCase(chatId).orderBy("dob", Query.Direction.ASCENDING)
                .addSnapshotListener { messages, error ->
                if (error == null) {
                    messages?.let {
                        _messages.value = it.toObjects(Message::class.java)
                    }
                }
            }
        }
    }

    fun sendMessage(message: Message, chatId: String) {
        viewModelScope.launch {
            setMessageUseCase(message, chatId)
        }
    }
}