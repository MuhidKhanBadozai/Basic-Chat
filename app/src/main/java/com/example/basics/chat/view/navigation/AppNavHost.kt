package com.example.basics.chat.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.basics.chat.view.ChatScreen
import com.example.basics.chat.view.login.LoginScreen
import com.example.basics.chat.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost(
    navController: NavHostController,
    chatViewModel: ChatViewModel
) {
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "chat" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen { username ->
                navController.navigate("chat") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        composable("chat") {
            val user = auth.currentUser
            val myName = user?.email?.substringBefore('@') ?: "UnknownUser"
            ChatScreen(viewModel = chatViewModel, myName = myName)
        }
    }
}
