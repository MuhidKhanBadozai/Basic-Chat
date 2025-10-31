package com.example.basics.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basics.chat.model.Message
import com.example.basics.chat.repository.ChatRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository = ChatRepository()
) : ViewModel() {

    // ✅ Real-time messages from Firestore
    val messages: StateFlow<List<Message>> =
        repository.messages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ✅ Called when Send button pressed
    fun sendMessage(sender: String, text: String) {
        if (text.isBlank()) return

        val msg = Message(
            sender = sender,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.sendMessage(msg)
        }
    }

    // ✅ Optional clear all messages
//    fun clearChat() {
//        viewModelScope.launch {
//            repository.clear()
//        }
//    }
}
