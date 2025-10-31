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

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var lastReadTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }

    val firstNewMessageIndex = remember(messages, lastReadTimestamp) {
        messages.indexOfFirst { it.timestamp > lastReadTimestamp }
    }

    LaunchedEffect(messages) {
        if (firstNewMessageIndex == -1 && messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.scrollToItem(messages.lastIndex)
            }
        }
    }

    // 🖤 App background black
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(12.dp)
    ) {

        // 💬 Chat messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
            verticalArrangement = Arrangement.Top
        ) {
            itemsIndexed(messages) { index, msg ->

                if (index == firstNewMessageIndex && firstNewMessageIndex != -1) {
                    NewMessageDivider()
                }

                MessageBubble(message = msg, isMine = msg.sender == myName)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🟩 Input bar and Send button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Type a message", color = Color.Gray) },
                modifier = Modifier
                    .weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    cursorColor = Color.Green,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )


            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        viewModel.sendMessage(myName, input)
                        input = ""
                        lastReadTimestamp = System.currentTimeMillis()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C853), // bright green
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Send", color = Color.Black)
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isMine: Boolean) {
    val bgColor = if (isMine) Color(0xFF1B5E20) else Color(0xFF2E2E2E) // green mine, dark gray others
    val textColor = Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor)
                .padding(10.dp)
        ) {
            Text(
                text = message.sender,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
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
            color = Color.Green,
            thickness = 1.dp
        )
        Text(
            "  New Messages  ",
            color = Color.Green,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.Green,
            thickness = 1.dp
        )
    }
}
