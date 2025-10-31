package com.example.basics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.basics.chat.viewmodel.ChatViewModel
import com.example.basics.chat.view.navigation.AppNavHost
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        // Test Firestore connection
        val db = FirebaseFirestore.getInstance()
        db.collection("connection_test")
            .add(hashMapOf("connected" to true, "timestamp" to System.currentTimeMillis()))

        setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController, chatViewModel = chatViewModel)
        }
    }
}
