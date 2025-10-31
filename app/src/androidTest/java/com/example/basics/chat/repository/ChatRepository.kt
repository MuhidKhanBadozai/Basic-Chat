package com.example.basics.chat.repository

import com.example.basics.chat.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await

class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val messagesCollection = firestore.collection("chat_messages")

    // ✅ Real-time listener for all messages
    val messages: Flow<List<Message>> = callbackFlow {
        val listener = messagesCollection
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val msgs = snapshot?.documents?.mapNotNull { it.toObject(Message::class.java) } ?: emptyList()
                trySend(msgs)
            }

        awaitClose { listener.remove() }
    }

    // ✅ Send message (auto-creates collection)
    suspend fun sendMessage(message: Message) {
        try {
            val data = hashMapOf(
                "sender" to message.sender,
                "text" to message.text,
                "timestamp" to message.timestamp
            )
            messagesCollection.add(data).await()
            println("✅ Message sent to Firestore: $data")
        } catch (e: Exception) {
            println("❌ Firestore send failed: ${e.message}")
            e.printStackTrace()
        }
    }
}
