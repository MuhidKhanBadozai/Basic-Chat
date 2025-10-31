package com.example.basics.chat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.basics.chat.model.Message
import com.example.basics.chat.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(viewModel: ChatViewModel, myName: String = "User1") {
    val messages by viewModel.messages.collectAsStateWithLifecycle(initialValue = emptyList())
    var input by remember { mutableStateOf("") }

    // 🧭 Scroll control
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // 🕓 Store the last read timestamp (in real app, persist this per user)
    var lastReadTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }

    // 🔍 Find first "new" message
    val firstNewMessageIndex = remember(messages, lastReadTimestamp) {
        messages.indexOfFirst { it.timestamp > lastReadTimestamp }
    }

    // 🚀 Scroll automatically if no new messages
    LaunchedEffect(messages) {
        if (firstNewMessageIndex == -1 && messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.scrollToItem(messages.lastIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
            verticalArrangement = Arrangement.Top
        ) {
            itemsIndexed(messages) { index, msg ->

                // ✅ Show "New Messages" divider at the right place
                if (index == firstNewMessageIndex && firstNewMessageIndex != -1) {
                    NewMessageDivider()
                }

                MessageBubble(message = msg, isMine = msg.sender == myName)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Type a message") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        viewModel.sendMessage(myName, input)
                        input = ""
                        lastReadTimestamp = System.currentTimeMillis() // mark as read
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isMine: Boolean) {
    val bgColor = if (isMine) Color(0xFFD1F7C4) else Color(0xFFF0F0F0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor)
                .padding(8.dp)
        ) {
            Text(
                text = message.sender,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun NewMessageDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.Gray,
            thickness = 1.dp
        )
        Text(
            "  New Messages  ",
            color = Color.Gray,
            fontSize = 12.sp
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.Gray,
            thickness = 1.dp
        )
    }
}
