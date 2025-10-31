package com.example.basics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.basics.chat.viewmodel.ChatViewModel
import com.example.basics.chat.view.navigation.AppNavHost
import com.example.basics.utils.UpdateChecker  // ✅ import your updater
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize Firebase
        FirebaseApp.initializeApp(this)

        // ✅ Test Firestore connection (optional)
        val db = FirebaseFirestore.getInstance()
        db.collection("connection_test")
            .add(hashMapOf("connected" to true, "timestamp" to System.currentTimeMillis()))

        // ✅ Set your app content
        setContent {
            MainAppContent(chatViewModel)
        }
    }
}

@Composable
fun MainAppContent(chatViewModel: ChatViewModel) {
    val context = LocalContext.current

    // ✅ Auto-update check runs here
    UpdateChecker(context)

    val navController = rememberNavController()
    AppNavHost(navController = navController, chatViewModel = chatViewModel)
}
