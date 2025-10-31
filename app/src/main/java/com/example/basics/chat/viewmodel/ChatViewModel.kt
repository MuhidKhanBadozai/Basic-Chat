package com.example.basics.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basics.chat.model.Message
import com.example.basics.chat.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class ChatViewModel(
    private val repository: ChatRepository = ChatRepository()
) : ViewModel() {

    // Timestamp for when the user entered the chat, initialized only once
    private val _lastReadTimestamp = MutableStateFlow(System.currentTimeMillis())
    val lastReadTimestamp = _lastReadTimestamp.asStateFlow()

    val messages: StateFlow<List<Message>> =
        repository.messages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    fun sendMessage(sender: String, text: String) {
        if (text.isBlank()) return
        val m = Message(sender = sender, text = text)
        repository.sendMessage(m)
        // After sending, mark all messages as read by updating the timestamp
        updateLastReadTimestamp()
    }

    fun updateLastReadTimestamp() {
        _lastReadTimestamp.value = System.currentTimeMillis()
    }
}
