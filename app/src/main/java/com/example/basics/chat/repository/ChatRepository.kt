package com.example.basics.chat.repository

import com.example.basics.chat.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatRepository {

    private val db = FirebaseFirestore.getInstance()
    private val messagesCollection = db.collection("messages")

    val messages: Flow<List<Message>> = callbackFlow {
        val subscription = messagesCollection
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messageList = snapshot.toObjects(Message::class.java)
                    trySend(messageList).isSuccess
                }
            }

        awaitClose { subscription.remove() }
    }

    fun sendMessage(message: Message) {
        messagesCollection.add(message)
    }

    fun clear() {
        // This is more complex with Firestore. A cloud function is a better approach for this.
    }
}
